package com.contacts.peachblossomspring;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
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

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.addContactButton.setOnClickListener(view -> openFilePickerIntent());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        dbHelper = new DBHelper(this);

        openFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleFilePickerResult);
    }

    private void openFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/x-vcard");
        openFilePickerLauncher.launch(intent);
    }

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
                String successMessage = "已成功导入" + count + "联系人";
                if (duplicateCount > 0) {
                    successMessage += "，忽略" + duplicateCount + "人";
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
