package mx.com.developer.themobiedb.view.uploadFiles

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class File(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val imageUrl: String = ""
): Parcelable
