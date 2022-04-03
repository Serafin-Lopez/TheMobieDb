package mx.com.developer.themobiedb.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.di.FirebaseModule
import mx.com.developer.themobiedb.view.uploadFiles.File
import mx.com.developer.themobiedb.view.uploadFiles.UploadFragment
import javax.inject.Inject

class FilesRepository @Inject constructor(
    private val context: Context,
    @FirebaseModule.FilesCollection private val filesCollection: CollectionReference
): GenericRepository(Dispatchers.IO) {

    suspend fun saveImage(fileName: String,imageURI: Uri, fragment: Fragment)  {

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            fileName
        )

        sRef.putFile(imageURI)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        when(fragment) {
                            is UploadFragment -> fragment.observerStatusUpload(fileName,uri.toString())
                        }
                    }
            }
            .addOnFailureListener { exception ->
                // Hide the progress dialog if there is any error. And print the error in log.
                Log.e(
                    context.javaClass.simpleName,
                    exception.message,
                    exception
                )

                when(fragment) {
                    is UploadFragment -> fragment.observeErrorUpload(exception.message.toString())
                }
            }

            .addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * (taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

            }.addOnPausedListener { println("Upload is paused") }

    }

    suspend fun saveInfoDataImage(file: File) {
        try {
            filesCollection.document(file.id).set(file, SetOptions.merge())
                .addOnSuccessListener {
                }.addOnFailureListener {
                }.await()

        } catch (e: Exception){

        }
    }

    suspend fun getListFiles() : Resource<List<File>> {

        val resource : Resource<List<File>>

        val list = filesCollection
            .get()
            .await()
            .toObjects(File::class.java)

        resource = Resource.success(list)

        return resource
    }
}