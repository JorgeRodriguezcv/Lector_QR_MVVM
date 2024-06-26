package com.example.vviiblue.pixelprobeqrdeluxe.ui.scan

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.ui.home.HomeFragment
import com.example.vviiblue.pixelprobeqrdeluxe.ui.scan.ScanFragment
import com.example.vviiblue.pixelprobeqrdeluxe.ui.scan.ScanViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class ScanFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // Necessary to simulate grant permissions
    @get:Rule
    val mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Before
    fun setUp() {
        hiltRule.inject()
        // launch the fragment
        launchFragmentInHiltContainer<ScanFragment> { ScanFragment ->
        }
    }

    @Test
    fun testScanFragment() {
        // Verify that the camera view is displayed
        onView(withId(R.id.rvCameraScan)).check(matches(isDisplayed()))

        // Make click in button to exit of camera scan
        onView(withId(R.id.fabExitScanCam)).perform(click())

    }
}