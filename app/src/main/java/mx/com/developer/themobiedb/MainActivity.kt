package mx.com.developer.themobiedb

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import mx.com.developer.themobiedb.helpers.ResourceUtils
import mx.com.developer.themobiedb.view.locations.LocationSaveViewModel
import mx.com.developer.themobiedb.view.locations.Locations

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener, OnSetupActivityListener {

    private val viewModel: LocationSaveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        requestForLocationServices()
        startLocationUpdates(getLocationCallback())
    }

    private fun setupNavigation() {
        NavigationUI.setupWithNavController(
            bottomNavigation,
            Navigation.findNavController(this, R.id.my_nav_host_fragment)
        )
        Navigation.findNavController(this, R.id.my_nav_host_fragment)
            .addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }

    override fun requestForLocationUpdates() {
        requestForLocationServices()
    }

    private fun getLocationCallback() = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {

            val location = Locations(date = ResourceUtils.getToday(), lat = "${locationResult?.lastLocation?.latitude}" , lng = "${locationResult?.lastLocation?.latitude}}")
            viewModel.saveLocation(location)

            showToast("my location ${locationResult?.lastLocation?.latitude} ${locationResult?.lastLocation?.latitude}").show()
        }

        @SuppressLint("MissingPermission")
        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            super.onLocationAvailability(locationAvailability)
            locationAvailability?.let {
                if (it.isLocationAvailable) {
                    Log.e("Location","Services are available!")
                    locationProvider.client.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                        }
                    }
                } else {
                    Log.e("Location","Services not available!")
                    requestForLocationUpdates()
                }
            }
        }
    }
}