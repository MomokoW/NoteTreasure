package com.example.notes.notestreasure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 类名：NotesDB
 * 类的描述：此类为操作便笺存储信息的类，用于添加，删除，更新便笺信息
 * 创建时间：2019/3/27 15:12
 */
public class NotesDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";     //便笺表表名
    public static final String TABLE_URL = "picture";    //涂鸦存储表名
    public static final String URL = "Path";              //涂鸦存储路径
    public static final String URL_NAME = "name";         //涂鸦保存时间
    public static final String CONTENT = "content";      //便笺内容
    public static final String ID = "_id";               //id,方便读取
    public static final String TAG = "tag";              //便笺的分类
    public static final String TIME = "time";            //便笺创建的时间

    public NotesDB(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TAG + " VARCHAR(20) NOT NULL," +CONTENT + " TEXT NOT NULL," + TIME +" TEXT NOT NULL)");
        db.execSQL("CREATE TABLE TABLE_URL(URL TEXT NOT NULL,URL_NAME TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
