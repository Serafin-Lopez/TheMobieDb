@file:Suppress("DEPRECATION")

package mx.com.developer.themobiedb.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Utility class to manage all the functions that requires access to resources.
 */

object ResourceUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun ByteArray.toBase64(): String =
        String(Base64.getEncoder().encode(this))

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: java.lang.Exception) {
            Log.d("see", e.toString())
            null
        }
    }

    fun saveImage(image: Bitmap) : String {

        var rute = ""
        val imageFileName = System.currentTimeMillis().toString() + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + "/TheMovieDb")

        Log.e("Sees","${storageDir}")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }

        if (success) {
            val imageFile = File(storageDir, imageFileName)
            rute = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 25, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return rute
    }

    fun deleteAllImages() {
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + "/TheMovieDb")

        if (storageDir.isDirectory) {
            val children = storageDir.list()
            for (i in children.indices) {
                Log.e("DELETE","true")
                File(storageDir, children[i]).delete()
            }
        } else {
            Log.e("DELETE","false")
        }
    }

    fun getFileExtension(context:  Context, uri: Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(uri!!))
    }
}