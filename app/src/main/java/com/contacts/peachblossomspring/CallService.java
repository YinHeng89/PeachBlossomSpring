package com.contacts.peachblossomspring;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

public class CallService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        TelephonyManager telephonyManager = getSystemService(TelephonyManager.class);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        // 通话结束，停止FloatingWindowService
                        stopService(new Intent(CallService.this, FloatingWindowService.class));
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        // 电话响铃，启动FloatingWindowService并显示悬浮窗
                        Intent updateIntent = new Intent(CallService.this, FloatingWindowService.class);
                        updateIntent.setAction(FloatingWindowService.ACTION_UPDATE_TEXT);
                        updateIntent.putExtra(FloatingWindowService.EXTRA_PHONE_NUMBER, phoneNumber);
                        startService(updateIntent);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
