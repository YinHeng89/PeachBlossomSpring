package com.contacts.peachblossomspring;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class CallService extends Service {
    private boolean isInCall = false; // 标志变量，用于表示是否正在通话中
//    private View windowsView;
//    private WindowManager windowManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // 实例化windowsView
//        windowsView = LayoutInflater.from(this).inflate(R.layout.floating_window, null);

        // 获取WindowManager
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        TelephonyManager telephonyManager = getSystemService(TelephonyManager.class);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: // 电话响铃状态
                        // 电话响铃,启动FloatingWindowService并显示悬浮窗
                        Intent updateIntent = new Intent(CallService.this, FloatingWindowService.class);
                        updateIntent.setAction(FloatingWindowService.ACTION_UPDATE_TEXT);
                        updateIntent.putExtra(FloatingWindowService.EXTRA_PHONE_NUMBER, phoneNumber);
                        startService(updateIntent);
                        isInCall = true; // 将通话状态标志设置为true
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        if (isInCall) {
                            isInCall = false;
                            Intent hideIntent = new Intent(CallService.this, FloatingWindowService.class);
                            hideIntent.setAction(FloatingWindowService.ACTION_HIDE_WINDOW);
                            startService(hideIntent);
                        }
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK: // 电话接通状态
                        isInCall = true; // 将通话状态标志设置为true
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
