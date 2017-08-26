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
    private static final String CLOCK_SECONDS = "clock_seconds";

    private static boolean isTime24 = false;

    public static void enableDemoMode() {
        if (!isDemoModeAllowed()) {
            Settings.Global.putInt(App.CONTEXT.getContentResolver(), DEMO_MODE_ALLOWED, 1);
        }

        Settings.Global.putInt(App.CONTEXT.getContentResolver(), DEMO_MODE_ON, 1);

        try {
            if (Settings.System.getInt(App.CONTEXT.getContentResolver(), Settings.System.TIME_12_24) == 24) {
                Settings.System.putInt(App.CONTEXT.getContentResolver(), Settings.System.TIME_12_24, 12);
                Settings.Secure.putInt(App.CONTEXT.getContentResolver(), CLOCK_SECONDS, 1);
                Settings.Secure.putInt(App.CONTEXT.getContentResolver(), CLOCK_SECONDS, 0);
                isTime24 = true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        Intent a = new Intent("com.android.systemui.demo");
        a.putExtra("command", "clock");
        a.putExtra("hhmm", "0800");
        App.CONTEXT.sendBroadcast(a);

        Intent b = new Intent("com.android.systemui.demo");
        b.putExtra("command", "battery");
        b.putExtra("level", "100");
        b.putExtra("plugged", "false");
        App.CONTEXT.sendBroadcast(b);

        Intent c = new Intent("com.android.systemui.demo");
        c.putExtra("command", "network");
        c.putExtra("mobile", "show");
        c.putExtra("datatype", "none");
        c.putExtra("level", "4");
        c.putExtra("fully", "true");
        c.putExtra("wifi", "show");
        c.putExtra("fully", "true");
        c.putExtra("level", "4");
        c.putExtra("command", "network");
        c.putExtra("airplane", "hide");
        c.putExtra("nosim", "hide");
        App.CONTEXT.sendBroadcast(c);

        Intent d = new Intent("com.android.systemui.demo");
        d.putExtra("command", "notifications");
        d.putExtra("visible", "false");
        App.CONTEXT.sendBroadcast(d);

        Intent e = new Intent("com.android.systemui.demo");
        e.putExtra("command", "status");
        e.putExtra("bluetooth", "hide");
        e.putExtra("volume", "hide");
        e.putExtra("mute", "hide");
        App.CONTEXT.sendBroadcast(e);
    }

    public static void disableDemoMode() {
        Settings.Global.putInt(App.CONTEXT.getContentResolver(), Utils.DEMO_MODE_ON, 0);

        Intent enableDemoMode = new Intent("com.android.systemui.demo");
        enableDemoMode.putExtra("command", "exit");
        App.CONTEXT.sendBroadcast(enableDemoMode);

        if (isTime24) {
            Settings.System.putInt(App.CONTEXT.getContentResolver(), Settings.System.TIME_12_24, 24);
            Settings.Secure.putInt(App.CONTEXT.getContentResolver(), CLOCK_SECONDS, 1);
            Settings.Secure.putInt(App.CONTEXT.getContentResolver(), CLOCK_SECONDS, 0);
            isTime24 = false;
        }
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
