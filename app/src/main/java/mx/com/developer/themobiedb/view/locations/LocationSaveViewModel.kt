package mx.com.developer.themobiedb.view.locations



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.repository.LocationsRepository
import javax.inject.Inject

@HiltViewModel
class LocationSaveViewModel @Inject constructor(
   var locationsRepository: LocationsRepository
) : ViewModel() {

    val data = MutableLiveData<Resource<List<Locations>>>()


    fun saveLocation(location: Locations) {
        viewModelScope.launch(Dispatchers.IO) {
            locationsRepository.saveMyLocation(location)
        }
    }

    fun getLocations() {
        data.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(locationsRepository.getListLocations())
        }
    }


}
