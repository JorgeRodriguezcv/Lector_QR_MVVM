package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.scanObjectUITest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.textDataTest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.urlDataTest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.wifiDataTest
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.SelectedItem

//JUnit4
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

        //Mock responses for scanRepository's methods
        coEvery { scanRepository.getAllScanCodes() } returns scanObjectUITest
        coEvery { scanRepository.listScanCodes } returns MutableStateFlow(scanObjectUITest)  // i make sure that the list has a MutableStateFlow with test data

        viewModel = HomeViewModel(scanRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher
    }


    @Test
    fun `getAllScanCodes should update correctly listScanCodes`() = runTest {
        /**Given */
        coEvery { scanRepository.getAllScanCodes() } returns scanObjectUITest

        /**When */
        viewModel.getAllScanCodes()
        advanceUntilIdle() // Ensure all coroutines have completed
        val result = viewModel.listScanCodes.value

        /**Then */
        coVerify { scanRepository.getAllScanCodes() }
        Assert.assertEquals(scanObjectUITest.subList(0, 3), result)
    }

    @Test
    fun `deleteScanCode should remove the scan code from room and listScanCodes`() = runTest {

        /**Given */
        val scanIdToRemove = scanObjectUITest.first().scanIdCode

        /**When */
        viewModel.deleteScan(scanIdToRemove)
        coEvery { scanRepository.deleteScanCode(scanIdToRemove) }


        advanceUntilIdle() // Ensure all coroutines have completed
        val result = viewModel.listScanCodes.value

        /**Then */
        coVerify { scanRepository.deleteScanCode(scanIdToRemove) }

    }

    @Test
    fun `handleSelectedItem should change the _selectedItem value correctly`() = runTest {
        /**Give */
        val urlExample = urlDataTest
        val textExample = textDataTest
        val wifiExample = wifiDataTest
        var result: SelectedItem? = null
        /**When */
        viewModel.handleSelectedItem(urlExample)
        result = viewModel.selectedItem.value

        /*** Then */
        Assert.assertTrue(result is SelectedItem.Url && (result as SelectedItem.Url).url == urlExample.url)

        /**When */
        viewModel.handleSelectedItem(textExample)
        result = viewModel.selectedItem.value

        /*** Then */
        Assert.assertTrue(result is SelectedItem.Text && (result as SelectedItem.Text).text == textExample.text)

        /**When */
        viewModel.handleSelectedItem(wifiExample)
        result = viewModel.selectedItem.value

        /*** Then */
        Assert.assertTrue(result is SelectedItem.Wifi && wifiExample.wifiData.contains((result as SelectedItem.Wifi).networkName))

    }


    @Test
    fun `onPageStarted and onPageFinished should change value correctrly`()= runTest {

        var result : WebViewEvent? = null

        /**Given */
        viewModel.onPageFinished()
        /**When */
        result = viewModel.webViewEvent.value
        /**Then */
        Assert.assertTrue(result == WebViewEvent.PageFinished)

        /**Given */
        viewModel.onPageStarted()
        /**When */
        result = viewModel.webViewEvent.value
        /**Then */
        Assert.assertTrue(result == WebViewEvent.PageStarted)
    }


}
