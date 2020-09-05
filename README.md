# MoviesDemo
**A Decade of Movies App**
It loads movies from assets (movies.json) and store them on Room Database. Retrofit is used to fetch movie's photos using Flickers API.

[[![kotlin](https://img.shields.io/badge/Kotlin-1.4.xxx-brightgreen.svg)](https://kotlinlang.org/) [![MVVM ](https://img.shields.io/badge/Clean--Code-MVVM-brightgreen.svg)](https://github.com/googlesamples/android-architecture)    [![coroutines](https://img.shields.io/badge/coroutines-asynchronous-red.svg)](https://kotlinlang.org/docs/reference/coroutines-overview.html) [![Mockk](https://img.shields.io/badge/Mockk-testing-yellow.svg)](https://mockk.io/)      [![Junit4](https://img.shields.io/badge/Junit4-testing-yellowgreen.svg)](https://junit.org/junit5/)   
[![Espresso](https://img.shields.io/badge/Espresso-testing-lightgrey.svg)](https://developer.android.com/training/testing/espresso/) 
[![Dagger-Hilt](https://img.shields.io/badge/Hilt_Dagger-2.xx-orange.svg)](https://github.com/google/dagger/tree/master/java/dagger/hilt)  [![Kotlin-Android-Extensions ](https://img.shields.io/badge/Kotlin--Android--Extensions-plugin-red.svg)](https://kotlinlang.org/docs/tutorials/android-plugin.html) 
[![Lifecycle](https://img.shields.io/badge/Lifecycle-2.xx-orange.svg)](https://developer.android.com/training/testing/espresso/) 
[![Room](https://img.shields.io/badge/Room-2.xx-orange.svg)](https://developer.android.com/topic/libraries/architecture/room/) 
[![ViewModel](https://img.shields.io/badge/ViewModel-2.xx-orange.svg)](https://developer.android.com/topic/libraries/architecture/viewmodel/) 



#### The app has following packages:
1. **data**: It contains all the data accessing and manipulating components. It has three packages local, Model and remote.
2. **di**: It contains the files required by Dagger-Hilt
3. **ui**: View classes along with their corresponding ViewModel.
4. **utils**: Utility classes.


## Hints:
1. Flicker_BASE_URL and FLICKER_API_KEY are constants and located on Constants.kt inside utils package.
2. App supports Unit Tests and UI Tests.
