package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

object  WifiUtils {

    fun connectToWifi(context: Context,securityType: String, password: String, ssid: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectToWifiSuggestion(context,securityType, password, ssid)
        } else {
            connectToWifiLegacy(context,securityType, password, ssid)
        }
    }

    // Para versiones anteriores a Android 10
    private fun connectToWifiLegacy(context: Context,securityType: String, password: String, ssid: String) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiConfig = WifiConfiguration().apply {
            SSID = "\"$ssid\""
        }

        when (securityType) {
            "WEP" -> {
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                wifiConfig.wepKeys[0] = "\"$password\""
                wifiConfig.wepTxKeyIndex = 0
            }
            "WPA", "WPA2" -> {
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
                wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
                wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)
                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                wifiConfig.preSharedKey = "\"$password\""
            }
            else -> {
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            }
        }

        val networkId = wifiManager.addNetwork(wifiConfig)
        if (networkId != -1) {
            wifiManager.disconnect()
            wifiManager.enableNetwork(networkId, true)
            wifiManager.reconnect()
        } else {
            Toast.makeText(context.applicationContext, "Error al conectar a la red WiFi", Toast.LENGTH_SHORT).show()
        }
    }

    // Para Android 10 en adelante
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectToWifiSuggestion(context: Context,securityType: String, password: String, ssid: String) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiNetworkSuggestion = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .apply {
                when (securityType) {
                    "WPA", "WPA2" -> setWpa2Passphrase(password)
                    "WPA3" -> setWpa3Passphrase(password)
                    "OPEN" -> setIsEnhancedOpen(true)
                    else -> {
                        Toast.makeText(context.applicationContext, "Error al añadir red", Toast.LENGTH_SHORT).show()
                    return
                    }
                }
            }
            .build()

        val suggestionsList = listOf(wifiNetworkSuggestion)
        val status = wifiManager.addNetworkSuggestions(suggestionsList)

        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            Toast.makeText(context.applicationContext, "Red añadida", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context.applicationContext, "Error al añadir red", Toast.LENGTH_SHORT).show()
        }
    }


}