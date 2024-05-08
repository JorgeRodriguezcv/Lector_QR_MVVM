package com.example.vviiblue.pixelprobeqrdeluxe.ui.model

import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain

data class ScanObjectUI(
    val scanIdCode: String,
    val scanCode: String,
    val scanDate: String,
    val scanNote: String
)

fun ScanObjectDomain.toScanUI() = ScanObjectUI(scanIdCode = scanIdCode ,scanCode = scanCode,scanDate = scanDate,scanNote = scanNote)
