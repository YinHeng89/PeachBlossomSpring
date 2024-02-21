package com.contacts.peachblossomspring.ui.contacts;

// 导入所需的Android、AndroidX和Java类
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.contacts.peachblossomspring.R;
import java.util.ArrayList;
import java.util.List;

// 定义一个公开的HomeFragment类，它继承自Fragment
public class HomeFragment extends Fragment {
    // 定义一个私有的DBHelper对象
    private DBHelper dbHelper;

    // 重写Fragment类的onCreateView方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 通过LayoutInflater对象的inflate方法，将布局文件fragment_home.xml转化为View对象
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 通过View对象的findViewById方法，获取布局文件中的ListView组件
        ListView listView = view.findViewById(R.id.select_dialog_listview);

        // 创建一个DBHelper对象，并从数据库中获取联系人数据
        dbHelper = new DBHelper(requireContext());
        List<Contact> data = getContactsFromDatabase();

        // 创建一个ContactsList适配器，并将其设置为ListView的适配器
        ContactsList adapter = new ContactsList(requireContext(), data);
        listView.setAdapter(adapter);

        // 为ListView的每一个列表项设置点击事件监听器
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // 获取被点击的联系人，并获取其电话号码
            Contact contact = data.get(position);
            String phone = contact.getPhone();

            // 创建一个打电话的Intent，并启动它
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        });

        // 返回View对象
        return view;
    }

    // 定义一个私有的getContactsFromDatabase方法，用于从数据库中获取联系人数据
    private List<Contact> getContactsFromDatabase() {
        // 创建一个联系人列表，并获取一个可读的数据库
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询数据库，并将查询结果保存在Cursor对象中
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        // 遍历Cursor对象，获取每一行的数据，并将其添加到联系人列表中
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE));
            contacts.add(new Contact(name, phone));
        }

        // 关闭Cursor和数据库，并返回联系人列表
        cursor.close();
        db.close();

        return contacts;
    }
}
