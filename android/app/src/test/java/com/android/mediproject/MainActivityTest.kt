package com.android.mediproject

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.mediproject.feature.home.HomeFragment
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun init() {
        activityRule.scenario.onActivity {
            it.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment()).commitNow()
        }
    }
}