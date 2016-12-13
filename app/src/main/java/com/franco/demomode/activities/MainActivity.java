package com.franco.demomode.activities;

import android.app.AlertDialog;
import android.os.Bundle;
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

import static com.franco.demomode.Utils.isDumpPermissionGranted;
import static com.franco.demomode.Utils.isWriteSecureSettingsPermissionGranted;

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

        getFragmentManager().beginTransaction().replace(R.id.prefs_frame, new SettingsFragment()).commit();

        if (getIntent() != null) {
            if (getIntent().getAction() != null) {
                if (getIntent().getAction().equals(Utils.MISSING_PERMISSION)) {
                    final Snackbar snackbar = Snackbar.make(rootLayout,
                            R.string.permissions_need_to_be_granted, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private static final String KEY_DUMP = "dump_permission";
        private static final String KET_WRITE_SECURE_SETTINGS = "write_secure_settings";

        private Preference dump;
        private Preference writeSecureSettings;

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
                    .setMessage(preference.getKey().equals(KEY_DUMP)
                            ? R.string.dump_permission_msg : R.string.write_secure_settings_permission_msg)
                    .show();

            return false;
        }

        @Override
        public void onResume() {
            super.onResume();

            dump.setSummary(isDumpPermissionGranted() ? R.string.granted : R.string.not_granted);
            writeSecureSettings.setSummary(isWriteSecureSettingsPermissionGranted()
                    ? R.string.granted : R.string.not_granted);
        }
    }
}
