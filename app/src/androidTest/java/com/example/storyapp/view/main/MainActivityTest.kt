package com.example.storyapp.view.main

import android.support.test.filters.LargeTest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapp.R
import com.example.storyapp.utils.EspressoIdlingResource
import com.example.storyapp.view.liststory.ListStoryActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest{

    @get: Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    private val email = "testes@gmail.com"
    private val password = "12345678abcd"

    @Test
    fun loadLoginView_and_LoginUser_Success(){
        onView(withId(R.id.app_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.login_email)).perform(click())
        onView(withId(R.id.login_email)).perform(typeText(email), closeSoftKeyboard())

        onView(withId(R.id.login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.login_password)).perform(click())
        onView(withId(R.id.login_password)).perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())
    }

    @Test
    fun logOut_Success(){
        ActivityScenario.launch(ListStoryActivity::class.java)
        onView(withId(R.id.logout)).check(matches(isDisplayed()))
        onView(withId(R.id.logout)).perform(click())
    }


}