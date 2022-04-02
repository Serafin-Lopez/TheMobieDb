package mx.com.developer.themobiedb.view.splash

import android.annotation.SuppressLint
import android.os.Bundle
import mx.com.developer.themobiedb.BaseActivity
import mx.com.developer.themobiedb.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        checkStatusGPS()
    }
}