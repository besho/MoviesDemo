package com.demo.movies

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.movies.ui.components.movieslist.MoviesListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchMoviesBehaviorTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MoviesListActivity>
            = ActivityScenarioRule(MoviesListActivity::class.java)

    @Test
    fun searchOnMovies() {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        onView(withId(R.id.app_bar_search)).perform(typeText("Blockers"));
    }

}