package com.android.systemui.quicksettings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;
import com.android.systemui.statusbar.phone.QuickSettingsController;

public class BatterySaverTile extends QuickSettingsTile{

    public BatterySaverTile(Context context, final QuickSettingsController qsc) {
        super(context, qsc);

        mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                qsc.mBar.collapseAllPanels(true);

                boolean checkModeOn = Settings.Global.getInt(mContext.getContentResolver(),
                                        Settings.Global.BATTERY_SAVER_OPTION, 0) == 1;
                Settings.Global.putInt(mContext.getContentResolver(),
                                        Settings.Global.BATTERY_SAVER_OPTION, checkModeOn ? 0 : 1);

                Intent scheduleSaver = new Intent();
                scheduleSaver.setAction(Intent.ACTION_BATTERY_SERVICES);
                mContext.sendBroadcast(scheduleSaver);
                if (isFlipTilesEnabled()) {
                    flipTile(0);
                }
            }
        };

        mOnLongClick = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startSettingsActivity(Intent.ACTION_POWER_USAGE_SUMMARY);
                return true;
            }
        };

    }

    @Override
    void onPostCreate() {
        updateTile();
        super.onPostCreate();
    }

    @Override
    public void updateResources() {
        updateTile();
        super.updateResources();
    }

    private synchronized void updateTile() {
        mDrawable = R.drawable.ic_qs_battery_saver_off;
        mLabel = mContext.getString(R.string.quick_settings_battery_saver_off_label);
    }
}
