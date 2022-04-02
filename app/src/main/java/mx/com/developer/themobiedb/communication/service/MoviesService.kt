package mx.com.developer.themobiedb.communication.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface MoviesService {

    @GET("popular")
    suspend fun getPopularMovies(): Response<ResponseBody?>
}