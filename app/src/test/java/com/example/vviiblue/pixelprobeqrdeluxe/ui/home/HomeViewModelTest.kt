package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import com.example.vviiblue.pixelprobeqrdeluxe.MainDispatcherRule
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.scanObjectUITest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.textDataTest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.urlDataTest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.wifiDataTest
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanRepository
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.SelectedItem

//JUnit4
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest{

    @MockK
    lateinit var scanRepository: ScanRepository
    private lateinit var viewModel: HomeViewModel

    //this rule replaces the main dispatcher with a TestDispatcher for tests, ensuring that coroutines execute in a predictable manner.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)

        //Mock responses for scanRepository's methods
        coEvery { scanRepository.getAllScanCodes() } returns scanObjectUITest
        coEvery { scanRepository.listScanCodes } returns MutableStateFlow(scanObjectUITest)  // i make sure that the list has a MutableStateFlow with test data

        viewModel = HomeViewModel(scanRepository)
    }

    @After
    fun tearDown() {
        unmockkAll() // clean the mocks created
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
        val initialSize = viewModel.listScanCodes.value.size

        // Mock listScanCodes flow with initial data
        coEvery { scanRepository.listScanCodes } returns MutableStateFlow(scanObjectUITest)


        /**When */
        viewModel.deleteScan(scanIdToRemove)
        advanceUntilIdle() // Ensure all coroutines have completed
        val result = viewModel.listScanCodes.value

        /**Then */
        coVerify { scanRepository.deleteScanCode(scanIdToRemove) }
        assertEquals(initialSize - 1, result.size)
        assertEquals(scanObjectUITest.subList(1, 3), result) // Check remaining items
        Assert.assertFalse(result.any { it.scanIdCode == scanIdToRemove })
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
