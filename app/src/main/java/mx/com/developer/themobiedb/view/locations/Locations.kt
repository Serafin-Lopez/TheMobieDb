package mx.com.developer.themobiedb.view.locations

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Locations(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val lat: String = "",
    val lng: String = ""
): Parcelable

data class LocationMarker(
    val latLng: LatLng,
    val title: String
)
