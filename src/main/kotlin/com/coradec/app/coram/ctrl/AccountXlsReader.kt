package com.coradec.app.coram.ctrl

import com.coradec.app.coram.model.Account
import com.coradec.app.coram.model.impl.BasicAccount
import java.net.URL
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class AccountXlsReader(val location: URL) {
    fun run() {
        Accounts.clear()
        location.openStream().use {
            XSSFWorkbook(it).let { workbook ->
                workbook.sheetIterator().asSequence().forEach { sheet ->
                    sheet.rowIterator().asSequence()
                        .filter { row -> row.getCell(8).cellType == CellType.NUMERIC }
                        .forEach { row ->
                            val nr = row.getCell(0).numericCellValue.toInt()
                            val name = row.getCell(1).stringCellValue
                            val currency = row.getCell(3).stringCellValue
                            val vatCode = row.getCell(4).stringCellValue
                            val group = row.getCell(8).numericCellValue.toInt()
                            Accounts.add(BasicAccount(nr, name, currency, vatCode, group))
                        }
                }
            }
        }
    }
}