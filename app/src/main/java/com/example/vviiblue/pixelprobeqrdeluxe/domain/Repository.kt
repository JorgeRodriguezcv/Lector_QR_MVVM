package com.example.vviiblue.pixelprobeqrdeluxe.domain

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain

interface Repository {
    suspend fun getScanCodes(): List<ScanObjectDomain>
    suspend fun insertScanCode(list:List<ScanCodeEntity>)
    suspend fun insertScanCode(scanCode:ScanCodeEntity): Long
    suspend fun deleteScanCode(idScan:Int)
}