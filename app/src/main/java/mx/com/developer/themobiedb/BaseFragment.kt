package mx.com.developer.themobiedb

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.location.LocationCallback
import mx.com.developer.themobiedb.location.LocationProvider
import javax.inject.Inject


/**
 * Base class where all the fragments are going to inherit from.
 * Primary goal for this class is to put variables and functions
 * that are shared across the fragments in order NO to have repeated code.
 */

abstract class BaseFragment : Fragment() {


    var callbackActivity: OnSetupActivityListener? = null
        private set


    var rootView: View? = null

    @Inject lateinit var  locationProvider : LocationProvider

    /**
     * Var to store the current callback from the child fragment that has been subscribed for location updates.
     * It will depend on the child fragment to decide to subscribe for location updates or not.
     */

    private var locationCallback: LocationCallback? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackActivity = context.takeIf { it is OnSetupActivityListener }?.let { it as OnSetupActivityListener }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
    }
    override fun onDetach() {
        super.onDetach()
        callbackActivity = null
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { stopLocationUpdates() }
    }


    /**
     * Start for location updates. This function must be called from on Resume()
     */
    fun startLocationUpdates(locationCallback: LocationCallback) {
        this.locationCallback = locationCallback
        locationProvider.subscribeForLocationUpdates(locationCallback)
    }

    /**
     * Remove location Updates
     */
    private fun stopLocationUpdates() {
        locationProvider.client.removeLocationUpdates(locationCallback)
        locationCallback = null
    }

    fun navigateToFragment(view: View, navigationID: Int) {
        Navigation.findNavController(view).navigate(navigationID)
    }

    fun popBackStack(view: View) {
        Navigation.findNavController(view).popBackStack()
    }


    fun showToast(message: String?): Toast {
        return Toast.makeText(context, message, Toast.LENGTH_SHORT)
    }

}

/**
 * Interface in charge to serve as a bridge to communicate
 * the fragments with the parent activity.
 */
interface OnSetupActivityListener {
    fun requestForLocationUpdates()
}
