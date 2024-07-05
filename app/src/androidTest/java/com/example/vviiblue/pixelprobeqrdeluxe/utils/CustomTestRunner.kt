package com.example.vviiblue.pixelprobeqrdeluxe.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        /** to test with daggerHilt */
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}