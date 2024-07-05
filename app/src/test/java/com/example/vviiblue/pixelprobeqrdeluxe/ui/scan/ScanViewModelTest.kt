package com.example.vviiblue.pixelprobeqrdeluxe.ui.scan

import com.example.vviiblue.pixelprobeqrdeluxe.MainDispatcherRule
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.scanObjectUITest
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class ScanViewModelTest {

    @MockK
    lateinit var scanRepository: ScanRepository
    private lateinit var scanViewModel:ScanViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp(){

        MockKAnnotations.init(this, relaxUnitFun = true)

        // With spyk, i can use the real object but change the behavior of some methods that i want to override
        scanViewModel = spyk(ScanViewModel(scanRepository))
    }

    @After
    fun tearDown() {
    //    Dispatchers.resetMain()
    }

    @Test
    fun `insertScanCode should insert a code scaned correctly`() = runTest {

        /**Given */
        val firstScanCode = scanObjectUITest.first()
        val currentDate = "sábado, 25 de 2024"
        val scanCodeToInsert = firstScanCode.copy(scanDate = currentDate)

        /**When */
        every { scanViewModel.getCurrentDate() } returns currentDate

        coEvery { scanRepository.insertScanCode(scanCodeToInsert) } just Runs // i use "just Runs" to tell that this don't return anything
        scanViewModel.insertScanCode(scanCodeToInsert)

        /**Then */
        coVerify { scanRepository.insertScanCode(scanCodeToInsert) }

    }



}