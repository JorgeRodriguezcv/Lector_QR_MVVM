package com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity


@Dao
interface ScanCodeDao {

    @Query("SELECT * FROM scan_code_table ORDER BY id DESC")
    suspend fun getAllScanCodes():List<ScanCodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllScanCodes(list:List<ScanCodeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanCode(scan:ScanCodeEntity): Long

    @Query("DELETE FROM scan_code_table WHERE id = :scanId")
    suspend fun deleteScanCode(scanId : Int)

}