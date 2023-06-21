package com.franco.demomode.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.franco.demomode.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.time.ExperimentalTime

@ExperimentalTime
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    private lateinit var dumpPermissionPref: Preference
    private lateinit var writeSecureSettingsPref: Preference

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)

        dumpPermissionPref = findPreference(KEY_DUMP)!!
        writeSecureSettingsPref = findPreference(KET_WRITE_SECURE_SETTINGS)!!

        dumpPermissionPref.onPreferenceClickListener = this
        writeSecureSettingsPref.onPreferenceClickListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isDumpPermissionUpdates(requireContext())
            .observe(viewLifecycleOwner, ::renderDump)
        viewModel.isWriteSecureSettingsPermissionUpdates(requireContext())
            .observe(viewLifecycleOwner, ::renderWriteSecureSettings)
    }

    private fun renderDump(isGranted: Boolean) {
        dumpPermissionPref.summary = when (isGranted) {
            true -> getString(R.string.granted)
            false -> getString(R.string.not_granted)
        }
    }

    private fun renderWriteSecureSettings(isGranted: Boolean) {
        writeSecureSettingsPref.summary = when (isGranted) {
            true -> getString(R.string.granted)
            false -> getString(R.string.not_granted)
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.permission_request_title)
            .setIcon(
                when (preference.key) {
                    KEY_DUMP -> R.drawable.bug
                    else -> R.drawable.ic_settings_black_24dp
                }
            )
            .setMessage(
                when (preference.key) {
                    KEY_DUMP -> R.string.dump_permission_msg
                    KET_WRITE_SECURE_SETTINGS -> R.string.write_secure_settings_permission_msg
                    else -> throw IllegalArgumentException("well, this shouldn't ever happen")
                }
            )
            .setPositiveButton(R.string.ok, null)
            .show()
        return false
    }

    companion object {
        private const val KEY_DUMP = "dump_permission"
        private const val KET_WRITE_SECURE_SETTINGS = "write_secure_settings"
    }
}