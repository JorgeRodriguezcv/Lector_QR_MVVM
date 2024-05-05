package com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vviiblue.pixelprobeqrdeluxe.domain.model.ScanObjectDomain

@Entity(tableName = "scan_code_table")
data class ScanCodeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "scan_code") val code: String,
    @ColumnInfo(name = "scan_date") val date: String,
    @ColumnInfo(name = "scan_note") val note: String
)


fun ScanObjectDomain.toDatabase() = ScanCodeEntity(code = scanCode,date = scanDate,note = scanNote)