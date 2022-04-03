package mx.com.developer.themobiedb.view.locations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Locations(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val lat: String = "",
    val lng: String = ""
): Parcelable
