package com.franco.demomode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;

public class Utils {
    private static final String DEMO_MODE_ALLOWED = "sysui_demo_allowed";
    private static final String DEMO_MODE_ON = "sysui_tuner_demo_on";
    public static final String MISSING_PERMISSION = "missing_permission";

    public static void enableDemoMode(Context context) {
        try {
            if (Settings.Global.getInt(context.getContentResolver(), DEMO_MODE_ALLOWED) == 0) {
                Settings.Global.putInt(context.getContentResolver(), DEMO_MODE_ALLOWED, 1);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        Settings.Global.putInt(context.getContentResolver(), DEMO_MODE_ON, 1);

        Intent a = new Intent("com.android.systemui.demo");
        a.putExtra("command", "clock");
        a.putExtra("hhmm", "0700");
        context.sendBroadcast(a);

        Intent b = new Intent("com.android.systemui.demo");
        b.putExtra("command", "battery");
        b.putExtra("level", "100");
        b.putExtra("plugged", "false");
        context.sendBroadcast(b);

        Intent c = new Intent("com.android.systemui.demo");
        c.putExtra("command", "network");
        c.putExtra("mobile", "show");
        c.putExtra("datatype", "none");
        c.putExtra("level", "4");
        c.putExtra("fully", "true");
        context.sendBroadcast(c);

        Intent d = new Intent("com.android.systemui.demo");
        d.putExtra("command", "network");
        d.putExtra("wifi", "show");
        d.putExtra("fully", "true");
        d.putExtra("level", "4");
        context.sendBroadcast(d);

        Intent e = new Intent("com.android.systemui.demo");
        e.putExtra("command", "network");
        e.putExtra("airplane", "hide");
        context.sendBroadcast(e);

        Intent f = new Intent("com.android.systemui.demo");
        f.putExtra("command", "network");
        f.putExtra("nosim", "hide");
        context.sendBroadcast(f);

        Intent g = new Intent("com.android.systemui.demo");
        g.putExtra("command", "notifications");
        g.putExtra("visible", "false");
        context.sendBroadcast(g);

        Intent h = new Intent("com.android.systemui.demo");
        h.putExtra("command", "status");
        h.putExtra("bluetooth", "hide");
        context.sendBroadcast(h);
    }

    public static void disableDemoMode(Context context) {
        Settings.Global.putInt(context.getContentResolver(), Utils.DEMO_MODE_ON, 0);

        Intent enableDemoMode = new Intent("com.android.systemui.demo");
        enableDemoMode.putExtra("command", "exit");
        context.sendBroadcast(enableDemoMode);
    }

    public static boolean isDemoModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                DEMO_MODE_ON, 0) != 0;
    }


    public static boolean isDumpPermissionGranted(Context context) {
        return context.getPackageManager().checkPermission(Manifest.permission.DUMP, context.getPackageName())
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWriteSecureSettingsPermissionGranted(Context context) {
        return context.getPackageManager()
                .checkPermission(Manifest.permission.WRITE_SECURE_SETTINGS, context.getPackageName())
                == PackageManager.PERMISSION_GRANTED;
    }
}
