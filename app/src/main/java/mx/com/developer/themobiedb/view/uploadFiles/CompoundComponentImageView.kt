package mx.com.developer.themobiedb.view.uploadFiles

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mx.com.developer.themobiedb.R


class CompoundComponentImageView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    companion object {

        const val STATUS_PENDING = 0
        const val STATUS_CAPTURED = 1
        const val STATUS_UPLOADING = 2
        const val STATUS_UPLOADED = 3
        const val STATUS_ERROR = 4
    }

    var status: Int = STATUS_PENDING
        set(value) {
            field = value
            updateView()
        }

    var src: String? = null

    var title: String? = null

    var errorText: String? = null

    private var onImageCapturedClickListener = OnClickListener {
        errorText = null
        startTakePhoto?.invoke()
    }

    private var onTryUploadClickListener = OnClickListener {
        errorText = null
        status = STATUS_UPLOADING
        startUpload?.invoke(this, src)
    }


    private var onDeleteClickListener = OnClickListener {
        errorText = null
        startDeletion?.invoke(this)
    }

    var startUpload: ((imageView: CompoundComponentImageView, src: String?) -> Unit)? = null

    var startDeletion: ((imageView: CompoundComponentImageView) -> Unit)? = null

    var startTakePhoto: (() -> Unit)? = null

    private val imageButton: ImageButton
    private val statusMainImageView: ImageView
    private val eventText: TextView
    private val marginView: View
    private val titleTextView: TextView
    private val mainEventView: LinearLayout
    private val eventImageView: ImageView
    private val eraseImageButton: ImageButton

    init {
        inflate(context, R.layout.item_compound_view_image_upload, this).apply {
            imageButton = findViewById(R.id.mainImageButton)
            statusMainImageView = findViewById(R.id.statusImageButton)
            eventText = findViewById(R.id.eventTextView)
            marginView = findViewById(R.id.marginView)
            titleTextView = findViewById(R.id.titleTextView)
            mainEventView = findViewById(R.id.mainEventView)
            eventImageView = findViewById(R.id.eventImageView)
            eraseImageButton = findViewById(R.id.eraseImageButton)
            eraseImageButton.setOnClickListener (onDeleteClickListener)
        }

        updateView()
    }

    private fun updateView() {
        title?.let {
            titleTextView.text = it
            titleTextView.visibility = View.VISIBLE
        } ?: run {
            titleTextView.visibility = GONE
        }
        when (status) {

            STATUS_PENDING -> {
                imageButton.visibility = View.VISIBLE
                imageButton.setOnClickListener(onImageCapturedClickListener)
                Glide.with(this)
                    .load("")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageButton)
                statusMainImageView.visibility = View.INVISIBLE
                mainEventView.visibility = View.VISIBLE
                eventText.text = resources.getString(R.string.chose_your_file)
                eventImageView.setImageResource(R.drawable.ic_upload_file)
                eventText.setTextColor(resources.getColor(android.R.color.darker_gray, null))
                marginView.background = resources.getDrawable(R.drawable.shape_image_empty_background, null)
            }

            STATUS_CAPTURED -> {
                imageButton.visibility = View.VISIBLE
                Glide.with(this)
                    .load(src)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageButton)
                statusMainImageView.visibility = View.GONE
                imageButton.setOnClickListener(onTryUploadClickListener)
                eventText.text = resources.getString(R.string.upload_your_file)
                eventImageView.setImageResource(R.drawable.ic_upload_file)
                mainEventView.visibility = View.VISIBLE
                eventText.setTextColor(resources.getColor(android.R.color.darker_gray, null))
                marginView.background = resources.getDrawable(R.drawable.shape_image_animations_background, null)
            }

            STATUS_UPLOADED -> {
                imageButton.visibility = View.VISIBLE
                Glide.with(this)
                    .load(src)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageButton)
                statusMainImageView.visibility = View.VISIBLE
                statusMainImageView.setImageResource(R.drawable.ic_sucess_upload)
                imageButton.setOnClickListener(null)
                mainEventView.visibility = View.GONE
                marginView.background = resources.getDrawable(R.drawable.shape_image_successful_background, null)
            }

            STATUS_UPLOADING -> {
                imageButton.visibility = View.VISIBLE
                Glide.with(this)
                    .load(src)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageButton)
                statusMainImageView.visibility = View.GONE
                imageButton.setOnClickListener(onTryUploadClickListener)
                eventText.text = resources.getString(R.string.uploading_file)
                eventText.setTextColor(Color.parseColor("#3498db"))
                mainEventView.visibility = View.VISIBLE
                marginView.background = resources.getDrawable(R.drawable.shape_image_animations_background, null)
            }

            STATUS_ERROR -> {
                imageButton.visibility = View.VISIBLE
                Glide.with(this)
                    .load(src)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageButton)
                statusMainImageView.visibility = View.VISIBLE
                statusMainImageView.setImageResource(R.drawable.ic_upload_error)
                imageButton.setOnClickListener(if (src?.isNotBlank() == true) onTryUploadClickListener else onImageCapturedClickListener)
                mainEventView.visibility = View.VISIBLE
                eventText.text = errorText
                eventText.setTextColor(Color.parseColor("#e67e22"))
                eventImageView.setImageResource(R.drawable.ic_upload_error)
                marginView.background = resources.getDrawable(R.drawable.shape_image_error_background, null)
            }
        }
    }

}