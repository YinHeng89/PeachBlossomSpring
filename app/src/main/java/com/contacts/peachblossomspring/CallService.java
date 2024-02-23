package com.contacts.peachblossomspring;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.WindowManager;

import com.contacts.peachblossomspring.ui.contacts.DBHelper;

public class CallService extends Service {

    public static final String ACTION_UPDATE_TEXT = "com.contacts.peachblossomspring.ACTION_UPDATE_TEXT";
    public static final String EXTRA_PHONE_NUMBER = "com.contacts.peachblossomspring.EXTRA_PHONE_NUMBER";
    public static final String ACTION_HIDE_WINDOW = "com.contacts.peachblossomspring.ACTION_HIDE_WINDOW";

    private WindowManager windowManager;
    private View floatingWindow;
    private TextView textViewFloating;
    private DBHelper dbHelper;
    private boolean isInCall = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化组件
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        dbHelper = new DBHelper(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        floatingWindow = layoutInflater.inflate(R.layout.floating_window, null);
        textViewFloating = floatingWindow.findViewById(R.id.textViewFloating);

        // 设置悬浮窗口参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER | Gravity.TOP;
        int parentHeight = windowManager.getDefaultDisplay().getHeight();
        int verticalPosition = (int) (parentHeight / 4.5);
        params.x = 0;
        params.y = verticalPosition;

        // 添加悬浮窗口
        windowManager.addView(floatingWindow, params);
        floatingWindow.setVisibility(View.GONE);

        // 监听来电状态
        TelephonyManager telephonyManager = getSystemService(TelephonyManager.class);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        handleIncomingCall(phoneNumber);
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        handleCallIdle();
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        isInCall = true;
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    // 处理来电
    private void handleIncomingCall(String phoneNumber) {
        Intent intent = new Intent(CallService.this, CallService.class);
        intent.setAction(ACTION_UPDATE_TEXT);
        intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
        startService(intent);
        isInCall = true;
    }

    // 处理通话结束
    private void handleCallIdle() {
        if (isInCall) {
            isInCall = false;
            Intent intentHide = new Intent(CallService.this, CallService.class);
            intentHide.setAction(ACTION_HIDE_WINDOW);
            startService(intentHide);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_UPDATE_TEXT.equals(action)) {
                String phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
                updateFloatingWindowText(phoneNumber);
                floatingWindow.setVisibility(View.VISIBLE);
            } else if (ACTION_HIDE_WINDOW.equals(action)) {
                hideFloatingWindow();
            } else {
                floatingWindow.setVisibility(View.GONE);
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 更新悬浮窗口文本信息
    private void updateFloatingWindowText(String phoneNumber) {
        String name = dbHelper.getContactNameByPhone(phoneNumber);
        textViewFloating.setText(name != null ? name : phoneNumber);
    }

    // 隐藏悬浮窗口
    private void hideFloatingWindow() {
        floatingWindow.setVisibility(View.GONE);
        stopSelf();
    }
}
