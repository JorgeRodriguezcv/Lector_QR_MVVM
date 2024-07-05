package com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase

import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository
import javax.inject.Inject

class DeleteScanCodeUseCase  @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(idScan:Int) = repository.deleteScanCode(idScan)

}