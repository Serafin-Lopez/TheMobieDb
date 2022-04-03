package mx.com.developer.themobiedb.di


import android.content.Context
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.com.developer.themobiedb.repository.FilesRepository
import mx.com.developer.themobiedb.repository.LocationsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationsModule {

    @Provides
    @Singleton
    fun provideLocationsRepository(  @ApplicationContext context: Context,
        @FirebaseModule.LocationCollection locationsReference: CollectionReference
    ) = LocationsRepository(context,locationsReference)

}