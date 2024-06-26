package com.example.vviiblue.pixelprobeqrdeluxe.motherobject

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity

object ScanCodesMotherObject {
    val listTestScans = listOf(
        ScanCodeEntity(0, "el primer item aregado", "Date0", "El primer item agregado"),
        ScanCodeEntity(1, "WIFI:T:WPA;S:test;P:test;H:;;", "Date1", "Nota para el WIFI"),
        ScanCodeEntity(2, "texto de prueba", "Date2", "Nota para el texto de prueba"),
        ScanCodeEntity(3, "https://example.com", "Date3", "Nota para la web scaneada"),
    )
}