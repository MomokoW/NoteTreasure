package com.example.notes.notestreasure;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {

    final String items[] = {"未标签","生活","个人","旅游","工作"};
    private String tag = "未标签";
    private Button saveBtn,deleteBtn,backBtn,addTag;
    private EditText ettext;
    NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        //初始化按钮
        saveBtn = (Button)findViewById(R.id.save);
        deleteBtn = (Button)findViewById(R.id.delete);
        backBtn = (Button)findViewById(R.id.goback);
        addTag = (Button)findViewById(R.id.tag);
        ettext = (EditText)findViewById(R.id.ettext);

        //监听按钮事件
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        addTag.setOnClickListener(this);

        //创建数据库对象
        dbWriter = getDataBase();


    }

    //响应按钮事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                addNotes();
                break;
            case R.id.delete:
                deleteNotes();
                break;
            case R.id.goback:
                dbWriter.close();
                finish();
                break;
            case R.id.tag:
                setTag();
                break;

        }

    }
    //获取数据库对象
    public SQLiteDatabase getDataBase() {
        notesDB = new NotesDB(this,"notes.db",null,1);
        return notesDB.getWritableDatabase();
    }

    //添加便笺数据
    public void addNotes() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT,ettext.getText().toString());
        cv.put(NotesDB.TAG,tag);
        cv.put(NotesDB.TIME,getTime());
        dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
        Toast.makeText(getApplicationContext(),"添加便笺成功!",Toast.LENGTH_LONG).show();
    }

    //获取创建时间
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }

    //删除当前便笺
    public void deleteNotes()
    {
        String content = ettext.getText().toString();
        dbWriter.delete(NotesDB.TABLE_NAME,"content=?",new String[]{content});
        Toast.makeText(getApplicationContext(),"删除便笺成功!",Toast.LENGTH_LONG).show();
    }

    //得到便笺的分类
    public void setTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择标签");
        //设置图标
        builder.setIcon(R.mipmap.icon_launcher);
        //设置单选按钮
        builder.setSingleChoiceItems(items,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取出选择的条目
                String item = items[which];
                tag = item;
                //关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

}
