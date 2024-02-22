package com.contacts.peachblossomspring;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.contacts.peachblossomspring.ui.contacts.DBHelper;

import java.util.Timer;
import java.util.TimerTask;

public class FloatingWindowService extends Service {
    public static final String ACTION_UPDATE_TEXT = "com.contacts.peachblossomspring.ACTION_UPDATE_TEXT";
    public static final String EXTRA_PHONE_NUMBER = "com.contacts.peachblossomspring.EXTRA_PHONE_NUMBER";

    private WindowManager windowManager;
    private View floatingView;
    private TextView textViewFloating;
    private DBHelper dbHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        dbHelper = new DBHelper(this);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        floatingView = layoutInflater.inflate(R.layout.floating_window, null);
        textViewFloating = floatingView.findViewById(R.id.textViewFloating);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // 设置悬浮窗口的位置和重力
        params.gravity = Gravity.CENTER | Gravity.TOP;
        int parentHeight = windowManager.getDefaultDisplay().getHeight();
        int verticalPosition = (int) (parentHeight / 4.5);
        params.x = 0;
        params.y = verticalPosition;


        // 隐藏悬浮窗
        floatingView.setVisibility(View.GONE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {
                String action = intent.getAction();
                if (ACTION_UPDATE_TEXT.equals(action)) {
                    String phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
                    updateFloatingWindowText(phoneNumber);

                    // 显示悬浮窗
                    floatingView.setVisibility(View.VISIBLE);
                } else {
                    // 隐藏悬浮窗
                    floatingView.setVisibility(View.GONE);
                    // 停止服务
                    stopSelf();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
            floatingView = null;
        }
    }

    public void updateFloatingWindowText(String phoneNumber) {
        String name = dbHelper.getContactNameByPhone(phoneNumber);
        textViewFloating.setText(name);
    }
}
