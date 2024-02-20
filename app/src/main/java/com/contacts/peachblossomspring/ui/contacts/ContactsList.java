package com.contacts.peachblossomspring.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.contacts.peachblossomspring.R;

import java.util.List;

public class ContactsList extends ArrayAdapter<Contact> {
    private final Context mContext;
    private final List<Contact> mData;

    public ContactsList(Context context, List<Contact> data) {
        super(context, 0, data);
        mContext = context;
        mData = data;
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

        return convertView;
    }
}
