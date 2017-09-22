package com.franco.demomode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.franco.demomode.application.App;

public class Utils {
    private static final String DEMO_MODE_ALLOWED = "sysui_demo_allowed";
    private static final String DEMO_MODE_ON = "sysui_tuner_demo_on";
    public static final String MISSING_PERMISSION = "missing_permission";

    public static void enableDemoMode() {
        if (!isDemoModeAllowed()) {
            Settings.Global.putInt(App.CONTEXT.getContentResolver(), DEMO_MODE_ALLOWED, 1);
        }

        Settings.Global.putInt(App.CONTEXT.getContentResolver(), DEMO_MODE_ON, 1);

        // clock with the latest release string
        Intent clock = new Intent("com.android.systemui.demo");
        clock.putExtra("command", "clock");
        clock.putExtra("hhmm", "0800");
        App.CONTEXT.sendBroadcast(clock);

        // battery icon needs to be perfect
        Intent battery = new Intent("com.android.systemui.demo");
        battery.putExtra("command", "battery");
        battery.putExtra("level", "100");
        battery.putExtra("plugged", "false");
        App.CONTEXT.sendBroadcast(battery);

        // signal icon
        Intent data = new Intent("com.android.systemui.demo");
        data.putExtra("command", "network");
        data.putExtra("mobile", "show");
        data.putExtra("datatype", "hide");
        data.putExtra("level", "4");
        App.CONTEXT.sendBroadcast(data);

        // mock sim carrier connection
        Intent signal = new Intent("com.android.systemui.demo");
        signal.putExtra("command", "network");
        signal.putExtra("fully", "true");
        App.CONTEXT.sendBroadcast(signal);

        // WiFi icon
        Intent wifi = new Intent("com.android.systemui.demo");
        wifi.putExtra("command", "network");
        wifi.putExtra("wifi", "show");
        wifi.putExtra("level", "4");
        App.CONTEXT.sendBroadcast(wifi);

        // rip icons
        Intent miscNetwork = new Intent("com.android.systemui.demo");
        miscNetwork.putExtra("command", "network");
        miscNetwork.putExtra("airplane", "hide");
        miscNetwork.putExtra("nosim", "hide");
        miscNetwork.putExtra("sims", 1);
        App.CONTEXT.sendBroadcast(miscNetwork);


        // if there's one thing I hate is cluttered statusbar with notifs
        Intent notifs = new Intent("com.android.systemui.demo");
        notifs.putExtra("command", "notifications");
        notifs.putExtra("visible", "false");
        App.CONTEXT.sendBroadcast(notifs);

        // goodbye more icons!
        Intent miscIcons = new Intent("com.android.systemui.demo");
        miscIcons.putExtra("command", "status");
        miscIcons.putExtra("bluetooth", "hide");
        miscIcons.putExtra("volume", "hide");
        miscIcons.putExtra("mute", "hide");
        App.CONTEXT.sendBroadcast(miscIcons);
    }

    public static void disableDemoMode() {
        Settings.Global.putInt(App.CONTEXT.getContentResolver(), Utils.DEMO_MODE_ON, 0);

        Intent enableDemoMode = new Intent("com.android.systemui.demo");
        enableDemoMode.putExtra("command", "exit");
        App.CONTEXT.sendBroadcast(enableDemoMode);
    }

    public static boolean isDemoModeAllowed() {
        try {
            return Settings.Global.getInt(App.CONTEXT.getContentResolver(), DEMO_MODE_ALLOWED) == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDemoModeOn() {
        return Settings.Global.getInt(App.CONTEXT.getContentResolver(),
                DEMO_MODE_ON, 0) != 0;
    }

    public static boolean isDumpPermissionGranted() {
        return App.CONTEXT.getPackageManager().checkPermission(Manifest.permission.DUMP, App.CONTEXT.getPackageName())
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWriteSecureSettingsPermissionGranted() {
        return App.CONTEXT.getPackageManager()
                .checkPermission(Manifest.permission.WRITE_SECURE_SETTINGS, App.CONTEXT.getPackageName())
                == PackageManager.PERMISSION_GRANTED;
    }
}
