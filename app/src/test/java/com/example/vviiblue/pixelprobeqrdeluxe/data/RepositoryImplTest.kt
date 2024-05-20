package com.example.vviiblue.pixelprobeqrdeluxe.data

import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.ScanCodeDao
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity
import io.kotlintest.shouldNotBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class RepositoryImplTest {

    @MockK
    lateinit var dao: ScanCodeDao   // I inject dependency, i simulate the injection with dagger hilt
    lateinit var repositoryImpl: RepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this) // if i don't want to test all operations, just need to add "relaxUnitFun = true"
        repositoryImpl = RepositoryImpl(dao) // i make a repositoryImpl instance
    }


    @Test
    fun `getAllScanCodes should return a correct list mapped`() =
        runBlocking {  // i use "runBlocking" because i want to simulate the method suspend of "insertScanCode"
            val entities  = listOf(
                ScanCodeEntity(1, "scanCode01", "date01", "text note"),
                ScanCodeEntity(2, "scanCode02", "date02", "text note"),
                ScanCodeEntity(3, "scanCode03", "date03", ""),
                ScanCodeEntity(4, "scanCode04", "date04", "text note test")
            )
            /**Given*/
            coEvery { dao.getAllScanCodes() } returns entities

            /**when*/
            val result = repositoryImpl.getScanCodes()

            /**then*/
            coVerify { dao.getAllScanCodes() } // getAllScanCodes was called in Dao?

            assertEquals(entities.size, result.size)

            entities.forEachIndexed{index,entity -> // all of scan code are equals?
                assertEquals(entity.id, result[index].scanIdCode)
                assertEquals(entity.code, result[index].scanCode)
                assertEquals(entity.note, result[index].scanNote)
            }

        }

    @Test
    fun `insertScanCode should return a correct id insert of the new scan code in Room`() =
        runBlocking {
            /**Given*/
            val scanCode = ScanCodeEntity(-1,"new Code Insert", "date of new code scan", "note")
            val resultIdNewScanCodeInserted = 5L
            coEvery { dao.insertScanCode(scanCode) } returns resultIdNewScanCodeInserted

            /**When*/
            val resultId = repositoryImpl.insertScanCode(scanCode)

            /**Then*/
            coVerify{dao.insertScanCode(scanCode)}
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