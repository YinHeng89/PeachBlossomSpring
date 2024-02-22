package com.contacts.peachblossomspring;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.contacts.peachblossomspring.ui.contacts.DBHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Service负责在电话通话期间显示带有电话号码或联系人姓名的悬浮窗口。
 */
public class FloatingWindowService extends Service {
    public static final String ACTION_UPDATE_TEXT = "com.contacts.peachblossomspring.ACTION_UPDATE_TEXT";
    public static final String EXTRA_PHONE_NUMBER = "com.contacts.peachblossomspring.EXTRA_PHONE_NUMBER";

    private WindowManager windowManager;
    private View floatingWindows;
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

        // 初始化WindowManager和DBHelper
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        dbHelper = new DBHelper(this);

        // 填充悬浮窗口的布局
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        floatingWindows = layoutInflater.inflate(R.layout.floating_window, null);
        textViewFloating = floatingWindows.findViewById(R.id.textViewFloating);

        // 设置WindowManager的LayoutParams
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // 设置悬浮窗口的位置和重力
        params.gravity = Gravity.CENTER | Gravity.TOP;
        int parentHeight = windowManager.getDefaultDisplay().getHeight();
        int verticalPosition = (int) (parentHeight / 4.5);
        params.x = 0;
        params.y = verticalPosition;

        // 将悬浮窗口添加到WindowManager，并隐藏它
        windowManager.addView(floatingWindows, params);
        floatingWindows.setVisibility(View.GONE);
    }

    /**
     * 根据联系人电话号码更新悬浮窗口中显示的文本，如果联系人姓名可用则显示姓名，否则显示电话号码。
     * @param phoneNumber 要显示的电话号码
     */
    public void updateFloatingWindowText(String phoneNumber) {
        String name = dbHelper.getContactNameByPhone(phoneNumber);
        if (name != null) {
            textViewFloating.setText(name);
        } else {
            textViewFloating.setText(phoneNumber);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {
                String action = intent.getAction();
                if (ACTION_UPDATE_TEXT.equals(action)) {
                    String phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
                    updateFloatingWindowText(phoneNumber);
                    // 显示悬浮窗口
                    floatingWindows.setVisibility(View.VISIBLE);
                } else {
                    // 隐藏悬浮窗口并停止服务
                    floatingWindows.setVisibility(View.GONE);
                    stopSelf();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
