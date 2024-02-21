package com.contacts.peachblossomspring.ui.contacts;

// 导入所需的Android和Java类
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 定义一个公开的DBHelper类，它继承自SQLiteOpenHelper
public class DBHelper extends SQLiteOpenHelper {
    // 定义数据库的名称和版本
    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 1;

    // 定义联系人表的名称和列名
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";

    // 定义创建联系人表的SQL语句
    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " +
            TABLE_CONTACTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_PHONE + " TEXT)";

    // 定义DBHelper的构造函数
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 重写SQLiteOpenHelper类的onCreate方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行创建联系人表的SQL语句
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // 重写SQLiteOpenHelper类的onUpgrade方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 删除旧的联系人表，并创建新的联系人表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // 定义一个公开的addContact方法，用于向数据库中添加联系人
    public void addContact(String name, String phone) {
        // 获取一个可写的数据库，并创建一个ContentValues对象
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 向ContentValues对象中添加联系人的姓名和电话
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        // 将ContentValues对象插入到数据库中，并关闭数据库
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // 定义一个公开的getAllContacts方法，用于从数据库中获取所有的联系人
    //已废弃

    //检查一个联系人是否已经存在于数据库
    public boolean isContactExists(String name, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS,
                new String[] { COLUMN_NAME, COLUMN_PHONE },
                COLUMN_NAME + "=? AND " + COLUMN_PHONE + "=?",
                new String[] { name, phone },
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
