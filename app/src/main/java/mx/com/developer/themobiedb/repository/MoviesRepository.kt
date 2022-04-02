package mx.com.developer.themobiedb.repository


import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.communication.remote.MoviesRemoteDataSource
import mx.com.developer.themobiedb.database.dao.PopularMoviesDao
import mx.com.developer.themobiedb.helpers.Constants.BASE_URL_FOR_IMAGE
import mx.com.developer.themobiedb.helpers.ResourceUtils.deleteAllImages
import mx.com.developer.themobiedb.helpers.ResourceUtils.toBase64
import mx.com.developer.themobiedb.view.popularMovies.PopularMoviesModel
import java.io.BufferedInputStream
import java.net.URL
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val context: Context,
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: PopularMoviesDao
): GenericRepository(Dispatchers.IO) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPopularMovies() : Resource<PopularMoviesModel> {
        val result = remoteDataSource.getPopularMovies()
        val responseCode = result.statusResponse
        val stringData = result.data?.string()

        val resource: Resource<PopularMoviesModel> = if (responseCode == 200) {

            deleteLocalMovies()

            val gson = Gson()

            val response = gson.fromJson(stringData, PopularMoviesModel::class.java)

            Log.d("", "Values movies 200 = $response")

            response.results.forEach {


                val url = URL(BASE_URL_FOR_IMAGE+it.posterPath)
                val bis = BufferedInputStream(url.openConnection().getInputStream())
                it.posterPath =  bis.readBytes().toBase64()
                localDataSource.insert(it)

               /* val image = getBitmapFromURL(BASE_URL_FOR_IMAGE + it.posterPath)
                it.posterPath = image?.let { it1 -> saveImage(it1) }.toString()
                localDataSource.insert(it)*/
            }



            Resource.success(response)

        } else {
            Log.d("", "Error response Code = $responseCode")
            Resource.error("", responseCode)
        }

        return resource
    }

    private suspend fun deleteLocalMovies() {
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllMovies()
            deleteAllImages()
        }
    }

    suspend fun getLocalPopularMovies() : Resource<List<PopularMoviesModel.Result>> {

        val resource: Resource<List<PopularMoviesModel.Result>>

        val data = localDataSource.getPopularMovies()

        data.forEach {
            val url = it.posterPath
            val imageBytes = Base64.decode(url, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            it.bitmap = decodedImage
        }

        resource = Resource.success(data)

        return resource
    }

}