package com.example.vviiblue.pixelprobeqrdeluxe.utils

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.example.vviiblue.pixelprobeqrdeluxe.HiltTestActivity
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.ui.main.MainActivity
import org.hamcrest.Matcher

/** Function for launch a Fragment inside an Activity with support for HiltFunction*/
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_PixelProbeQRDeluxe,
    crossinline action: (Fragment) -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    // Lanzar el HiltTestActivity usando ActivityScenario
    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        // Instanciar el Fragmento que necesitamos probar dentro del Activity HiltTestActivity
        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        action(fragment)
    }
}

fun clickInChild(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> =
            ViewMatchers.withId(id)

        override fun getDescription(): String = "Child in ViewHolder."

        override fun perform(uiController: UiController?, view: View) {
            (view.findViewById(id) as View).performClick()
        }
    }
}
