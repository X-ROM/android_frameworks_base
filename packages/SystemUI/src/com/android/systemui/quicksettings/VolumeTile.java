package com.android.systemui.quicksettings;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsController;

public class VolumeTile extends QuickSettingsTile {

    private AudioManager mAudioManager;

    public VolumeTile(Context context, 
            final QuickSettingsController qsc, Handler handler) {
        super(context, qsc);

        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);

        qsc.registerAction(AudioManager.VOLUME_CHANGED_ACTION, this);
        qsc.registerAction(AudioManager.RINGER_MODE_CHANGED_ACTION, this);

        mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isFlipTilesEnabled()) {
                    flipTile(0);
                }
                qsc.mBar.collapseAllPanels(true);
                mAudioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
            }
        };

        mOnLongClick = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                startSettingsActivity(android.provider.Settings.ACTION_SOUND_SETTINGS);
                return true;
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        updateResources();
    }

    @Override
    void onPostCreate() {
        updateTile();
        super.onPostCreate();
    }

    @Override
    public void updateResources() {
        updateTile();
        updateQuickSettings();
    }

    private synchronized void updateTile() {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int value = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int level = value * 100 / max;
        boolean silent = mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
        if (silent || level == 0) {
            mDrawable = R.drawable.ic_qs_volume_0;
        } else if (level <= 25) {
            mDrawable = R.drawable.ic_qs_volume_1;
        } else if (level <= 50) {
            mDrawable = R.drawable.ic_qs_volume_2;
        } else if (level <= 75) {
            mDrawable = R.drawable.ic_qs_volume_3;
        } else {
            mDrawable = R.drawable.ic_qs_volume_4;
        }
        mLabel = mContext.getString(R.string.quick_settings_volume);
    }
}
