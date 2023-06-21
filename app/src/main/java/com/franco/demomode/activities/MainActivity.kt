package com.franco.demomode.activities

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.franco.demomode.R
import com.franco.demomode.Utils
import com.franco.demomode.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val action = intent?.action ?: ""
        if (action == Utils.MISSING_PERMISSION) {
            Snackbar.make(binding.root, R.string.permissions_need_to_be_granted,
                    Snackbar.LENGTH_INDEFINITE).apply {
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
                        .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                        .show()
            }
        }
    }

}