package com.demo.movies

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.movies.data.model.Movie
import com.demo.movies.ui.components.movieslist.MoviesListActivity
import com.google.gson.Gson
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.List.of

private const val PACKAGE_NAME = "com.demo.movies"

@RunWith(AndroidJUnit4::class)
class LaunchMovieDetailsBehaviorTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MoviesListActivity>
            = ActivityScenarioRule(MoviesListActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun launchDetailsActivity() {

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withId(R.id.moviesRV)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()));

        intended(allOf(
            hasComponent(hasShortClassName(".ui.components.moviedetail.MovieDetailActivity")),
            toPackage(PACKAGE_NAME),
            hasExtra("movie", Gson().toJson(Movie(title = "The Strange Ones",genres = ArrayList(listOf("Drama")),
                cast = ArrayList(listOf("Alex Pettyfer","James Freedson-Jackson","Emily Althaus","Gene Jones","Owen Campbell","Tobias Campbell")),rating = 5,year = 2018)))))
    }

}