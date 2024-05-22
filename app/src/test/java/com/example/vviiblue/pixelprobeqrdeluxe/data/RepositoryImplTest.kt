package com.example.vviiblue.pixelprobeqrdeluxe.data

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.ScanCodeDao
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.entitiesTest
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.scanCodeTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class RepositoryImplTest {

    @MockK
    lateinit var dao: ScanCodeDao   // I inject dependency, i simulate the injection with dagger hilt
    private lateinit var repositoryImpl: RepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this) // if i don't want to test all operations, just need to add "relaxUnitFun = true"
        repositoryImpl = RepositoryImpl(dao) // i make a repositoryImpl instance
    }


    @Test
    fun `getAllScanCodes should return a correct list mapped`() =
        runBlocking {  // i use "runBlocking" because i want to simulate the method suspend of "insertScanCode"

            /**Given*/
            coEvery { dao.getAllScanCodes() } returns entitiesTest

            /**when*/
            val result = repositoryImpl.getScanCodes()

            /**then*/
            coVerify { dao.getAllScanCodes() } // getAllScanCodes was called in Dao?

            assertEquals(entitiesTest.size, result.size)

            entitiesTest.forEachIndexed{index,entity -> // all of scan code are equals?
                assertEquals(entity.id, result[index].scanIdCode)
                assertEquals(entity.code, result[index].scanCode)
                assertEquals(entity.note, result[index].scanNote)
            }

        }

    @Test
    fun `insertScanCode should return a correct id insert of the new scan code in Room`() =
        runBlocking {
            /**Given*/
            val resultIdNewScanCodeInserted = 5L
            coEvery { dao.insertScanCode(scanCodeTest) } returns resultIdNewScanCodeInserted

            /**When*/
            val resultId = repositoryImpl.insertScanCode(scanCodeTest)

            /**Then*/
            coVerify{dao.insertScanCode(scanCodeTest)}
            assertEquals(resultIdNewScanCodeInserted, resultId)

        }

    @Test
    fun `deleteScanCode should remove the correct scan code by a Id`() = runBlocking{
        /**Given*/
        val idScanToRemove = 1
        coEvery { dao.deleteScanCode(idScanToRemove) } returns Unit

        /**When*/
         repositoryImpl.deleteScanCode(idScanToRemove)


        /**Then*/
        coVerify{dao.deleteScanCode(idScanToRemove)}

    }


}