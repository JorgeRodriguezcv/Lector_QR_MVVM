package com.example.vviiblue.pixelprobeqrdeluxe.motherobject

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI

object ScanCodesMotherObject {

    val scanCodeTest = ScanCodeEntity(-1,"new Code Insert", "date of new code scan", "note")

    val entitiesTest  = listOf(
        ScanCodeEntity(1, "scanCode01", "date01", "text note"),
        ScanCodeEntity(2, "scanCode02", "date02", "text note"),
        ScanCodeEntity(3, "scanCode03", "date03", ""),
        ScanCodeEntity(4, "scanCode04", "date04", "text note test")
    )

    val scanObjectUITest  = listOf(
        ScanObjectUI(1, "scanCode01", "date01", "text note"),
        ScanObjectUI(2, "scanCode02", "date02", "text note"),
        ScanObjectUI(3, "scanCode03", "date03", ""),
        ScanObjectUI(4, "scanCode04", "date04", "text note test")
    )

    val urlDataTest = ScanData.Url("https://google.com")
    val textDataTest = ScanData.Text("Hello World")
    val wifiDataTest = ScanData.Wifi("WIFI:T:WPA;S:test;P:passsss;H:;;")

}