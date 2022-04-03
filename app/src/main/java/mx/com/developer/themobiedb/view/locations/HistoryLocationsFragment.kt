package mx.com.developer.themobiedb.view.locations

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history_locations.*
import kotlinx.android.synthetic.main.toolbar_main.*
import mx.com.developer.themobiedb.BaseFragment
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.helpers.loadText


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryLocationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HistoryLocationsFragment : BaseFragment(), OnMapReadyCallback {

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

        }
    }

}