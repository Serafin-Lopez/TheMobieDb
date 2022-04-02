package mx.com.developer.themobiedb.helpers

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.helpers.Constants.IMAGE_URL_DEFAULT


/**
 * Extension functions to help to reduce the boilerplate when setting up visibility to views.
 * Feel free to add extensions functions as you need.
 */

/**
 * Set visibility of a view to visible.
 * Usage: myView.show()
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * Set visibility of a view to gone.
 * Usage: myView.hide()
 */
fun View.hide() {
    this.visibility = View.GONE
}

fun ImageView.loadBitMap(bitmap: Bitmap?) {
    Glide.with(context)
        .load(bitmap?: IMAGE_URL_DEFAULT)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}

fun ImageView.loadUrlImage(url: String?) {
    Glide.with(context)
        .load(url?: IMAGE_URL_DEFAULT)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}



fun TextView.loadText(text: String) {
    this.text = text
}


