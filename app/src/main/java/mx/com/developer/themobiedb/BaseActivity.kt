package mx.com.developer.themobiedb

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import mx.com.developer.themobiedb.helpers.MultiplePermissionListener
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import mx.com.developer.themobiedb.helpers.AppNavigation
import mx.com.developer.themobiedb.helpers.Constants.LOCATION_REQUEST_FASTEST_INTERVAL
import mx.com.developer.themobiedb.helpers.Constants.LOCATION_REQUEST_UPDATE_INTERVAL
import mx.com.developer.themobiedb.helpers.Constants.REQUEST_CODE_CHECK_SETTINGS
import mx.com.developer.themobiedb.helpers.ErrorListener


/**
 * Base class from where all the activities are going to extend from.
 * If there are functions that need to be shared across the activities
 * add them here.
 */
abstract class BaseActivity : AppCompatActivity() {


    private lateinit var allPermissionsListener: MultiplePermissionsListener
    private lateinit var errorListener: PermissionRequestErrorListener


    fun multiplePermissionListener() {

        val feedbackViewMultiplePermissionListener: MultiplePermissionsListener =
            MultiplePermissionListener(this)

        allPermissionsListener = CompositeMultiplePermissionsListener(
            feedbackViewMultiplePermissionListener
        )

        errorListener = ErrorListener()

        requestForPermissions(allPermissionsListener,errorListener)

    }

    fun showPermissionGranted(permission: String) {
        val feedbackView: String = getFeedbackViewForPermission(permission)
        Log.e("PermissionGranted","$feedbackView ")
    }

    fun showPermissionDenied(permission: String, isPermanentlyDenied: Boolean) {
        val feedbackView: String = getFeedbackViewForPermission(permission)
        Log.e("PermissionDenied","$feedbackView and is $isPermanentlyDenied")
    }

    fun allPermissionIsGranted() {
        Log.e("PermissionDenied","all granted")
        AppNavigation.goToMainActivity(this,true)
    }

    fun permissionDenied() {
        Log.e("PermissionDenied","denied")
        AppNavigation.goToPermissions(this,true)
    }

    private fun getFeedbackViewForPermission(name: String): String {
        val feedbackView: String = when (name) {
            Manifest.permission.ACCESS_FINE_LOCATION -> "ACCESS_FINE_LOCATION"
            Manifest.permission.ACCESS_COARSE_LOCATION -> "ACCESS_COARSE_LOCATION"
            else -> throw RuntimeException("No feedback view for this permission")
        }

        return feedbackView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                multiplePermissionListener()
            } else {
                finish()
            }
        }
    }

    /**
     * Function to request for Location services
     */
    fun requestForLocationServices() {
        val locationRequest = LocationRequest.create()
            .setInterval(LOCATION_REQUEST_UPDATE_INTERVAL)
            .setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnFailureListener(this) { ex ->
                if (ex is ResolvableApiException) {
                    // Location settings are NOT satisfied, but this can be fixed by showing the user a dialog.
                    try {
                        val resolvable = ex as ResolvableApiException
                        resolvable.startResolutionForResult(this, REQUEST_CODE_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
    }

    /**
     * Function to request for location and phone permissions
     * @param permissionsListener that is going to handle the options for the permissions that are granted.
     */

    private fun requestForPermissions(permissionsListener: MultiplePermissionsListener, errorListener: PermissionRequestErrorListener) {
        Dexter.withContext(applicationContext)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(permissionsListener)
            .withErrorListener(errorListener)
            .check()
    }


    /**
     * Function to check GPS Status
     * if GPS is enabled request multiplePermissionListener else requestForLocationServices.
     */

    fun checkStatusGPS()  {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requestForLocationServices()
        } else {
            multiplePermissionListener()
        }
    }

}