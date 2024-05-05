package com.example.vviiblue.pixelprobeqrdeluxe.domain.model

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI

data class ScanObjectDomain(
    val scanCode: String,
    val scanDate: String,
    val scanNote: String
)

fun ScanCodeEntity.toScanDomain() = ScanObjectDomain(code,date,note)
fun ScanObjectUI.toScanDomain() = ScanObjectDomain(scanCode = scanCode,scanDate = scanDate,scanNote = scanNote)

