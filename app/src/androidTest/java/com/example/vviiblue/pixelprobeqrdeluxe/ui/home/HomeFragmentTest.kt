package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vviiblue.pixelprobeqrdeluxe.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.filters.SmallTest
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.AppDatabase
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.dao.ScanCodeDao
import com.example.vviiblue.pixelprobeqrdeluxe.data.database.entities.ScanCodeEntity
import com.example.vviiblue.pixelprobeqrdeluxe.motherobject.ScanCodesMotherObject.listTestScans
import com.example.vviiblue.pixelprobeqrdeluxe.utils.clickInChild
import com.example.vviiblue.pixelprobeqrdeluxe.utils.launchFragmentInHiltContainer
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import javax.inject.Inject
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.rule.GrantPermissionRule
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.WifiUtils
import org.hamcrest.core.AllOf.allOf
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class HomeFragmentTest {

    private lateinit var mockContext: Context
    private lateinit var mockWifiUtils: WifiUtils

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // Necessary to simulate grant permissions
    @get:Rule
    val mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Inject
    lateinit var scanDao: ScanCodeDao

    @Before
    fun setUp() {
        hiltRule.inject()

        //I prepare the Intents, for the Test that verifies the navigation with the browser
        Intents.init()

        runBlocking {
            // Insertar datos de prueba en la base de datos
            scanDao.insertAllScanCodes(
                listTestScans
            )
        }

        // launch the fragment
        launchFragmentInHiltContainer<HomeFragment> { fragment ->
        }

        //I simulate the objects
        mockContext = mock(Context::class.java)
//        mockWifiUtils = mock(WifiUtils::class.java)

    }

    @After
    fun tearDown(){
        Intents.release()
    }


    @Test
    fun when_recycleViews_item_is_selected_and_is_url_open_webView() = runTest {
        // Usa Espresso para esperar a que el RecyclerView esté listo
        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        // Realiza una acción en un elemento específico del RecyclerView
        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        // Verifica que la nueva vista se muestra después del clic
        onView(withId(R.id.idWebViewInclude))
            .check(matches(isDisplayed()))

    }


    @Test
    fun when_recycleViews_item_is_selected_and_is_text_show_textArea() = runTest {

        // Usa Espresso para esperar a que el RecyclerView esté listo
        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        // Realiza una acción en un elemento específico del RecyclerView
        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        // Verifica que la nueva vista se muestra después del clic
        onView(withId(R.id.idTextInclude))
            .check(matches(isDisplayed()))

    }

    @Test
    fun when_recycleViews_item_is_selected_and_is_text_show_Wifi_Info() = runTest {

        // Usa Espresso para esperar a que el RecyclerView esté listo
        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        // Realiza una acción en un elemento específico del RecyclerView
        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click()
                )
            )

        // Verifica que la nueva vista se muestra después del clic
        onView(withId(R.id.idWifiInclude))
            .check(matches(isDisplayed()))

    }

    @Test
    fun when_recycleViews_icon_note_of_item_webView_is_selected_show_note() = runTest(){

        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        //test of click in the recycleview just to start the test
        onView(withId(R.id.rvHome)).perform(click())

        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickInChild(R.id.ivScanCodeNote)
                )
            )

        // the view is displayed?
        onView(withId(R.id.idTextInclude)).check(matches(isDisplayed()))


        //i testing if the note displayed is correct
        onView(withId(R.id.textContent)).check(matches(withText(listTestScans.get(3).note)))

    }

    @Test
    fun when_recycleViews_icon_note_of_item_text_is_selected_show_note() = runTest(){


        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        //test of click in the recycleview just to start the test
        onView(withId(R.id.rvHome)).perform(click())

        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    clickInChild(R.id.ivScanCodeNote)
                )
            )

        // the view is displayed?
        onView(withId(R.id.idTextInclude)).check(matches(isDisplayed()))


        //i testing if the note displayed is correct
        onView(withId(R.id.textContent)).check(matches(withText(listTestScans.get(2).note)))

    }

    @Test
    fun when_recycleViews_icon_note_of_item_wifi_is_selected_show_note() = runTest(){

        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        //test of click in the recycleview just to start the test
        onView(withId(R.id.rvHome)).perform(click())

        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    clickInChild(R.id.ivScanCodeNote)
                )
            )

        // the view is displayed?
        onView(withId(R.id.idTextInclude)).check(matches(isDisplayed()))


        //i testing if the note displayed is correct
        onView(withId(R.id.textContent)).check(matches(withText(listTestScans.get(1).note)))

    }

    @Test
    fun when_recycleViews_icon_trashCan_of_item_webView_is_selected_item_is_deleted_and_the_extra_item_of_list_take_his_place(){
        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        //test of click in the recycleview just to start the test
        onView(withId(R.id.rvHome)).perform(click())

        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickInChild(R.id.ivDeleteScanItem)
                )
            )

        try{
            // i find by a item that have a textView with the text "https://example.com" in the recycleview
            onView(withId(R.id.rvHome))
                .perform(
                    RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                        ViewMatchers.hasDescendant(withText(Matchers.containsString("https://example.com")))
                    )
                )

            Assert.fail("The element don't was deleted")
        }catch (err : Exception){
        //if i get a exception, it will be because the item that i tried off find was deleted
            ViewMatchers.assertThat(
                (err as? PerformException),
                Matchers.`is`(Matchers.notNullValue())
            )
        }

    }

    @Test
    fun when_recycleViews_icon_web_is_selected_open_browser() {

        onView(withId(R.id.rvHome))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rvHome)).perform(click())

        onView(withId(R.id.rvHome))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickInChild(R.id.ivActionScanItem)
                )
            )

        // Verify that an Intent is sent with the correct action
        Intents.intended(allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(Uri.parse("https://example.com"))
        ))
    }

//    @Test
//    fun when_recycleViews_action_icon_wifi_is_selected_attempt_wifi_connection() {
//
//        onView(withId(R.id.rvHome))
//            .check(matches(isDisplayed()))
//
//
//        onView(withId(R.id.rvHome))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                    2,
//                    clickInChild(R.id.ivActionScanItem)  // Reemplaza con el ID del ícono de acción
//                )
//            )
//
//        // Verifiy that the method "connectionWifi" has been called with these parameters
//        verify(mockWifiUtils).connectToWifi(
//            eq(mockContext),
//            eq("WPA2"),
//            eq("password123"),
//            eq("MyNetworkSSID")
//        )
//    }




}