package mx.com.developer.themobiedb.di


import android.content.Context
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.com.developer.themobiedb.repository.FilesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FilesModule {

    @Provides
    @Singleton
    fun provideFilesRepository(  @ApplicationContext context: Context,
        @FirebaseModule.FilesCollection filesCollection: CollectionReference
    ) = FilesRepository(context,filesCollection)

}