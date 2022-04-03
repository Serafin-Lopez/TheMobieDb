package mx.com.developer.themobiedb.repository

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.di.FirebaseModule
import mx.com.developer.themobiedb.view.locations.Locations
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val context: Context,
    @FirebaseModule.LocationCollection private val locationsCollections: CollectionReference
): GenericRepository(Dispatchers.IO) {

    suspend fun saveMyLocation(location: Locations) {
        try {
            locationsCollections.document(location.id).set(location, SetOptions.merge())
                .addOnSuccessListener {
                }.addOnFailureListener {
                }.await()

        } catch (e: Exception){

        }
    }

    suspend fun getListLocations() : Resource<List<Locations>> {

        val resource : Resource<List<Locations>>

        val list = locationsCollections
            .get()
            .await()
            .toObjects(Locations::class.java)

        resource = Resource.success(list)

        return resource
    }

}