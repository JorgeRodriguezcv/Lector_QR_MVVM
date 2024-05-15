package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

import android.text.Editable

object Utils { // static utls
     fun getConfigurationWifi(wifiData: String): List<String> {
        val parts: List<String> = wifiData.split(";")
        var ssid: String = ""
        var password: String = ""
        var securityType: String = ""
        for (part in parts) {
            if (part.startsWith("S:")) {
                ssid = part.substring(2)
            } else if (part.startsWith("P:")) {
                password = part.substring(2)
            } else if (part.startsWith("WIFI")) {
                val _securityType = part.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[2]
                securityType = _securityType
            }
        }

        return listOf(securityType, password, ssid)

    }

    /** Función de extensión para convertir un String a Editable */
    fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }
}