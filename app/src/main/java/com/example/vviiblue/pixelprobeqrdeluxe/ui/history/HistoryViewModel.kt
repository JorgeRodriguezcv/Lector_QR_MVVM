package com.example.vviiblue.pixelprobeqrdeluxe.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.GetScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val _getScanCodesUseCase: GetScanCodesUseCase) :
    ViewModel() {
    private var _listScanCodes = MutableStateFlow<List<ScanObjectUI>>(emptyList())
    val listScanCodes: StateFlow<List<ScanObjectUI>> = _listScanCodes

    init {
        getAllScanCodes()
    }

    fun getAllScanCodes() {
        /** lanzo una corrutina, para invocar la operacion del caso de uso que es Suspend */
        viewModelScope.launch {
            /** ejecuto en hilo secundario */
            val listScans = withContext(Dispatchers.IO) { _getScanCodesUseCase() }
            _listScanCodes.value = listScans
        }
    }

    fun onScanUpdated(scan: ScanObjectUI) {
        _listScanCodes.value = (_listScanCodes.value + scan)
    }

}