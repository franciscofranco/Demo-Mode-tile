package com.franco.demomode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.franco.demomode.application.App;

public class Utils {
    public static final String DEMO_MODE_ON = "sysui_tuner_demo_on";
    public static final String MISSING_PERMISSION = "missing_permission";

    public static void enableDemoMode() {
        Settings.Global.putInt(App.CONTEXT.getContentResolver(), DEMO_MODE_ON, 1);

        Intent a = new Intent("com.android.systemui.demo");
        a.putExtra("command", "clock");
        a.putExtra("hhmm", "0700");
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
        App.CONTEXT.sendBroadcast(c);

        Intent d = new Intent("com.android.systemui.demo");
        d.putExtra("command", "network");
        d.putExtra("wifi", "show");
        d.putExtra("fully", "true");
        d.putExtra("level", "4");
        App.CONTEXT.sendBroadcast(d);

        Intent e = new Intent("com.android.systemui.demo");
        e.putExtra("command", "network");
        e.putExtra("airplane", "hide");
        App.CONTEXT.sendBroadcast(e);

        Intent f = new Intent("com.android.systemui.demo");
        f.putExtra("command", "network");
        f.putExtra("nosim", "hide");
        App.CONTEXT.sendBroadcast(f);

        Intent g = new Intent("com.android.systemui.demo");
        g.putExtra("command", "notifications");
        g.putExtra("visible", "false");
        App.CONTEXT.sendBroadcast(g);

        Intent h = new Intent("com.android.systemui.demo");
        h.putExtra("command", "status");
        h.putExtra("bluetooth", "hide");
        App.CONTEXT.sendBroadcast(h);
    }

    public static void disableDemoMode() {
        Settings.Global.putInt(App.CONTEXT.getContentResolver(), Utils.DEMO_MODE_ON, 0);

        Intent enableDemoMode = new Intent("com.android.systemui.demo");
        enableDemoMode.putExtra("command", "exit");
        App.CONTEXT.sendBroadcast(enableDemoMode);
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
