package com.demo.movies.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.demo.movies.BuildConfig
import com.demo.movies.data.local.MoviesRoomDatabase
import com.demo.movies.data.model.MoviesList
import com.demo.movies.data.remote.ApiHelper
import com.demo.movies.data.remote.ApiHelperImpl
import com.demo.movies.data.remote.ApiService
import com.demo.movies.utils.BASE_URL
import com.demo.movies.utils.MOVIES_DATABASE_NAME
import com.demo.movies.utils.Utils
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            MOVIES_DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val dao = moviesRoomDatabase.moviesDao()
                    val movies = dao.getAllMovies()
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

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper


}