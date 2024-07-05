package com.example.vviiblue.pixelprobeqrdeluxe.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) :
    ViewModel() {
    private var _listScanCodes = MutableStateFlow<List<ScanObjectUI>>(emptyList())
    val listScanCodes: StateFlow<List<ScanObjectUI>> = _listScanCodes

    fun getAllScanCodes() {
        /** I launch a coroutine to invoke the operation of "the use case" that is suspend */
        viewModelScope.launch {
            val listScans = scanRepository.getAllScanCodes()
            _listScanCodes.value = listScans

            scanRepository.listScanCodes.collect { listScans ->
                _listScanCodes.value = listScans
            }
        }
    }

    fun deleteScan(idCodeScan: Int) {
        viewModelScope.launch {
            try {
                /** Execute on the secondary thread */
                withContext(Dispatchers.IO) { scanRepository.deleteScanCode(idCodeScan) }
            } catch (e: Exception) {
                println("Error deleting the object: ${e.message}")
            }
        }
    }

}