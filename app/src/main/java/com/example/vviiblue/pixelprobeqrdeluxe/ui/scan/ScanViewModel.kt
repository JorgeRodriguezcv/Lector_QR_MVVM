package com.example.vviiblue.pixelprobeqrdeluxe.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.toScanDomain
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.InsertScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.ui.history.HistoryViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.home.HomeViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val _insertScanCodesUseCase: InsertScanCodesUseCase
) : ViewModel() {

    fun insertScanCode(scanCode : ScanObjectUI) {
        /** lanzo una corrutina, para invocar la operacion del caso de uso que es Suspend */
        viewModelScope.launch {
            /** ejecuto en hilo secundario */
             withContext(Dispatchers.IO) { _insertScanCodesUseCase(scanCode.toScanDomain()) }
        }

    }

}