package com.example.vviiblue.pixelprobeqrdeluxe.di

import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.DeleteScanCodeUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.GetScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.domain.usecase.InsertScanCodesUseCase
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideScanRepository(
        getScanCodesUseCase: GetScanCodesUseCase,
        deleteScanCodeUseCase: DeleteScanCodeUseCase,
        insertScanCodesUseCase: InsertScanCodesUseCase
    ): ScanRepository {
        return ScanRepository(getScanCodesUseCase, deleteScanCodeUseCase, insertScanCodesUseCase)
    }
}