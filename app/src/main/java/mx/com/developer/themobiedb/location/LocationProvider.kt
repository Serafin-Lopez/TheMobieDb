package mx.com.developer.themobiedb.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import mx.com.developer.themobiedb.helpers.Constants.LOCATION_REQUEST_FASTEST_INTERVAL
import mx.com.developer.themobiedb.helpers.Constants.LOCATION_REQUEST_UPDATE_INTERVAL
import javax.inject.Inject

/**
 * Class that holds the initialization for requesting location services
 */

class LocationProvider @Inject constructor(@ApplicationContext private val context: Context) {

    // Unique client during the whole lifecycle of the the app
    val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Initialize a location request
     */
    private val locationRequest: LocationRequest = LocationRequest.create()
        .setInterval(LOCATION_REQUEST_UPDATE_INTERVAL)
        .setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


    /**
     * Subscribe a location call back for updates
     */
    @SuppressLint("MissingPermission")
    fun subscribeForLocationUpdates(locationCallback: LocationCallback) {
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}