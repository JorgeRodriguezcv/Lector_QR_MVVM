package com.example.vviiblue.pixelprobeqrdeluxe.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun insertScanCode(scanCode: ScanObjectUI) {
        viewModelScope.launch {
            val updatedScanCode = scanCode.copy(scanDate = getCurrentDate())
            scanRepository.insertScanCode(updatedScanCode)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("EEEE, d 'de' yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

}