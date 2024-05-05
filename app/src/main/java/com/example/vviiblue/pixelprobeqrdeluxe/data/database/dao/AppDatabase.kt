package com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity

@Database(entities = [ScanCodeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getScanCodeDao(): ScanCodeDao
}