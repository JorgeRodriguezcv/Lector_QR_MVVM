package com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.toDatabase
import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain
import javax.inject.Inject

class InsertScanCodesUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(scan: ScanObjectDomain): Long {
        return repository.insertScanCode(scan.toDatabase())
    }

}