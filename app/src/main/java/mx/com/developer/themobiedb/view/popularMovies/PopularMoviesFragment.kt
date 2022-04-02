package mx.com.developer.themobiedb.view.popularMovies

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.android.synthetic.main.toolbar_main.*
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.helpers.loadText


/**
 * A simple [Fragment] subclass.
 * Use the [PopularMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PopularMoviesFragment : Fragment(), MoviesListCallback {

    private val viewModel: PopularMoviesViewModel by viewModels()
    lateinit var adapter: PopularMoviesAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            adapter = PopularMoviesAdapter().apply { listCallback = this@PopularMoviesFragment }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObserverData()
    }

    private fun setupRecyclerView() {
        textViewTitleToolbar.loadText(getString(R.string.popular_movies))
        recyclerViewMovies?.layoutManager = GridLayoutManager(context,2)
        recyclerViewMovies?.adapter = adapter
    }

    private fun setupObserverData() {

        viewModel.loadPopularMovies()

        viewModel.movies.observe(viewLifecycleOwner, Observer { result ->
            when(result.status) {

                Resource.Status.LOADING -> {
                    Log.e("movies","${Resource.Status.LOADING}")
                }

                Resource.Status.SUCCESS -> {
                    Log.e("movies","${Resource.Status.SUCCESS}")
                    getPopularMovies()
                }

                Resource.Status.ERROR -> {
                    Log.e("movies","${Resource.Status.ERROR}")
                    getPopularMovies()
                }
            }
        })
    }

    override fun onMovieSelected(movie: PopularMoviesModel.Result) {
        
    }

    private fun getPopularMovies() {
        viewModel.localData.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                adapter.setData(it)
            }
        })
    }
}