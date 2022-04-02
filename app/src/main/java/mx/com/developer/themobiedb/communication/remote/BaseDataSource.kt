package mx.com.developer.themobiedb.communication.remote

import android.util.Log
import mx.com.developer.themobiedb.communication.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>  ) : Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    val responseCode = response.code()
                    return Resource.success(
                        body,
                        responseCode
                    )
                }
            }


            return error(" ${response.code()} ${response.message()}" , response, response.code() )
        } catch (e: Exception) {
            return  Resource.error(e.message ?: e.toString())
        }
    }


    private fun <T> error(message: String, data: Response<T>? = null , statusResponse : Int ): Resource<T> {
        Log.e("remoteDataSource", message)
        return Resource.error(
            "Network call has failed for a following reason: $message",
            statusResponse,
            data?.body()
        )
    }

}