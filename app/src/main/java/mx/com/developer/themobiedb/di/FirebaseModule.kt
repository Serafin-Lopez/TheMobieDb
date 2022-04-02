package mx.com.developer.themobiedb.di


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.com.developer.themobiedb.helpers.Constants.FILES_COLLECTION
import mx.com.developer.themobiedb.helpers.Constants.LOCATION_COLLECTION
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @LocationCollection
    @Provides
    @Singleton
    fun provideLocationCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection(LOCATION_COLLECTION)
    }

    @FilesCollection
    @Provides
    @Singleton
    fun provideFilesCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection(FILES_COLLECTION)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocationCollection
    annotation class FilesCollection
}