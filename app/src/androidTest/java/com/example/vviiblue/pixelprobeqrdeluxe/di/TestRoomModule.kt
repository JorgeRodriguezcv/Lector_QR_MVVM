package com.example.vviiblue.pixelprobeqrdeluxe.di

import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import com.example.vviiblue.pixelprobeqrdeluxe.data.RepositoryImpl
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.AppDatabase
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.ScanCodeDao
import com.example.vviiblue.pixelprobeqrdeluxe.domain.Repository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RoomModule::class] // i replaced the production module
)
object TestRoomModule {

    @Provides
    @Singleton
    fun provideInMemoryDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Just for testing
            .build()
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