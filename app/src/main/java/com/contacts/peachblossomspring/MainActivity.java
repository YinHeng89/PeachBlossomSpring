package com.contacts.peachblossomspring;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.contacts.peachblossomspring.ui.contacts.Contact;
import com.contacts.peachblossomspring.ui.contacts.DBHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.contacts.peachblossomspring.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.contacts.peachblossomspring.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 插入数据并获取是否成功插入数据的标志
                boolean isInserted = insertTestData();

                // 如果成功插入数据，刷新页面
                if (isInserted) {
                    recreate();
                }
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 创建DBHelper实例
        dbHelper = new DBHelper(this);
    }

    private boolean insertTestData() {
        // 获取已有联系人列表
        List<Contact> contacts = dbHelper.getAllContacts();

        int successCount = 0; // 成功添加的联系人数量

        // 插入测试数据并去重
        if (!contacts.contains(new Contact("ナナちゃん", "13668130182"))) {
            dbHelper.addContact("ナナちゃん", "13668130182");
            successCount++;
        }
        if (!contacts.contains(new Contact("向泽红", "15883623116"))) {
            dbHelper.addContact("向泽红", "15883623116");
            successCount++;
        }
        if (!contacts.contains(new Contact("尹恒", "17399656562"))) {
            dbHelper.addContact("尹恒", "17399656562");
            successCount++;
        }
        if (!contacts.contains(new Contact("布丁", "18602809874"))) {
            dbHelper.addContact("布丁", "18602809874");
            successCount++;
        }
        if (!contacts.contains(new Contact("熊怪", "18581619151"))) {
            dbHelper.addContact("熊怪", "18581619151");
            successCount++;
        }
        if (!contacts.contains(new Contact("陆沉", "95779166"))) {
            dbHelper.addContact("陆沉", "95779166");
            successCount++;
        }
        if (!contacts.contains(new Contact("萧逸", "95795011"))) {
            dbHelper.addContact("萧逸", "95795011");
            successCount++;
        }
        if (!contacts.contains(new Contact("齐司礼", "95795177"))) {
            dbHelper.addContact("齐司礼", "95795177");
            successCount++;
        }
        if (!contacts.contains(new Contact("查理苏", "95779055"))) {
            dbHelper.addContact("查理苏", "95779055");
            successCount++;
        }
        if (!contacts.contains(new Contact("夏鸣星", "95779100"))) {
            dbHelper.addContact("夏鸣星", "95779100");
            successCount++;
        }

        // 可以继续插入更多测试数据

        //Toast通知信息
        if (successCount > 0) {
            Toast toast = Toast.makeText(this, "已成功添加" + successCount + "个联系人信息", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();

            // 返回成功插入数据的标志
            return true;
        } else {
            Toast toast = Toast.makeText(this, "联系人已存在，请勿重复添加", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();

            // 返回未成功插入数据的标志
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
