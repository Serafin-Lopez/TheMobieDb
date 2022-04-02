package mx.com.developer.themobiedb.helpers

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.helpers.Constants.IMAGE_URL_DEFAULT


/**
 * Extension functions to help to reduce the boilerplate when setting up visibility to views.
 * Feel free to add extensions functions as you need.
 */

fun ImageView.loadPngOriginal(url: String?) {
    Glide.with(context)
        .load(url?: IMAGE_URL_DEFAULT)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}

fun TextView.loadText(text: String) {
    this.text = text
}

fun ImageView.loadCircleImage(url: String?) {
    Glide.with(context)
        .load(url)
        .circleCrop()
        .into(this)
}


