package com.contacts.peachblossomspring.ui.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.contacts.peachblossomspring.R;

import java.text.Collator;
import java.util.List;
import java.util.Locale;

public class ContactsList extends ArrayAdapter<Contact> {
    private final Context mContext;
    private final List<Contact> mData;
    private final DBHelper dbHelper;

    public ContactsList(Context context, List<Contact> data) {
        super(context, 0, data);
        mContext = context;
        mData = data;
        dbHelper = new DBHelper(mContext);
        final Collator collator = Collator.getInstance(Locale.CHINA); // 修改为正确的写法
        mData.sort((c1, c2) -> collator.compare(c1.getName(), c2.getName()));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_contact, parent, false);
        }

        Contact item = mData.get(position);
        TextView nameTextView = convertView.findViewById(R.id.text_name);
        TextView phoneTextView = convertView.findViewById(R.id.text_phone);
        nameTextView.setText(item.getName());
        phoneTextView.setText(item.getPhone());

        convertView.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + item.getPhone()));
            mContext.startActivity(dialIntent);
        });

        convertView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("确定要删除此联系人吗？")
                    .setPositiveButton("确定", (dialog, which) -> deleteContact(item))
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        });

        return convertView;
    }


    private void deleteContact(Contact contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_CONTACTS, DBHelper.COLUMN_NAME + " = ? AND " + DBHelper.COLUMN_PHONE + " = ?", new String[]{contact.getName(), contact.getPhone()});
        mData.remove(contact);
        notifyDataSetChanged();
        db.close();

        // 获取 ImageView 组件
        ImageView defaultImage = ((Activity) mContext).findViewById(R.id.default_image); // 修改为正确的 ID

        // 如果联系人列表为空,显示默认图片;否则隐藏默认图片
        if (mData.isEmpty()) {
            defaultImage.setVisibility(View.VISIBLE);
        } else {
            defaultImage.setVisibility(View.GONE);
        }
    }
}