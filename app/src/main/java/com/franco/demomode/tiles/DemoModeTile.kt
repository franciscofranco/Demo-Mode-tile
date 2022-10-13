package com.franco.demomode.tiles

import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.franco.demomode.R
import com.franco.demomode.Utils
import com.franco.demomode.activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DemoModeTile : TileService() {
    override fun onStartListening() {
        super.onStartListening()

        GlobalScope.launch {
            val isDemoModeAllowed = Utils().isDemoModeAllowed(applicationContext)
            val isDemoMode = Utils().isDemoModeOn(applicationContext)

            qsTile?.state = when {
                !isDemoModeAllowed -> Tile.STATE_UNAVAILABLE
                isDemoMode -> Tile.STATE_ACTIVE
                else -> Tile.STATE_INACTIVE
            }

            qsTile?.icon = when {
                isDemoMode -> Icon.createWithResource(applicationContext, R.drawable.ic_on)
                else -> Icon.createWithResource(applicationContext, R.drawable.ic_off)
            }

            qsTile?.updateTile()
        }
    }

    override fun onClick() {
        super.onClick()

        GlobalScope.launch {
            val hasDumpPermission = Utils().isDumpPermissionGranted(applicationContext)
            val hasWriteSecurePermission = Utils().isWriteSecureSettingsPermissionGranted(applicationContext)

            when {
                !hasDumpPermission || !hasWriteSecurePermission -> {
                    Intent(applicationContext, MainActivity::class.java).apply {
                        action = Utils.MISSING_PERMISSION
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivityAndCollapse(this)
                    }
                }

                else -> {
                    qsTile?.apply {
                        if (state == Tile.STATE_ACTIVE) {
                            state = Tile.STATE_INACTIVE
                            icon = Icon.createWithResource(applicationContext, R.drawable.ic_off)
                            Utils().disableDemoMode(applicationContext)
                        } else {
                            state = Tile.STATE_ACTIVE
                            icon = Icon.createWithResource(applicationContext, R.drawable.ic_on)
                            Utils().enableDemoMode(applicationContext)
                        }

                        updateTile()
                    }
                }
            }
        }
    }
}