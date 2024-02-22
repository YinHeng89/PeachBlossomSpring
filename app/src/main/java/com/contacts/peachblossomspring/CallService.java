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

                String stateString = "";
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        stateString = "IDLE";
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        stateString = "RINGING";
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        stateString = "OFF-HOOK";
                        break;
                }

                Log.i("onCallStateChanged", "State: " + stateString + ", Phone Number: " + phoneNumber);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
