package com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.toDatabase
import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain
import javax.inject.Inject

class DeleteScanCodeUseCase  @Inject constructor(private val repository: Repository){

    suspend operator fun invoke(idScan:Int) = repository.deleteScanCode(idScan)

}