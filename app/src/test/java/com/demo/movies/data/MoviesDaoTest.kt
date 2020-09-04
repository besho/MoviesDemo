package com.demo.movies.data.local

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.movies.data.model.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(manifest=Config.NONE)
class MoviesDaoTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var database: MoviesRoomDatabase

    private val movie1 = Movie(title = "Blockers",genres = ArrayList(),cast = ArrayList(),rating = 3,year = 2017)
    private val movie2 = Movie(title = "Sherlock Gnomes",genres = ArrayList(),cast = ArrayList(),rating = 5,year = 2018)
    private val movie3 = Movie(title = "Sweet Country",genres = ArrayList(),cast = ArrayList(),rating = 4,year = 2018)
    private val movies = listOf(movie1, movie2,movie3)

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(context, MoviesRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    private fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }

    @Test
    fun insertMovie_savesData(): Unit = runBlocking {
        database.moviesDao().insert(movie1)
        val savedMovie = database.moviesDao().getAllMovies().blockingObserve()
        savedMovie?.isEmpty()?.let { assertFalse(it) }
    }

    @Test
    fun insertMovies_savesData(): Unit = runBlocking {
        database.moviesDao().insert(movie1)
        database.moviesDao().insert(movie2)
        val savedMovies = database.moviesDao().getAllMovies().blockingObserve()
        shadowOf(Looper.getMainLooper()).idle()
        savedMovies?.isNotEmpty()?.let {
            assertTrue(it && savedMovies.size ==2)
        }
    }

    @Test
    fun insertAllMovies_retrievesData(): Unit= runBlocking {
        for (movie in movies) {
            database.moviesDao().insert(movie)
        }
        val savedMovies = database.moviesDao().getAllMovies().blockingObserve()
        shadowOf(Looper.getMainLooper()).idle()
        savedMovies?.isNotEmpty()?.let {
            assertTrue(it && savedMovies == movies)
        }
    }

    /*Filter movies by specific keyword*/
    @Test
    fun getFilteredMovies_retrievesData(): Unit = runBlocking {
        for (movie in movies) {
            database.moviesDao().insert(movie)
        }
        val savedMovies = database.moviesDao().getFilteredMovies("2017").blockingObserve()
        shadowOf(Looper.getMainLooper()).idle()
        savedMovies?.isNotEmpty()?.let {
            assertTrue(it && savedMovies[0].title == "Blockers")
        }
    }

    /*Filter movies by fake keyword return empty result*/
    @Test
    fun getFilteredMovies_noData(): Unit = runBlocking {
        for (movie in movies) {
            database.moviesDao().insert(movie)
        }
        val savedMovies = database.moviesDao().getFilteredMovies("Hat").blockingObserve()
        shadowOf(Looper.getMainLooper()).idle()
        savedMovies?.isEmpty()?.let { assertTrue(it) }
    }

    /*Filter movies by keyword will return result sorted by Year and Rate*/
    @Test
    fun getFilteredMoviesSorted_retrievesData(): Unit = runBlocking {
        for (movie in movies) {
            database.moviesDao().insert(movie)
        }
        val savedMovies = database.moviesDao().getFilteredMovies("20").blockingObserve()
        shadowOf(Looper.getMainLooper()).idle()
        savedMovies?.isNotEmpty()?.let {
            assertTrue(it && savedMovies[0].title == "Sherlock Gnomes")
        }
    }
}