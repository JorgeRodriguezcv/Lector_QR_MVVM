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

    private var _listScanCodes = MutableStateFlow<List<ScanObjectUI>>(emptyList())
    val listScanCodes: StateFlow<List<ScanObjectUI>> = _listScanCodes

    private var _webViewEvents = MutableStateFlow<WebViewEvent>(WebViewEvent.PageFinished)
    val webViewEvent : StateFlow<WebViewEvent> = _webViewEvents

    private var _selectedItem = MutableStateFlow<SelectedItem?>(null)
    val selectedItem: StateFlow<SelectedItem?> = _selectedItem

    fun getAllScanCodes() {
        /** lanzo una corrutina, para invocar la operacion del caso de uso que es Suspend */
        viewModelScope.launch {
                /** ejecuto en hilo secundario para recuperar los valores la primea vez */
                val listScans = scanRepository.getAllScanCodes()
                if (listScans.size > 3) {
                    _listScanCodes.value = listScans.subList(0, 3)
                } else {
                    _listScanCodes.value = listScans
                }

            scanRepository.listScanCodes.collect { listScans ->
                if (listScans.size > 3) {
                    _listScanCodes.value = listScans.subList(0, 3)
                } else {
                    _listScanCodes.value = listScans
                }
            }
        }
    }

    fun deleteScan(idCodeScan: Int) {
        viewModelScope.launch {
            try {
                /** ejecuto en hilo secundario */
                withContext(Dispatchers.IO) { scanRepository.deleteScanCode(idCodeScan) }
            } catch (e: Exception) {
                println("Error al eliminar el objeto: ${e.message}")
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
                _selectedItem.value = SelectedItem.Wifi(listPartsConfigWifi[0], listPartsConfigWifi[1], listPartsConfigWifi[2])
            }
        }
    }

}