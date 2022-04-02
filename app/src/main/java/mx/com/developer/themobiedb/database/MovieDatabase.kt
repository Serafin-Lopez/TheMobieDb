package mx.com.developer.themobiedb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mx.com.developer.themobiedb.database.dao.PopularMoviesDao
import mx.com.developer.themobiedb.view.popularMovies.PopularMoviesModel

@Database(entities = [PopularMoviesModel.Result::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

        abstract fun popularMoviesDao(): PopularMoviesDao

        companion object {

                @Volatile
                private var INSTANCE: MovieDatabase? = null

                var nameDatabase = "movies_database"

                fun getDatabase(context: Context): MovieDatabase {
                        if (INSTANCE == null) {
                                synchronized(MovieDatabase::class.java) {
                                        if (INSTANCE == null) {
                                                INSTANCE = Room.databaseBuilder(
                                                        context.applicationContext,
                                                        MovieDatabase::class.java, nameDatabase
                                                )
                                                        .build()
                                        }
                                }
                        }
                        return INSTANCE!!
                }
        }
}