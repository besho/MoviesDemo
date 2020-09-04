package com.demo.movies.ui.components.movieslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.movies.data.local.MoviesLocalRepository


class MoviesViewModel @ViewModelInject constructor(moviesLocalRepository: MoviesLocalRepository): ViewModel() {


    val getAllMoviesList = moviesLocalRepository.getAllMovies()
    val  moviesRepository  =  moviesLocalRepository

    var noInterNetConnection: MutableLiveData<Boolean> = MutableLiveData()
    var showError: MutableLiveData<Error> = MutableLiveData()


    /* fun getCharts(timeSpan:String) {
         mRemoteRepository.requestChartsApi(callback,timeSpan)
     }

     private val callback = object : BaseCallback {

         override fun onSuccess(data: ChartResponseModel) {
             chartResponseModel.postValue(data)
         }

         override fun onFail(error: Error?) {
             if (error?.code == -1) {
                 noInterNetConnection.postValue(true)
             } else {
                 showError.postValue(error)
             }

         }
     }*/
}
