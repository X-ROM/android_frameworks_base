
package com.android.systemui.statusbar.toggles;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;

import com.android.systemui.R;
import com.android.internal.util.crom.SysHelpers;

public class PieToggle extends StatefulToggle {

    SettingsObserver mSettingsObserver;

    @Override
    public void init(Context c, int style) {
        super.init(c, style);
        mSettingsObserver = new SettingsObserver(new Handler());
        scheduleViewUpdate();
    }

    @Override
    protected void doEnable() {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.System.PIE_CONTROLS, 1);
    }

    @Override
    protected void doDisable() {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.System.PIE_CONTROLS, 0);
    }

    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName("com.android.settings", "com.android.settings.Settings$PieActivity");
        intent.addCategory("android.intent.category.LAUNCHER");
        startActivity(intent);
        return super.onLongClick(v);
    }

    @Override
    protected void updateView() {
        boolean enabled = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.PIE_CONTROLS, 0) == 1;
        setEnabledState(enabled);
        setIcon(enabled ? R.drawable.ic_qs_pie_on : R.drawable.ic_qs_pie_off);
        setLabel(enabled ? R.string.quick_settings_pie_on_label
                : R.string.quick_settings_pie_off_label);
        super.updateView();
    }

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
            observe();
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System
                    .getUriFor(Settings.System.PIE_CONTROLS), false,
                    this);
        }

        @Override
        public void onChange(boolean selfChange) {
            SysHelpers.restartSystemUI();
        }
    }

}
