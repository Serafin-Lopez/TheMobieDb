package mx.com.developer.themobiedb.view.listFiles

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_files_uploaded.*
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.android.synthetic.main.toolbar_main.*
import mx.com.developer.themobiedb.BaseFragment
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.helpers.hide
import mx.com.developer.themobiedb.helpers.loadText
import mx.com.developer.themobiedb.helpers.show
import mx.com.developer.themobiedb.view.uploadFiles.File
import mx.com.developer.themobiedb.view.uploadFiles.UploadFileViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ListFilesUploadedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ListFilesUploadedFragment : BaseFragment(), FilesListCallback {

    val viewModel: UploadFileViewModel by viewModels()
    lateinit var adapter: ListFilesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            adapter = ListFilesAdapter().apply { listCallback = this@ListFilesUploadedFragment }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_files_uploaded, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupList()
    }

    private fun setupList() {
        viewModel.getListFiles()

        viewModel.data.observe(viewLifecycleOwner, Observer { result ->
            when(result.status) {

                Resource.Status.LOADING -> {
                    Log.e("files","${Resource.Status.LOADING}")
                }

                Resource.Status.SUCCESS -> {
                    Log.e("files","${Resource.Status.SUCCESS}")
                    result.data?.let { adapter.setData(it) }
                }

                Resource.Status.ERROR -> {
                    Log.e("files","${Resource.Status.ERROR}")
                }
            }
        })
    }

    override fun onFileSelected(file: File) {

    }

    private fun setupRecyclerView() {
        textViewTitleToolbar.loadText(getString(R.string.files_uploaded))
        recyclerViewFiles?.layoutManager = GridLayoutManager(context,2)
        recyclerViewFiles?.adapter = adapter
    }
}