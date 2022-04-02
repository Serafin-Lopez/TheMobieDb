package mx.com.developer.themobiedb.view.popularMovies

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.helpers.loadText


class PopularMoviesAdapter : RecyclerView.Adapter<PopularMoviesAdapter.MoviesListViewHolder>() {
    private var data: List<PopularMoviesModel.Result> = ArrayList()
    var listCallback: MoviesListCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        return MoviesListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movies_list, parent, false),
            listCallback
        )
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MoviesListViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.bind(data[position])

    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<PopularMoviesModel.Result>) {
        this.data = data
        notifyDataSetChanged()
    }

    class MoviesListViewHolder(itemView: View, var moviesListCallback: MoviesListCallback?) : RecyclerView.ViewHolder(itemView) {

        var name:TextView = itemView.findViewById(R.id.textViewMovie)
        var imageView: ImageView = itemView.findViewById(R.id.imageViewMovie)

        var movies: PopularMoviesModel.Result?= null

        fun bind(item: PopularMoviesModel.Result) = with(itemView) {


            name.loadText(item.title)
            val url = item.posterPath
            val imageBytes = Base64.decode(url, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageView.setImageBitmap(decodedImage)

          /*  val bitmap = BitmapFactory.decodeFile(url)

            imageView.setImageBitmap(bitmap)*/

            movies = item

            setOnClickListener {
                selectItem()
            }

        }

        /**
         * this function was created to select the item in recyclerView
         */

        fun selectItem() {
            movies?.let {
                moviesListCallback?.onMovieSelected(it)
            }
        }

    }

}

interface MoviesListCallback {
    fun onMovieSelected(movie: PopularMoviesModel.Result)
}