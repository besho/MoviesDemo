package com.demo.movies.utils
import android.content.Context
import java.io.InputStream

class Utils {

    companion object Utils {

        fun loadMoviesFromAssets(context: Context) : String {
            var json: String? = null
            try {
                val  inputStream: InputStream = context.assets.open("movies.json")
                json = inputStream.bufferedReader().use{it.readText()}
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ""
            }
            return json
        }

    }

}