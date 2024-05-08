package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

sealed class ScanData {
    data class Text(val text: String) : ScanData()
    data class Url(val url: String) : ScanData()
    data class Wifi(val wifiData: String) : ScanData()
}