package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

sealed class WebViewEvent {
    object PageStarted : WebViewEvent()
    object PageFinished: WebViewEvent()
}