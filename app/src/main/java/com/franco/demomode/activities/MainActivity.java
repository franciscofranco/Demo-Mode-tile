package com.franco.demomode.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Process;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import com.franco.demomode.R;
import com.franco.demomode.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.root_layout)
    protected View rootLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setActionBar(toolbar);

        if (getIntent() != null) {
            if (getIntent().getAction() != null) {
                if (getIntent().getAction().equals(Utils.MISSING_PERMISSION)) {
                    final Snackbar snackbar = Snackbar.make(rootLayout,
                            R.string.permissions_need_to_be_granted, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.ok, view -> snackbar.dismiss());
                    snackbar.show();
                }
            }
        }

        if (!Utils.isDemoModeAllowed()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.demo_mode_allowed_title)
                    .setMessage(R.string.demo_mode_allowed_message)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private static final String KEY_DUMP = "dump_permission";
        private static final String KET_WRITE_SECURE_SETTINGS = "write_secure_settings";

        private Preference dump;
        private Preference writeSecureSettings;

        private Thread permissionsPollThread;

        private boolean isDumpPermissionGranted;
        private boolean isWriteSecureSettingsPermissionGranted;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            dump = findPreference(KEY_DUMP);
            writeSecureSettings = findPreference(KET_WRITE_SECURE_SETTINGS);

            dump.setOnPreferenceClickListener(this);
            writeSecureSettings.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.permission_request_title)
                    .setMessage(preference.getKey().equals(KEY_DUMP) ?
                            R.string.dump_permission_msg :
                            R.string.write_secure_settings_permission_msg)
                    .show();

            return false;
        }

        @Override
        public void onStart() {
            super.onStart();

            isDumpPermissionGranted = Utils.isDumpPermissionGranted();
            isWriteSecureSettingsPermissionGranted = Utils.isWriteSecureSettingsPermissionGranted();

            dump.setSummary(isDumpPermissionGranted ? R.string.granted : R.string.not_granted);
            writeSecureSettings.setSummary(isWriteSecureSettingsPermissionGranted
                    ? R.string.granted : R.string.not_granted);

            // there isn't a listener for when these permissions states change, so we have to poll it
            // Runs in a BG thread so it's ok
            permissionsPollThread = new Thread(new Runnable() {
                long lastNow = System.currentTimeMillis();

                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                    try {
                        while (permissionsPollThread != null && permissionsPollThread.isAlive()) {
                            if ((lastNow + 1000) < System.currentTimeMillis()) {
                                lastNow = System.currentTimeMillis();

                                if (isDumpPermissionGranted != Utils.isDumpPermissionGranted()) {
                                    isDumpPermissionGranted = Utils.isDumpPermissionGranted();

                                    getActivity().runOnUiThread(() ->
                                            dump.setSummary(isDumpPermissionGranted ?
                                                    R.string.granted : R.string.not_granted));
                                }

                                if (isWriteSecureSettingsPermissionGranted != Utils.isWriteSecureSettingsPermissionGranted()) {
                                    isWriteSecureSettingsPermissionGranted = Utils.isWriteSecureSettingsPermissionGranted();

                                    getActivity().runOnUiThread(() ->
                                            writeSecureSettings.setSummary(isWriteSecureSettingsPermissionGranted
                                            ? R.string.granted : R.string.not_granted));
                                }
                            }
                        }
                    } catch (Exception ignored) {
                        // it's ok to catch the exception here and not treat it properly.
                        // not doing anything important and the thread object might be null even after the null check
                        // so fail gracefully and in peace
                    }
                }
            });

            permissionsPollThread.start();
        }

        @Override
        public void onStop() {
            super.onStop();

            if (permissionsPollThread != null && !permissionsPollThread.isInterrupted()) {
                permissionsPollThread.interrupt();
                permissionsPollThread = null;
            }
        }
    }
}
