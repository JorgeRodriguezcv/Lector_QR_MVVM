package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.scanObjectUITest
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository

//JUnit4
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
//*****

class HomeViewModelTest{

    @MockK
    lateinit var scanRepository: ScanRepository
    private lateinit var viewModel: HomeViewModel

    // I create a "fake" main dispatcher to replace "Dispatchers.Main" in the test, i don't have access to the Android Main Thread in the "Unit Test", this is necessary because it is used in "HomeViewModel"
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(scanRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher
    }


    @Test
    fun `getAllScanCodes should update correctly listScanCodes`() = runTest {
        /**Given */
        coEvery { scanRepository.listScanCodes } returns MutableStateFlow(scanObjectUITest)  // i make sure that the list has a MutableStateFlow with test data
        coEvery { scanRepository.getAllScanCodes() } returns scanObjectUITest

        /**When */
        viewModel.getAllScanCodes()
        val result = viewModel.listScanCodes.value

        /**Then */
        coVerify { scanRepository.getAllScanCodes() }
        Assert.assertEquals(scanObjectUITest.subList(0, 3), result)
    }


}