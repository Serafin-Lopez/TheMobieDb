package mx.com.developer.themobiedb.helpers

import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import mx.com.developer.themobiedb.BaseActivity

class MultiplePermissionListener(private val activity: BaseActivity) : MultiplePermissionsListener {

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        for (response in report.grantedPermissionResponses) {
            activity.showPermissionGranted(response.permissionName)
        }
        for (response in report.deniedPermissionResponses) {
            activity.showPermissionDenied(response.permissionName, response.isPermanentlyDenied)
        }

        if (report.areAllPermissionsGranted()) {
            activity.allPermissionIsGranted()
        } else {
            activity.permissionDenied()
        }

    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: List<PermissionRequest>,
        token: PermissionToken
    ) {
        activity.permissionDenied()
    }

}