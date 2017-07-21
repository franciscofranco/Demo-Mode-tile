package com.franco.demomode.tiles;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.franco.demomode.R;
import com.franco.demomode.Utils;
import com.franco.demomode.activities.MainActivity;

public class DemoModeTile extends TileService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        getQsTile().setState(Utils.isDemoModeOn(getApplicationContext()) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        getQsTile().setIcon(Utils.isDemoModeOn(getApplicationContext()) ?
                Icon.createWithResource(getApplicationContext(), R.drawable.ic_on) :
                Icon.createWithResource(getApplicationContext(), R.drawable.ic_off));
        getQsTile().updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();

        if (!Utils.isDumpPermissionGranted(getApplicationContext())
                || !Utils.isWriteSecureSettingsPermissionGranted(getApplicationContext())) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            mainActivity.setAction(Utils.MISSING_PERMISSION);
            mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
        } else {
            if (getQsTile().getState() == Tile.STATE_ACTIVE) {
                getQsTile().setState(Tile.STATE_INACTIVE);
                getQsTile().setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_off));
                getQsTile().updateTile();

                Utils.disableDemoMode(getApplicationContext());
            } else {
                getQsTile().setState(Tile.STATE_ACTIVE);
                getQsTile().setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_on));
                getQsTile().updateTile();

                Utils.enableDemoMode(getApplicationContext());
            }
        }
    }
}
