package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel : ViewModel() {

    private var _permissionsGranted = MutableStateFlow<Boolean>(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted

    fun setPermissionsGranted(granted: Boolean) {
        _permissionsGranted.value = granted
    }
}