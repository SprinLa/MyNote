package com.mycompany.mynote.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.mycompany.mynote.dummy.DummyContent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by delia on 16/5/9.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper{
    final String CREATE_TABLE_SQL =
            "create table if not exists note_info( _id integer  primary key autoincrement," +
                                                 " title  varchar(50),"+
                                                 " detail  text(65535),"+
                                                 " date varchar(50))";

    public MyDataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);//文件名，版本号
//        System.out.println("MyDataBaseHelper");
        /*
        * 上是SQLiteOpenHelper 的构造函数，当数据库不存在时，就会创建数据库，然后打开数据库（过程已经被封装起来了），
        * 再调用onCreate (SQLiteDatabase db)方法来执行创建表之类的操作。当数据库存在时，SQLiteOpenHelper 就不会
        * 调用onCreate (SQLiteDatabase db)方法了，它会检测版本号，若传入的版本号高于当前的，就会执行onUpgrade()方
        * 法来更新数据库和版本号。
        * */
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("-----------Create SQLiteDatabase----------");
         db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("----------onUpdate Called--------"+oldVersion+"--->"+newVersion);
    }

    /*public static void main(String[] args) {
        MyDataBaseHelper help = new MyDataBaseHelper(null,"testDateBase.db3",1);
    }*/
}
