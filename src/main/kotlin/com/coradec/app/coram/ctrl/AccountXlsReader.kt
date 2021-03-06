package com.coradec.app.coram.ctrl

import com.coradec.app.coram.model.impl.BasicAccount
import java.net.URL
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CellType.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class AccountXlsReader(private val location: URL) {
    fun run() {
        Accounts.clear()
        location.openStream().use {
            XSSFWorkbook(it).let { workbook ->
                workbook.sheetIterator().asSequence().forEach { sheet ->
                    sheet.rowIterator().asSequence()
                        .filter { row -> row.getCell(8)?.cellType == NUMERIC && row.getCell(0).cellType == NUMERIC  }
                        .forEach { row ->
                            val nr = row.getCell(0)?.numericCellValue?.toInt()
                            val name = row.getCell(1)?.stringCellValue
                            val currency = row.getCell(3)?.stringCellValue ?: "CHF"
                            val vatCode = row.getCell(5)?.stringCellValue
                            val agroup = row.getCell(8)?.numericCellValue?.toInt()
                            if (nr != null && name != null && agroup != null)
                                Accounts.add(BasicAccount(nr, name, currency, vatCode, agroup))
                        }
                }
            }
        }
    }
}