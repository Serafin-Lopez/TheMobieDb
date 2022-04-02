package mx.com.developer.themobiedb.view.popularMovies



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.repository.MoviesRepository
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
   var moviesRepository: MoviesRepository
) : ViewModel() {

    var movies = MutableLiveData <Resource<PopularMoviesModel>>()

    var localData =  MutableLiveData<List<PopularMoviesModel.Result>>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPopularMovies() {
      movies.postValue(Resource.loading())
      viewModelScope.launch(Dispatchers.IO) {
        val moviesList = moviesRepository.getPopularMovies()
         movies.postValue(moviesList)
      }
    }

    fun getMovies() {

        viewModelScope.launch(Dispatchers.IO) {
            val listMovies = moviesRepository.getLocalPopularMovies()
            localData.postValue(listMovies)
        }
    }

}
