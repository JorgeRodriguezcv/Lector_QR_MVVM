package com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase

import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.toScanUI
import javax.inject.Inject


class GetScanCodesUseCase @Inject constructor(private val repository: Repository){

    suspend operator fun invoke():List<ScanObjectUI> = repository.getScanCodes().map { it.toScanUI()}

}