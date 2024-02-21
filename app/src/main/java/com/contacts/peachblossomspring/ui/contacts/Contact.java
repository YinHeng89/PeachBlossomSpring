package com.contacts.peachblossomspring.ui.contacts;

// 导入java.util.Objects类，后续用于实现equals和hashCode方法
import java.util.Objects;

// 定义一个公开的Contact类
public class Contact {
    // 定义两个私有的、只读的字符串变量，分别表示联系人的名字和电话号码
    private final String name;
    private final String phone;

    // Contact类的构造函数，需要传入名字和电话号码
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // 公开的getName方法，返回联系人的名字
    public String getName() {
        return name;
    }

    // 公开的getPhone方法，返回联系人的电话号码
    public String getPhone() {
        return phone;
    }

    // 重写Object类的equals方法，用于比较两个Contact对象是否相等
    // 如果名字和电话号码都相同，则认为两个Contact对象相等
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
                Objects.equals(phone, contact.phone);
    }

    // 重写Object类的hashCode方法，用于获取Contact对象的哈希码
    // 哈希码的计算基于联系人的名字和电话号码
    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }
}
