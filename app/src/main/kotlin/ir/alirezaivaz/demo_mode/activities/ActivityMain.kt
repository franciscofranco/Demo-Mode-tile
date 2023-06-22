package ir.alirezaivaz.demo_mode.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ir.alirezaivaz.demo_mode.R
import ir.alirezaivaz.demo_mode.Utils
import ir.alirezaivaz.demo_mode.databinding.ActivityMainBinding
import ir.alirezaivaz.demo_mode.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ActivityMain : AppCompatActivity() {
    private val activityMain = this@ActivityMain
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.initGitHubFab()

        viewModel.isDumpPermissionUpdates(activityMain)
            .observe(activityMain, ::renderDump)
        viewModel.isWriteSecureSettingsPermissionUpdates(activityMain)
            .observe(activityMain, ::renderWriteSecureSettings)

        binding.dumpGrant.setOnClickListener {
            showGrantPermissionDialog(
                ir.alirezaivaz.tablericons.R.drawable.ic_components,
                R.string.permission_dump,
                Manifest.permission.DUMP
            )
        }
        binding.writeGrant.setOnClickListener {
            showGrantPermissionDialog(
                ir.alirezaivaz.tablericons.R.drawable.ic_settings,
                R.string.permission_write_secure_settings,
                Manifest.permission.WRITE_SECURE_SETTINGS
            )
        }

//        grantPermissions()

        val action = intent?.action ?: ""
        if (action == Utils.MISSING_PERMISSION) {
            Snackbar.make(
                binding.root,
                R.string.permissions_need_to_be_granted,
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction(R.string.action_ok) { dismiss() }
                show()
            }
        }

        lifecycleScope.launch {
            val isDemoModeAllowed = Utils().isDemoModeAllowed(activityMain)

            if (!isDemoModeAllowed) {
                MaterialAlertDialogBuilder(activityMain).apply {
                    setIcon(ir.alirezaivaz.tablericons.R.drawable.ic_progress_alert)
                    setTitle(R.string.warning_demo_mode_allowed_title)
                    setMessage(R.string.warning_demo_mode_allowed_message)
                    setPositiveButton(R.string.action_open_developer) { _, _ ->
                        startActivity(
                            Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                        )
                    }
                    setNegativeButton(R.string.action_cancel, null)
                    show()
                }
            }
        }
    }

    private fun renderDump(isGranted: Boolean) {
        if (isGranted) {
            with(binding) {
                with(dumpStatus) {
                    setImageResource(ir.alirezaivaz.tablericons.R.drawable.ic_circle_check)
                    imageTintList = ContextCompat.getColorStateList(activityMain, R.color.green)
                }
                dumpGrant.isVisible = false
                dumpDescription.isVisible = false
                dumpDivider.isVisible = false
            }
        } else {
            with(binding) {
                with(dumpStatus) {
                    setImageResource(ir.alirezaivaz.tablericons.R.drawable.ic_progress_alert)
                    imageTintList = ContextCompat.getColorStateList(activityMain, R.color.orange)
                }
                dumpDivider.isVisible = true
                dumpDescription.isVisible = true
                dumpGrant.isVisible = true
            }
        }
    }

    private fun renderWriteSecureSettings(isGranted: Boolean) {
        if (isGranted) {
            with(binding) {
                with(writeStatus) {
                    setImageResource(ir.alirezaivaz.tablericons.R.drawable.ic_circle_check)
                    imageTintList = ContextCompat.getColorStateList(activityMain, R.color.green)
                }
                writeGrant.isVisible = false
                writeDescription.isVisible = false
                writeDivider.isVisible = false
            }
        } else {
            with(binding) {
                with(writeStatus) {
                    setImageResource(ir.alirezaivaz.tablericons.R.drawable.ic_progress_alert)
                    imageTintList = ContextCompat.getColorStateList(activityMain, R.color.orange)
                }
                writeDivider.isVisible = true
                writeDescription.isVisible = true
                writeGrant.isVisible = true
            }
        }
    }

    private fun showGrantPermissionDialog(
        @DrawableRes icon: Int,
        @StringRes name: Int,
        permission: String
    ) {
        MaterialAlertDialogBuilder(activityMain).apply {
            setIcon(icon)
            setTitle(name)
            setMessage(
                String.format(
                    getString(R.string.permission_request_description),
                    getString(name),
                    packageName,
                    permission
                )
            )
            setPositiveButton(R.string.action_ok, null)
            show()
        }
    }

    private fun grantPermissions() {
        try {
            val su = Runtime.getRuntime().exec("su")
            val permissions = arrayOf(
                Manifest.permission.DUMP,
                Manifest.permission.WRITE_SECURE_SETTINGS
            )
            permissions.forEach {
                val command = "pm grant $packageName $it\n"
                Log.e("Test", command)
                su.outputStream.write(command.toByteArray(charset("UTF-8")))
                su.outputStream.flush()
            }
            su.outputStream.write("exit\n".toByteArray(charset("UTF-8")))
            su.outputStream.flush()
//            su.waitFor()
            if (ContextCompat.checkSelfPermission(
                    activityMain,
                    Manifest.permission.DUMP
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(activityMain, "dump settings granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(activityMain, "dump settings denied", Toast.LENGTH_SHORT).show()
            }
//            if (isWriteSecureSettingsPermissionGranted()) {
//                Toast.makeText(mainActivity, "Write settings granted", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(mainActivity, "Write settings denied", Toast.LENGTH_SHORT).show()
//            }
        } catch (e: IOException) {
            Snackbar.make(binding.root, "Failed to grant permissions!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }


    private fun ExtendedFloatingActionButton.initGitHubFab() {
        smoothShrink()
        this.setOnClickListener {
            extend()
            smoothShrink()
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(activityMain, R.color.github))
                .build()
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(params)
                .setShowTitle(true)
                .build()
                .launchUrl(
                    activityMain,
                    Uri.parse("https://github.com/AlirezaIvaz/DemoModeTile")
                )
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