package mx.com.developer.themobiedb.database.dao

import androidx.room.Dao
import androidx.room.Query
import mx.com.developer.themobiedb.view.popularMovies.PopularMoviesModel

@Dao
interface PopularMoviesDao: BaseDao<PopularMoviesModel.Result> {

    @Query("SELECT * FROM popular_movies_table")
    fun getPopularMovies(): List<PopularMoviesModel.Result>

    @Query("DELETE FROM popular_movies_table")
    fun deleteAllMovies()
}