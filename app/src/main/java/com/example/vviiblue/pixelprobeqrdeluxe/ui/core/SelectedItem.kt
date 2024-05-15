package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

sealed class SelectedItem {
    data class Url(val url: String) : SelectedItem()
    data class Text(val text: String) : SelectedItem()
    data class Wifi(val encryption: String, val password: String, val networkName: String) : SelectedItem()
}