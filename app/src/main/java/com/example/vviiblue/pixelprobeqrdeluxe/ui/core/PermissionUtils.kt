package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object  PermissionUtils {

    val WIFI_PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun hasLocationPermissions(context: Context): Boolean {
        return WIFI_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestLocationPermissions(fragment: Fragment, requestCode: Int) {
        fragment.requestPermissions(WIFI_PERMISSIONS, requestCode)
    }

}