package mx.com.developer.themobiedb.view.uploadFiles

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.android.synthetic.main.toolbar_main.*
import mx.com.developer.themobiedb.BaseFragment
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.helpers.hide
import mx.com.developer.themobiedb.helpers.loadText
import mx.com.developer.themobiedb.helpers.show
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UploadFragment : BaseFragment() {

    private val viewModel: UploadFileViewModel by viewModels()

    lateinit var uriImage : Uri

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    uriImage = fileUri

                    val filename: String =
                        fileUri.path?.substring(fileUri.path?.lastIndexOf("/")?.plus(1) ?: 0).toString()

                    val image: String = fileUri.toString()
                    uploadFile.title = filename
                    uploadFile.src = image
                    uploadFile?.status = CompoundComponentImageView.STATUS_CAPTURED
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choseImage()
    }

    private fun choseImage() {
        textViewSeeImage.hide()
        textViewTitleToolbar.loadText(getString(R.string.upload_file))
        startTakePhoto()
        deleteImage()
        startUploadImage()

        textViewSeeImage.setOnClickListener {
            navigateToFragment(it,R.id.navigation_list_files_uploaded)
        }
    }


    private fun startTakePhoto() {

        val startTakePhoto: () -> Unit = {  ->

            ImagePicker.with(requireActivity())
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        uploadFile.startTakePhoto = startTakePhoto
    }

    private fun deleteImage() {

        val delete =  { _: CompoundComponentImageView ->

            if(uploadFile.src != null){
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.attention))
                    .setMessage(getString(R.string.confirm_message))
                    .setPositiveButton(R.string.agree) { _, _ ->
                        uploadFile?.status = CompoundComponentImageView.STATUS_PENDING
                    }.setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }.setCancelable(false)
                    .show()
            }
            else uploadFile?.status = CompoundComponentImageView.STATUS_PENDING

            Unit
        }


        uploadFile.startDeletion = delete

    }

    private fun startUploadImage() {

        val startUpload = { v: CompoundComponentImageView, s: String? ->

            viewModel.uploadFile(uploadFile.title?:"One", uriImage,this)

            Unit
        }

        uploadFile.startUpload = startUpload

    }

    fun observerStatusUpload(title: String, imageUrl: String) {
        uploadFile.status =  CompoundComponentImageView.STATUS_UPLOADED

        val file = File(title = title, imageUrl = imageUrl)
        viewModel.saveFileInfo(file)

        textViewSeeImage.show()
    }

    fun observeErrorUpload(message:String) {
        uploadFile.status =  CompoundComponentImageView.STATUS_ERROR
        uploadFile.errorText = message
    }



}