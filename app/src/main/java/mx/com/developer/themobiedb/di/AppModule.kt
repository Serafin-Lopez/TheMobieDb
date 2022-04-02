package mx.com.developer.themobiedb.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.com.developer.themobiedb.communication.GlobalCommunication.Companion.BASE_URL
import mx.com.developer.themobiedb.communication.remote.MoviesRemoteDataSource
import mx.com.developer.themobiedb.communication.service.MoviesService
import mx.com.developer.themobiedb.database.MovieDatabase
import mx.com.developer.themobiedb.database.dao.PopularMoviesDao
import mx.com.developer.themobiedb.repository.MoviesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ====== RETROFIT =======
    @Provides
    fun provideRetrofit(@ApplicationContext appContext: Context, gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideInterceptor(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder().
        retryOnConnectionFailure(false).
        readTimeout(180, TimeUnit.SECONDS).
        connectTimeout(180, TimeUnit.SECONDS).
        addInterceptor(loggingInterceptor)
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val baseUrl = chain.request().url
            val url = baseUrl.newBuilder().addQueryParameter("api_key", "2f72eea10d2663d258c92fe5cf505a55").build()
            request.url(url)
            chain.proceed(request.build())
        }
        return httpClient.build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // ====== Gson ======
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    // ====== API =======

    @Provides
    fun provideMoviesService(retrofit: Retrofit): MoviesService = retrofit.create(
        MoviesService::class.java)

    // ====== REMOTE DATA SOURCE ======

    @Provides
    fun provideMoviesRemoteDataSource(moviesService: MoviesService) = MoviesRemoteDataSource(moviesService)


    // ====== DATABASE =======
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = MovieDatabase.getDatabase(appContext)

    //dao
    @Provides
    fun provideUserDao(db: MovieDatabase) = db.popularMoviesDao()



    // ====== REPOSITORY ======

    @Provides
    fun provideMoviesRepository(@ApplicationContext appContext: Context,remoteDataSource: MoviesRemoteDataSource,popularMoviesDao: PopularMoviesDao) = MoviesRepository(appContext,remoteDataSource,popularMoviesDao)




}