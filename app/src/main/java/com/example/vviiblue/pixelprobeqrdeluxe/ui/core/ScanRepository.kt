package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

import android.util.Log
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.toScanDomain
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.DeleteScanCodeUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.GetScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.InsertScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScanRepository @Inject constructor(
    private val _getScanCodesUseCase: GetScanCodesUseCase,
    private val _deleteScanCodeUseCase: DeleteScanCodeUseCase,
    private val _insertScanCodesUseCase: InsertScanCodesUseCase
){

    private val _listScanCodes = MutableStateFlow<List<ScanObjectUI>>(emptyList())
    val listScanCodes: StateFlow<List<ScanObjectUI>> = _listScanCodes

     suspend  fun getAllScanCodes(): List<ScanObjectUI> {
         println("ScanRepository getAllScanCodes ")
        if(_listScanCodes.value.isEmpty()) {
            val listScans = withContext(Dispatchers.IO) { _getScanCodesUseCase() }
            _listScanCodes.value = listScans
        }
        return _listScanCodes.value
    }

     suspend fun insertScanCode(scanCode: ScanObjectUI) {
         withContext(Dispatchers.IO) {
             val id = _insertScanCodesUseCase(scanCode.toScanDomain())
             if (id != null) {
                 scanCode.copy(scanIdCode = id.toInt()).also { updatedScan ->

                     _listScanCodes.value = listOf(updatedScan) + _listScanCodes.value //listOf(updatedScan) creo una lista con un solo elemento
                     Log.d("Insert", "Inserted scan with ID: $id")
                 }
             } else {
                 Log.e("Insert", "Failed to insert scan")
             }
         }
    }

     suspend fun deleteScanCode(idCodeScan: Int) {
            try {
                /** ejecuto en hilo secundario */
                println("ScanRepository deleteScanCode - idCodeScan: $idCodeScan")
                withContext(Dispatchers.IO) { _deleteScanCodeUseCase(idCodeScan) }.also {
                    onScanDeleteScan(idCodeScan)
                }
            } catch (e: Exception) {
                println("Error al eliminar el objeto: ${e.message}")
            }
    }


    fun onScanDeleteScan(idCodeScan: Int) {
        // actualiza la lista y notifica a los observadores
        println("Antes - onScanDeleteScan - _listScanCodes.value: ${listScanCodes.value}")
        _listScanCodes.value = listScanCodes.value.filter { it.scanIdCode != idCodeScan }
        println("Despues - onScanDeleteScan - _listScanCodes.value: ${listScanCodes.value}")
    }
}