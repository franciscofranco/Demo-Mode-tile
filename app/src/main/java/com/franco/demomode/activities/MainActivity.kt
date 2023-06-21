package com.franco.demomode.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.franco.demomode.R
import com.franco.demomode.Utils
import com.franco.demomode.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.initGitHubFab()

        val action = intent?.action ?: ""
        if (action == Utils.MISSING_PERMISSION) {
            Snackbar.make(
                binding.root,
                R.string.permissions_need_to_be_granted,
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction(R.string.ok) { dismiss() }
                show()
            }
        }

        lifecycleScope.launch {
            val isDemoModeAllowed = Utils().isDemoModeAllowed(this@MainActivity)

            if (!isDemoModeAllowed) {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(R.string.demo_mode_allowed_title)
                    .setMessage(R.string.demo_mode_allowed_message)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        startActivity(
                            Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                        )
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        }
    }


    private fun ExtendedFloatingActionButton.initGitHubFab() {
        smoothShrink()
        this.setOnClickListener {
            extend()
            smoothShrink()
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.github))
                .build()
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(params)
                .setShowTitle(true)
                .build()
                .launchUrl(this@MainActivity, Uri.parse("https://github.com/AlirezaIvaz/DemoModeTile"))
        }
        setOnLongClickListener {
            extend()
            smoothShrink()
            true
        }
    }

    private fun ExtendedFloatingActionButton.smoothShrink() {
        Handler(Looper.getMainLooper()).postDelayed({
            this.shrink()
        }, 2000)
    }

}