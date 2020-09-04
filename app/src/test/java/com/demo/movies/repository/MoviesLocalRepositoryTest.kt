package com.demo.movies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.movies.data.local.MoviesDao
import com.demo.movies.data.local.MoviesLocalRepository
import com.demo.movies.data.local.MoviesRoomDatabase
import com.demo.movies.data.model.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class MoviesLocalRepositoryTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var database: MoviesRoomDatabase

    private val movie1 = Movie(title = "Blockers",genres = ArrayList(),cast = ArrayList(),rating = 3,year = 2017)
    private val movie2 = Movie(title = "Sherlock Gnomes",genres = ArrayList(),cast = ArrayList(),rating = 4,year = 2018)

    private lateinit var mockMoviesDao: MoviesDao

    private lateinit var repository: MoviesLocalRepository

    @Before
    fun createRepository(): Unit = runBlocking {
        mockMoviesDao = mock(MoviesDao::class.java)
        database = Room.inMemoryDatabaseBuilder(context, MoviesRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = MoviesLocalRepository(database)
        mockMoviesDao.insert(movie1)
        mockMoviesDao.insert(movie2)
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
    fun getAllMovies_retrievesData(): Unit = runBlocking {
        val actualMovies = repository.getAllMovies().blockingObserve()
        actualMovies?.isNotEmpty()?.let { assertTrue(it && actualMovies.size == 2) }
    }

    @Test
    fun getMoviesByKeyword_retrievesData(): Unit = runBlocking {
        val actualMovies = repository.getFilteredMovies("Blockers").blockingObserve()
        actualMovies?.isNotEmpty()?.let { assertTrue(it && actualMovies.size == 1) }
    }

}