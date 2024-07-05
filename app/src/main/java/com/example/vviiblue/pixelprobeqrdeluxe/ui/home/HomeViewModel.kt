package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.SelectedItem
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.Utils.getConfigurationWifi
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {

    private val _listScanCodes = MutableStateFlow<List<ScanObjectUI>>(emptyList())
    val listScanCodes: StateFlow<List<ScanObjectUI>> = _listScanCodes

    private var _webViewEvents = MutableStateFlow<WebViewEvent>(WebViewEvent.PageFinished)
    val webViewEvent: StateFlow<WebViewEvent> = _webViewEvents

    private var _selectedItem = MutableStateFlow<SelectedItem?>(null)
    val selectedItem: StateFlow<SelectedItem?> = _selectedItem

    init {
        viewModelScope.launch {
            scanRepository.getAllScanCodes()
            scanRepository.listScanCodes.collect { listScans ->
                _listScanCodes.value =
                    if (listScans.size > 3) listScans.subList(0, 3) else listScans
            }
        }
    }

    fun getAllScanCodes() {
        /** I launched a coroutine, to invoke the operation of the use case that uses Suspend */
        viewModelScope.launch {
            val listScans =
                withContext(Dispatchers.IO) { scanRepository.getAllScanCodes() }/*= scanRepository.getAllScanCodes()*/
            _listScanCodes.value = if (listScans.size > 3) listScans.subList(0, 3) else listScans
        }
    }

    fun deleteScan(idCodeScan: Int) {
        viewModelScope.launch {
            try {
                scanRepository.deleteScanCode(idCodeScan)
                val updatedList = _listScanCodes.value.filter { it.scanIdCode != idCodeScan }
                _listScanCodes.value = updatedList
            } catch (e: Exception) {
                println("Error deleting object: ${e.message}")
            }
        }
    }

    fun onPageStarted() {
        _webViewEvents.value = WebViewEvent.PageStarted
    }

    fun onPageFinished() {
        _webViewEvents.value = WebViewEvent.PageFinished
    }

    fun handleSelectedItem(dataScan: ScanData) {
        _selectedItem.value = null
        when (dataScan) {
            is ScanData.Url -> {
                _webViewEvents.value = WebViewEvent.PageStarted
                _selectedItem.value = SelectedItem.Url(dataScan.url)
            }

            is ScanData.Text -> {
                _selectedItem.value = SelectedItem.Text(dataScan.text)
            }

            is ScanData.Wifi -> {
                val listPartsConfigWifi = getConfigurationWifi(dataScan.wifiData)
                _selectedItem.value = SelectedItem.Wifi(
                    listPartsConfigWifi[0],
                    listPartsConfigWifi[1],
                    listPartsConfigWifi[2]
                )
            }
        }
    }

}