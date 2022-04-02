package mx.com.developer.themobiedb.helpers

import android.app.Activity
import mx.com.developer.themobiedb.MainActivity
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.view.permission.PermissionsActivity

/**
 * Use it to add functions that launches activities through the entire app.
 * It is better to have one place that is in charge of doing this.
 * We are going to add support for navigate to fragments as well. Stay tuned!
 */
object AppNavigation {

    /**
     * If you need to navigate to MainActivity use this!
     * Note that the overridePendingTransition is added and
     * the activity who is launching MainActivity is finished as well.
     */
    fun goToMainActivity(activity: Activity, shouldSlide: Boolean = false) {
        with(activity) {
            launchActivity<MainActivity>()
            if (shouldSlide) overridePendingTransition(
                R.anim.push_left_in,
                R.anim.push_left_out
            ) else overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out)
            finish()
        }
    }

    /**
     * If you need to navigate to PermissionsActivity use this!
     * Note that the overridePendingTransition is added and
     * the activity who is launching MainActivity is finished as well.
     */
    fun goToPermissions(activity: Activity, shouldSlide: Boolean = false) {
        with(activity) {
            launchActivity<PermissionsActivity>()
            if (shouldSlide) overridePendingTransition(
                R.anim.push_left_in,
                R.anim.push_left_out
            ) else overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out)
        }
    }

}