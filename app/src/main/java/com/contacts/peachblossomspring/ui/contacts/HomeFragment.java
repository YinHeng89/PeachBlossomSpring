package com.contacts.peachblossomspring.ui.contacts; // 定义了一个名为com.contacts.peachblossomspring.ui.contacts的包

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle; // 导入android.os.Bundle类
import android.view.LayoutInflater; // 导入android.view.LayoutInflater类
import android.view.View; // 导入android.view.View类
import android.view.ViewGroup; // 导入android.view.ViewGroup类
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment; // 导入androidx.fragment.app.Fragment类

import com.contacts.peachblossomspring.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = view.findViewById(R.id.select_dialog_listview);

        dbHelper = new DBHelper(requireContext());
        List<Contact> data = getContactsFromDatabase();

        ContactsList adapter = new ContactsList(requireContext(), data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = data.get(position);
                String phone = contact.getPhone();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        return view;
    }

    private List<Contact> getContactsFromDatabase() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE));
            contacts.add(new Contact(name, phone));
        }

        cursor.close();
        db.close();

        return contacts;
    }
}
