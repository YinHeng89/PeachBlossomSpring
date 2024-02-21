package com.contacts.peachblossomspring;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.contacts.peachblossomspring.ui.contacts.Contact;
import com.contacts.peachblossomspring.ui.contacts.DBHelper;
import com.contacts.peachblossomspring.ui.contacts.VCFParser;
import com.google.android.material.navigation.NavigationView;
import java.io.InputStream;
import java.util.List;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.contacts.peachblossomspring.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private DBHelper dbHelper;
    private ActivityResultLauncher<Intent> openFilePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.contacts.peachblossomspring.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置应用栏
        setSupportActionBar(binding.appBarMain.toolbar);

        // 设置添加联系人按钮的点击事件
        binding.appBarMain.addContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            addContactLauncher.launch(intent);
        });



        // 设置抽屉导航
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 初始化数据库帮助类
        dbHelper = new DBHelper(this);

        // 注册文件选择器启动器
        openFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleFilePickerResult);
    }

    // 打开文件选择器
    private void openFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/x-vcard");
        openFilePickerLauncher.launch(intent);
    }

    // 处理文件选择器结果
    private void handleFilePickerResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    ContentResolver resolver = getContentResolver();
                    InputStream stream = resolver.openInputStream(uri);
                    if (stream != null) {
                        importVCFFile(stream);
                        stream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 导入VCF文件中的联系人
    private void importVCFFile(InputStream stream) {
        VCFParser parser = new VCFParser();
        try {
            List<Contact> contacts = parser.parseContacts(stream);
            int count = 0;
            int duplicateCount = 0; // 重复联系人数量
            for (Contact contact : contacts) {
                if (!dbHelper.isContactExists(contact.getName(), contact.getPhone())) {
                    dbHelper.addContact(contact.getName(), contact.getPhone());
                    count++;
                } else {
                    duplicateCount++;
                }
            }
            recreate();

            if (count > 0) {
                String successMessage = "成功导入" + count + "个联系人";
                if (duplicateCount > 0) {
                    successMessage += "，已忽略" + duplicateCount + "人";
                }
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "导入的联系人已存在", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "导入联系人失败", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单资源文件，将菜单项添加到菜单中
        getMenuInflater().inflate(R.menu.main, menu);
        // 返回true表示菜单已经被创建
        return true;
    }

    //打开文件选择器导入本地联系人
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 检查被点击的菜单项的ID是否与import-contacts菜单项的ID匹配
        if (item.getItemId() == R.id.importcontactsstorage) {
            // 如果匹配，则调用openFilePickerIntent()方法，触发导入联系人的功能
            openFilePickerIntent();
            // 返回true表示已经处理了菜单项的点击事件
            return true;
        }
        // 如果被点击的菜单项不是import-contacts，交给父类处理
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private final ActivityResultLauncher<Intent> addContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 刷新联系人列表
                    recreate();
                }
            }
    );
}
