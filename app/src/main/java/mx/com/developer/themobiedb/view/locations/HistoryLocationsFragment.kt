package mx.com.developer.themobiedb.view.locations

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history_locations.*
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.android.synthetic.main.toolbar_main.*
import mx.com.developer.themobiedb.BaseFragment
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.communication.Resource
import mx.com.developer.themobiedb.helpers.hide
import mx.com.developer.themobiedb.helpers.loadText
import mx.com.developer.themobiedb.helpers.show


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryLocationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HistoryLocationsFragment : BaseFragment(), OnMapReadyCallback {

    private val viewModel: LocationSaveViewModel by viewModels()

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapViewLocations(savedInstanceState)
    }

    private fun setUpMapViewLocations(savedInstanceState: Bundle?) {
        textViewTitleToolbar.loadText(getString(R.string.history_locations))
        with(mapViewLocations) {
            onCreate(savedInstanceState)
            onResume()
            getMapAsync(this@HistoryLocationsFragment)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0
        locationProvider.client.lastLocation.addOnSuccessListener {
            it?.let {
                updateLocationOnMap(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationOnMap(location: Location) {
        val locationUser = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraPosition.Builder().target(locationUser).zoom(16F).build()
        googleMap?.let {
            it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            it.isMyLocationEnabled = true

            val locationButton= (mapViewLocations.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(
                Integer.parseInt(
                    "2"
                )
            )
            val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)

            observePinsLocation()

        }
    }

    private fun observePinsLocation() {

        viewModel.getLocations()

        viewModel.data.observe(viewLifecycleOwner, Observer { result ->

            when(result.status) {
                Resource.Status.LOADING -> {
                    Log.e("locations","${Resource.Status.LOADING}")
                }

                Resource.Status.SUCCESS -> {
                    Log.e("locations","${Resource.Status.SUCCESS}")
                    result.data?.let { displayLocationsMarkersOnMap(it) }
                }

                Resource.Status.ERROR -> {
                    Log.e("locations","${Resource.Status.ERROR}")
                }
            }
        })
    }

    private fun displayLocationsMarkersOnMap(locations: List<Locations>) {
        googleMap?.let {
            getLocationPin(locations).forEach { locationMarker ->
                val marker = MarkerOptions().position(locationMarker.latLng).title(locationMarker.title)
                val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)
                val cameraPosition = CameraPosition.Builder().target(locationMarker.latLng).zoom(16F).build()
                it.addMarker(marker)?.setIcon(icon)
                it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    private fun getLocationPin(locations: List<Locations>) = mutableListOf<LocationMarker>().apply {
        locations.forEach {
            add(LocationMarker(LatLng(it.lat.toDouble(), it.lng.toDouble()),it.date))
        }
    }

}