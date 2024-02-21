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
import java.text.Collator;
import java.util.List;
import java.util.Locale;

// 定义一个公开的ContactsList类，它继承自ArrayAdapter<Contact>
public class ContactsList extends ArrayAdapter<Contact> {
    // 定义两个私有的变量，分别表示上下文和联系人数据列表
    private final Context mContext;
    private final List<Contact> mData;

    // ContactsList类的构造函数，需要传入上下文和联系人数据列表
    public ContactsList(Context context, List<Contact> data) {
        super(context, 0, data);
        mContext = context;
        mData = data;

        // 创建一个 Collator 实例，用于字符串比较，这里设置为中国的排序规则
        final Collator collator = Collator.getInstance(Locale.CHINA);

        // 使用 List.sort() 方法对 mData 列表进行排序
        // 使用一个 Lambda 表达式来实现，比较器使用 Collator 对联系人的名字进行比较
        mData.sort((c1, c2) -> collator.compare(c1.getName(), c2.getName()));
    }

    // 重写ArrayAdapter类的getView方法，用于获取列表中每个项的视图
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 如果视图为空，则从XML布局文件中加载视图
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_contact, parent, false);
        }

        // 获取当前位置的联系人数据
        Contact item = mData.get(position);

        // 从视图中获取显示名字和电话号码的TextView控件
        TextView nameTextView = convertView.findViewById(R.id.text_name);
        TextView phoneTextView = convertView.findViewById(R.id.text_phone);

        // 将联系人的名字和电话号码设置到TextView控件中
        nameTextView.setText(item.getName());
        phoneTextView.setText(item.getPhone());

        // 返回视图
        return convertView;
    }
}
