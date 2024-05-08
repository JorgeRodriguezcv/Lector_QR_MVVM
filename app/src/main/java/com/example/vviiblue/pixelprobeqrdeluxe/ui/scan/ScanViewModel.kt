package com.example.vviiblue.pixelprobeqrdeluxe.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.toScanDomain
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.InsertScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import com.example.vviiblue.pixelprobeqrdeluxe.ui.history.HistoryViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.home.HomeViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun insertScanCode(scanCode: ScanObjectUI) {
        viewModelScope.launch {
            scanRepository.insertScanCode(scanCode)
        }
    }

}