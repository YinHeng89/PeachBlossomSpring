package com.contacts.peachblossomspring;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.contacts.peachblossomspring.ui.contacts.DBHelper;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getSupportActionBar().setTitle("添加联系人");

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        Button submitButton = findViewById(R.id.submitButton);
        dbHelper = new DBHelper(this);

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            if (!name.isEmpty() && !phone.isEmpty()) {
                if (dbHelper.isContactExists(name, phone)) {
                    Toast.makeText(this, "当前联系人已存在", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addContact(name, phone);
                    Toast.makeText(this, "联系人已保存", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                // 显示错误消息或提示
            }
        });

    }
}