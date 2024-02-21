package com.contacts.peachblossomspring;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.contacts.peachblossomspring.ui.contacts.DBHelper;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameEditText; // 姓名输入框
    private EditText phoneEditText; // 电话输入框
    private DBHelper dbHelper; // 数据库帮助类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("添加联系人"); // 设置标题栏标题为"添加联系人"
        }

        nameEditText = findViewById(R.id.nameEditText); // 获取姓名输入框控件
        phoneEditText = findViewById(R.id.phoneEditText); // 获取电话输入框控件
        Button submitButton = findViewById(R.id.submitButton); // 获取提交按钮控件
        dbHelper = new DBHelper(this); // 创建数据库帮助类实例

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim(); // 获取输入的姓名并去除首尾空格
            String phone = phoneEditText.getText().toString().trim(); // 获取输入的电话并去除首尾空格
            if (!name.isEmpty() && !phone.isEmpty()) { // 判断姓名和电话是否都不为空
                if (dbHelper.isContactExists(name, phone)) { // 判断联系人是否已存在于数据库中
                    Toast.makeText(this, "当前联系人已存在", Toast.LENGTH_SHORT).show(); // 显示联系人已存在的提示
                } else {
                    dbHelper.addContact(name, phone); // 将联系人添加到数据库中
                    Toast.makeText(this, "联系人已保存", Toast.LENGTH_SHORT).show(); // 显示联系人已保存的提示
                    setResult(RESULT_OK); // 设置返回结果为成功
                    finish(); // 关闭当前Activity
                }
            } else {
                // 显示错误消息或提示
                // 在此处添加逻辑以显示错误消息或提示，例如Toast或Snack bar等
            }
        });

    }
}
