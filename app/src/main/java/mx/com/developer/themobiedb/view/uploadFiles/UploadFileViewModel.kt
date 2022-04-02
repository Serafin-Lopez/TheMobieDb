package mx.com.developer.themobiedb.view.uploadFiles



import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.repository.FilesRepository
import javax.inject.Inject

@HiltViewModel
class UploadFileViewModel @Inject constructor(
   var filesRepository: FilesRepository
) : ViewModel() {

    fun uploadFile(fileName: String, uri: Uri, fragment: Fragment) {
      viewModelScope.launch(Dispatchers.IO) {
        filesRepository.saveImage(fileName, uri,fragment)
      }
    }

    fun saveFileInfo(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            filesRepository.saveInfoDataImage(file)
        }
    }


}
