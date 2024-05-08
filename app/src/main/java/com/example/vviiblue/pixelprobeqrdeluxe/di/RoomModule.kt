package com.example.vviiblue.pixelprobeqrdeluxe.di

import android.content.Context
import androidx.room.Room
import com.example.vviiblue.pixelprobeqrdeluxe.data.RepositoryImpl
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.AppDatabase
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.ScanCodeDao
import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val SCAN_CODE_DATABASE_NAME = "pixel_probe_qr"

    @Volatile
    private var INSTANCE: AppDatabase? = null

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            SCAN_CODE_DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideScanCodeDao(database: AppDatabase): ScanCodeDao {
        return database.getScanCodeDao()
    }

    @Provides
    fun provideRepository(dao: ScanCodeDao): Repository {
        return RepositoryImpl(dao)
    }

}