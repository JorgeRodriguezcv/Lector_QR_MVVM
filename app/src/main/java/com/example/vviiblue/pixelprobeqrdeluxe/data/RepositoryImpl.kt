package com.example.vviiblue.pixelprobeqrdeluxe.data

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.ScanCodeDao
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity
import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.toScanDomain
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val dao: ScanCodeDao) : Repository {
    override suspend fun getScanCodes(): List<ScanObjectDomain> {
       val response : List<ScanCodeEntity> = dao.getAllScanCodes()
        return response.map { it.toScanDomain() }
    }

    override suspend fun insertScanCode(listScans:List<ScanCodeEntity>){
        dao.insertAllScanCodes(listScans)
    }

    override suspend fun insertScanCode(scanCode: ScanCodeEntity): Long {
      return dao.insertScanCode(scanCode)
    }

    override suspend fun deleteScanCode(idScan: Int) {
        dao.deleteScanCode(idScan)
    }

}