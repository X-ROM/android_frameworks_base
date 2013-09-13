/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.systemui.screenstate;

import android.content.Context;
import android.provider.Settings;
import android.net.ConnectivityManager;
import android.util.Log;
import android.content.Intent;

public class MobileDataToggle extends ScreenStateToggle {
    private static final String TAG = "ScreenStateService_MobileDataToggle";

    public static final String MOBILE_DATA_CHANGED="com.android.internal.telephony.MOBILE_DATA_CHANGED";
    public static final String NETWORK_MODE = "networkMode";
        
    public MobileDataToggle(Context context){
        super(context);
    }

    protected boolean isEnabled(){
        return Settings.System.getBoolean(mContext.getContentResolver(), Settings.System.SCREEN_STATE_MOBILE_DATA, false);
    }

    protected boolean doScreenOnAction(){
        return mDoAction;
    }

    protected boolean doScreenOffAction(){
        if (isMobileDataEnabled()){
            mDoAction = true;
        } else {
            mDoAction = false;
        }
        return mDoAction;
    }

    private boolean isMobileDataEnabled(){
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getMobileDataEnabled();
    }

    protected Runnable getScreenOffAction(){
        return new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm =
                    (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                cm.setMobileDataEnabled(false);

                Intent intent = new Intent(MOBILE_DATA_CHANGED);
                intent.putExtra(NETWORK_MODE, false);
                mContext.sendBroadcast(intent);
                Log.d(TAG, "mobileData = false");
            }
        };
    }
    protected Runnable getScreenOnAction(){
        return new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm =
                    (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                cm.setMobileDataEnabled(true);

                Intent intent = new Intent(MOBILE_DATA_CHANGED);
                intent.putExtra(NETWORK_MODE, true);
                mContext.sendBroadcast(intent);
                Log.d(TAG, "mobileData = true");
            }
        };
    }
}
