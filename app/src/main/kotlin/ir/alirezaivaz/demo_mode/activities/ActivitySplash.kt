package ir.alirezaivaz.demo_mode.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ActivitySplash : AppCompatActivity() {
    private lateinit var splashScreen: SplashScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener {
            startActivity(
                Intent(
                    this@ActivitySplash,
                    ActivityMain::class.java
                )
            )
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}