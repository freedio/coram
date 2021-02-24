package com.coradec.app.coram.ctrl.impl

import com.coradec.app.coram.annot.Size
import com.coradec.app.coram.ctrl.Selection
import com.coradec.app.coram.ctrl.SqlEngine
import com.coradec.app.coram.ctrl.SqlFilter
import com.coradec.app.coram.trouble.ExcessResultsException
import com.coradec.coradeck.com.ctrl.impl.Logger
import com.coradec.coradeck.com.module.CoraCom
import com.coradec.coradeck.conf.model.LocalProperty
import com.coradec.coradeck.core.util.formatted
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.provider.SortDirection.*
import java.math.BigDecimal
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Spliterator.IMMUTABLE
import java.util.Spliterator.ORDERED
import java.util.function.Consumer
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.NoSuchElementException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class BasicSqlEngine<T: Any>(val klass: KClass<out T>) : Logger(), SqlEngine<T>, AutoCloseable {
    private val connection = DriverManager.getConnection(dbUrlProperty.value, usernameProperty.value, passwordProperty.value)
    override val tableName: String = klass.simpleName?.toSqlObjectName()
        ?: throw IllegalArgumentException("Unsupported class: $klass")
    internal val columnNames: List<String>
        get() = connection.metaData
            .getColumns(null, null, tableName, null)
            .listOf(ColumnMetadata::class).map { it.columnName }.ifEmpty {
                fieldNames.map { it.toSqlObjectName() }
            }
    internal val tableNames: List<String>
        get() = connection.metaData
            .getTables(null, null, tableName, listOf("TABLE").toTypedArray())
            .listOf(TableMetadata::class).map { it.tableName }
    internal val columnDefinitions: List<Pair<String, String>> get() = connection.metaData
        .getColumns(null, null, tableName, null)
        .listOf(ColumnMetadata::class).map { Pair(it.columnName, it.typeName.withSize(it.columnSize)) }.ifEmpty {
            fields.map { Pair(it.key.toSqlObjectName(), it.value.toSqlType()) }
        }
    internal val fields: Map<String, Pair<KClass<*>, List<Annotation>>> get() = klass.members
        .filter { it.name in fieldNames }
        .map { Pair(it.name, Pair(it.returnType.classifier as KClass<*>, it.returnType.annotations)) }.toMap()
    internal val fieldNames: List<String> get() = klass.memberProperties.map { it.name }
    private val statement = connection.createStatement()

    override fun count(query: Query<T, SqlFilter>): Int {
        val stmt = "select count(*) from $tableName${query.toSqlQuery()}"
        debug("Executing query «$stmt»")
        return statement.executeQuery(stmt).checkedSingleOf(Int::class)
    }
    override fun query(query: Query<T, SqlFilter>): Stream<T> {
        val stmt = "select * from $tableName${query.toSqlQuery()}"
        debug("Executing query «$stmt»")
        @Suppress("UNCHECKED_CAST")
        return statement.executeQuery(stmt).streamOf(klass) as Stream<T>
    }
    override fun insert(entry: T): Int {
        val record = entry::class.members.filter { it.name in fieldNames }.map { Pair(it.name, it.call(entry)) }.toMap()
        val stmt = "insert into $tableName (${record.keys.joinToString { it.toSqlObjectName() }}) " +
                "values (${record.values.joinToString { it.toSqlValueRepr() }})"
        debug("Executing command «$stmt»")
        return statement.executeUpdate(stmt)
    }
    override fun delete(selection: Selection): Int {
        val stmt = "delete from $tableName${selection.where}"
        debug("Executing command «$stmt»")
        return statement.executeUpdate(stmt)
    }
    override fun assertTable() {
        val stmt = "create table if not exists $tableName (${columnDefinitions.joinToString { "${it.first} ${it.second}" }})"
        debug("Executing command «$stmt»")
        statement.executeUpdate(stmt)
        connection.commit()
    }

    companion object {
        val dbUrlProperty = LocalProperty<String>("DbUrl")
        val usernameProperty = LocalProperty<String>("Username")
        val passwordProperty = LocalProperty<String>("Password")
    }

    override fun close() {
        connection.close()
    }

    private fun String.withSize(columnSize: Int?): String = if (columnSize == null) this else when(this) {
        "VARCHAR", "CHAR", "LONGVARCHAR" -> "$this($columnSize)"
        "VARBINARY", "BINARY" -> "#this($columnSize)"
        else -> this
    }
}

private fun Any?.toSqlValueRepr() = when (this) {
    null -> "NULL"
    is String -> "'$this'"
    is LocalDate -> "DATE '$this'"
    is LocalTime -> "TIME '$this'"
    is LocalDateTime -> "TIMESTAMP '${this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}'"
    else -> toString()
}

private fun Pair<KClass<*>, List<Annotation>>.toSqlType(): String = first.toSqlType(second)
private fun KClass<*>.toSqlType(annotations: List<Annotation>): String = when (this) {
    String::class -> "VARCHAR(%d)".format((annotations.single { it is Size } as Size).value)
    Boolean::class -> "BIT"
    Byte::class -> "TINYINT"
    Short::class -> "SMALLINT"
    Int::class -> "INTEGER"
    Long::class -> "BIGINT"
    Float::class -> "FLOAT"
    Double::class -> "DOUBLE"
    BigDecimal::class -> "NUMERIC"
    LocalDate::class -> "DATE"
    LocalTime::class -> "TIME"
    LocalDateTime::class -> "TIMESTAMP"
    else -> throw IllegalArgumentException("Type \"$this\" cannot be translated to SQL!")
}

data class TableMetadata(
    val tableCat: String?,
    val tableSchem: String?,
    val tableName: String,
    val tableType: String,
    val remarks: String?,
    val typeCat: String?,
    val typeSchem: String?,
    val typeName: String?,
    val selfReferencingColumnName: String?,
    val refGeneration: String?
)

data class ColumnMetadata(
    val tableCat: String?,
    val tableSchem: String?,
    val tableName: String,
    val columnName: String,
    val dataType: Int,
    val typeName: String,
    val columnSize: Int?,
    val decimalDigits: Long?,
    val numPrecRadix: Int?,
    val nullable: Int,
    val remarks: String?,
    val columnDef: String?,
    val charOctetLength: Int,
    val ordinalPosition: Int,
    val isNullable: String,
    val scopeCatalog: String?,
    val scopeSchema: String?,
    val scopeTable: String?,
    val sourceDataType: Short?,
    val isAutoIncrement: String?,
    val isGeneratedColumn: String?
)

@Suppress("UNCHECKED_CAST")
fun <T : Any> ResultSet.encode(klass: KClass<T>): T {
    if (klass == Int::class) return getInt(1) as T
    if (klass == String::class) return getString(1) as T
    val sqlValues: Map<String, Any?> = klass.fields
        .mapKeys { (name, _) -> name.toSqlObjectName() }
        .map { Pair(it.value.name, getObjectOrNull(it.key).toSqlFieldValue()) }.toMap()
    val pcon = klass.primaryConstructor ?: throw IllegalArgumentException("$klass has no primary constructor!")
    val args = pcon.valueParameters.map { Pair(it, it.name) }.toMap().mapValues { sqlValues[it.value] }
    CoraCom.log.debug("Calling ${pcon.name}(${args.entries.joinToString { "${it.key.name}:${it.key.type} = ${it.value.formatted}:${it.value?.javaClass?.name}" }})")
    return pcon.callBy(args)
}

fun ResultSet.getObjectOrNull(name: String): Any? = try {
    getObject(name)
} catch (e: SQLException) {
    if (e.message?.contains("Column not found: ") == true) null else throw e
}

fun <T : Any> ResultSet.singleOf(klass: KClass<T>): T {
    if (!next()) throw NoSuchElementException("ResultSet has no rows (left)")
    return encode(klass)
}

fun <T : Any> ResultSet.checkedSingleOf(klass: KClass<T>): T {
    if (!next()) throw NoSuchElementException("ResultSet has no rows (left)")
    val result = encode(klass)
    if (next()) throw ExcessResultsException("ResultSet has more than 1 row (left)")
    return result
}

fun <T : Any> ResultSet.listOf(klass: KClass<T>): List<T> {
    val result: MutableList<T> = mutableListOf<T>()
    while (next()) {
        result += encode(klass)
    }
    return result.toList()
}

fun <T: Any> ResultSet.streamOf(klass: KClass<T>): Stream<T> = StreamSupport.stream(ResultSetSpliterator(this, klass), false)

class ResultSetSpliterator<T: Any>(private val dataset: ResultSet, private val klass: KClass<T>) : Spliterator<T> {
    override fun tryAdvance(action: Consumer<in T>): Boolean = dataset.next().also { if (it) action.accept(dataset.singleOf(klass)) }
    override fun trySplit(): Spliterator<T>? = null
    override fun estimateSize(): Long = Long.MAX_VALUE
    override fun characteristics(): Int = ORDERED or IMMUTABLE
}

private fun Any?.toSqlFieldValue(): Any? = when (this) {
    is java.sql.Date -> toLocalDate()
    is java.sql.Time -> toLocalTime()
    is java.sql.Timestamp -> toLocalDateTime()
    else -> this
}

fun String.toSqlObjectName() = replace(Regex("([A-Z])"), "_$1").toUpperCase().trimStart('_')
fun <T, F> Query<T, F>.toSqlQuery(): String {
    val limitPart = limit.let { if (it == 2147483647) "" else " LIMIT $limit" }
    val offsetPart = offset.let { if (it == 0) "" else " OFFSET $offset" }
    val orderPart = sortOrders.joinToString(", ") { "${it.sorted}${sqlSortDirection(it.direction)}" }
        .let { if (it.isEmpty()) "" else " ORDER BY $it" }
    val filterPart = "" // filter
    return "$limitPart$offsetPart$filterPart$orderPart"
}

fun sqlSortDirection(direction: SortDirection?): String = when (direction) {
    ASCENDING -> " asc"
    DESCENDING -> " desc"
    null -> ""
}

val <T : Any> KClass<T>.fields: Map<String, KProperty1<T, *>> get() = memberProperties.map { Pair(it.name, it) }.toMap()