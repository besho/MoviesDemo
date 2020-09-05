package com.demo.movies.repository

import com.demo.movies.data.remote.ApiHelperImpl
import com.demo.movies.data.remote.ApiService
import com.demo.movies.data.remote.RemoteRepository
import com.demo.movies.utils.BASE_URL
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private val mockResponsePhotosInfo = """
            {"photos":{"page":1,"pages":18815,"perpage":20,"total":"376292","photo":[{"id":"50302559676","owner":"159071860@N03","secret":"2eed725a6e","server":"65535","farm":66,"title":"Lincoln Victorian Prison.","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301782598","owner":"35962850@N05","secret":"648f2616f9","server":"65535","farm":66,"title":"Lompoc Murals","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302470751","owner":"35962850@N05","secret":"bf10345e4c","server":"65535","farm":66,"title":"Lompoc Murals","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301782033","owner":"35962850@N05","secret":"b6d1b8a238","server":"65535","farm":66,"title":"Lompoc Murals","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302470346","owner":"35962850@N05","secret":"851d43ebb2","server":"65535","farm":66,"title":"Lompoc Murals","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302621467","owner":"35962850@N05","secret":"b224405d60","server":"65535","farm":66,"title":"Lompoc Murals","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301776073","owner":"99383989@N06","secret":"d1b59afe0e","server":"65535","farm":66,"title":"Urban Scene","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301754418","owner":"23024164@N06","secret":"44a0897289","server":"65535","farm":66,"title":"Blocked","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302413896","owner":"131001808@N08","secret":"b1fdfa584d","server":"65535","farm":66,"title":"Die Klassenmannschaft 1949\/1950","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301721418","owner":"8913547@N06","secret":"154819fe95","server":"65535","farm":66,"title":"Trust God + Chill","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301700698","owner":"43520375@N06","secret":"3c868eb4a9","server":"65535","farm":66,"title":"BIT OF A \"STINK\" AT THIS TOILET BLOCK CONTROLLED BY TRAFFIC LIGHTS","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302529577","owner":"93390528@N04","secret":"1c8de3b45a","server":"65535","farm":66,"title":"British Columbia \/ B.C. Postal History - 27 August 1954 - BILLINGS BAY, B.C. (cds cancel \/ postmark) to Vancouver 4, B.C. (signed by famous British Columbia Boat Builder Barrie Farrell)","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302503107","owner":"95887722@N05","secret":"0bee426356","server":"65535","farm":66,"title":"Rock climbing :)","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301660268","owner":"95887722@N05","secret":"3e450970e5","server":"65535","farm":66,"title":"Rock climbing :)","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301658333","owner":"95887722@N05","secret":"d97284e33c","server":"65535","farm":66,"title":"Rock climbing :)","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50301656298","owner":"152674964@N02","secret":"cd9969d020","server":"65535","farm":66,"title":"blocks","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302262881","owner":"10249901@N04","secret":"65cb6df902","server":"65535","farm":66,"title":"Last round","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302244316","owner":"8432336@N08","secret":"144b887676","server":"65535","farm":66,"title":"Blocked","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302237461","owner":"13395150@N07","secret":"27e3b4ea88","server":"65535","farm":66,"title":"The Tower of the Juche Idea, Pyongyang","ispublic":1,"isfriend":0,"isfamily":0},{"id":"50302364027","owner":"14858012@N08","secret":"000dbe82e7","server":"65535","farm":66,"title":"Fahrenheit 451","ispublic":1,"isfriend":0,"isfamily":0}]},"stat":"ok"}
    """.trimIndent()

    private val mockResponseAllPhotos = "[$mockResponsePhotosInfo]"

    private lateinit var repository: RemoteRepository

    /**
     * Helper method to get a ApiService instance
     */
    private fun getApiService(): ApiService {
        val baseUrl = mockWebServer.url(BASE_URL)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Before
    fun createRepository(): Unit = runBlocking {
        mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setBody(mockResponseAllPhotos))
        mockWebServer.start()

        repository = RemoteRepository(ApiHelperImpl(getApiService()))
    }

    @Test
    fun fetchPhotos_returnData(): Unit = runBlocking {
         val photosResponse = repository.getPhotos("Blockers",1)

        photosResponse.body()?.photosList?.photosList?.isNotEmpty()?.let { assertTrue(it) }

        mockWebServer.shutdown()
    }

}