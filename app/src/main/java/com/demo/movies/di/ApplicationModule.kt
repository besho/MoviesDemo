package com.demo.movies.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.demo.movies.data.local.MoviesLocalRepository
import com.demo.movies.data.local.MoviesRoomDatabase
import com.demo.movies.data.model.MoviesList
import com.demo.movies.utils.Utils
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import java.io.InputStream
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object  ApplicationModule {

    lateinit var moviesRoomDatabase: MoviesRoomDatabase

    private val executor = Executors.newSingleThreadExecutor()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): MoviesRoomDatabase {
        moviesRoomDatabase = Room.databaseBuilder(
            appContext,
            MoviesRoomDatabase::class.java,
            "movies_database.db"
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val dao = moviesRoomDatabase.moviesDao()
                    val movies = dao.getAlphabetizedMovies()
                    if (movies.value ==null)
                    {
                        val moviesList = Gson().fromJson(JSONObject(Utils.loadMoviesFromAssets(appContext)).toString(), MoviesList::class.java).movies
                        executor.execute {
                            if (moviesList != null) {
                                for (movie in moviesList)
                                    dao.insert(movie)
                            }
                        }
                    }
                }
            })
            .build()

        return moviesRoomDatabase
    }



}