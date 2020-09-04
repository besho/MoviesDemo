package com.demo.movies.ui

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.movies.data.local.MoviesLocalRepository
import com.demo.movies.data.local.MoviesRoomDatabase
import com.demo.movies.data.model.Movie
import com.demo.movies.ui.components.movieslist.MoviesViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class MoviesListViewModelTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var repository: MoviesLocalRepository
    private lateinit var database: MoviesRoomDatabase


    @Before
    fun createRepository(): Unit = runBlocking {
        database = Room.inMemoryDatabaseBuilder(context, MoviesRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = MoviesLocalRepository(database)
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    /* Get Movies Categorized By Year and each Category should have top 5 rated movies*/
    @Test
    fun getMoviesCategorizedByTopFive_retrievesData() {

        val viewModel = MoviesViewModel(repository)

        val movies = listOf(
              Movie(title = "Sherlock Gnomes",genres = ArrayList(),cast = ArrayList(),rating = 5,year = 2018)
            , Movie(title = "Blockers",genres = ArrayList(),cast = ArrayList(),rating = 4,year = 2018)
            , Movie(title = "Game Night",genres = ArrayList(),cast = ArrayList(),rating = 4,year = 2018)
            , Movie(title = "Gemini",genres = ArrayList(),cast = ArrayList(),rating = 3,year = 2018)
            , Movie(title = "Beirut",genres = ArrayList(),cast = ArrayList(),rating = 2,year = 2018)
            , Movie(title = "Kings",genres = ArrayList(),cast = ArrayList(),rating = 1,year = 2018)
            , Movie(title = "Puzzle",genres = ArrayList(),cast = ArrayList(),rating = 1,year = 2018)
            , Movie(title = "Sherlock Gnomes",genres = ArrayList(),cast = ArrayList(),rating = 5,year = 2016)
            , Movie(title = "Blockers",genres = ArrayList(),cast = ArrayList(),rating = 4,year = 2016)
            , Movie(title = "Game Night",genres = ArrayList(),cast = ArrayList(),rating = 4,year = 2016)
            , Movie(title = "Gemini",genres = ArrayList(),cast = ArrayList(),rating = 3,year = 2016)
            , Movie(title = "Beirut",genres = ArrayList(),cast = ArrayList(),rating = 2,year = 2016)
            , Movie(title = "Kings",genres = ArrayList(),cast = ArrayList(),rating = 1,year = 2016)
            , Movie(title = "Puzzle",genres = ArrayList(),cast = ArrayList(),rating = 1,year = 2016)
        )

        val moviesCategorized = viewModel.getMoviesCategorizedByYear(movies)

        assertTrue(moviesCategorized[0].title == "2018") //Category Title
        assertTrue(moviesCategorized[5].title == "Beirut")

        assertTrue(moviesCategorized[6].title == "2016") //Category Title
        assertTrue(moviesCategorized[11].title == "Beirut")

        assertTrue(moviesCategorized.size ==12)
    }


}