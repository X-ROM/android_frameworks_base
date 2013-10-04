package com.android.systemui.quicksettings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;
import com.android.systemui.statusbar.phone.QuickSettingsController;

public class CromTile extends QuickSettingsTile{

    public CromTile(Context context, final QuickSettingsController qsc) {
        super(context, qsc);

        mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                qsc.mBar.collapseAllPanels(true);
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(ComponentName
                        .unflattenFromString("com.crom.settings/.CrSettingsActivity"));
                intent.addCategory("android.intent.category.LAUNCHER");
                startSettingsActivity(intent);
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
        mDrawable = R.drawable.ic_qs_crom;
        mLabel = mContext.getString(R.string.quick_settings_crom_label);
    }
}
