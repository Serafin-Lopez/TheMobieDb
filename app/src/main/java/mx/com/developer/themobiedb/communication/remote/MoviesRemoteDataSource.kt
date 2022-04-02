package mx.com.developer.themobiedb.communication.remote


import mx.com.developer.themobiedb.communication.service.MoviesService
import javax.inject.Inject

class MoviesRemoteDataSource @Inject constructor(
    private val moviesService: MoviesService
): BaseDataSource() {

    suspend fun getPopularMovies() = getResult {
        moviesService.getPopularMovies()
    }

}