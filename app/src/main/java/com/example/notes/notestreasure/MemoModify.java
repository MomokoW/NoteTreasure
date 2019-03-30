package com.example.notes.notestreasure;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类的描述:本类用于对从主界面ListView处点击的Item进行显示和修改
 * 创建日期:2019/3/30 15:31
 */
public class MemoModify extends AppCompatActivity implements View.OnClickListener {

    //标签内容
    final String items[] = {"未标签", "生活", "个人", "旅游", "工作"};
    private String tagPast;
    private Button saveBtn, shareBtn, addTag, deleteBtn;
    private TextView timetv;
    private EditText ettext;
    private NotesDB notesDB;
    private String DateNow;
    private String timeNow;
    private String contentPast;
    private SQLiteDatabase dbWriter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_modify);
        initView();
    }

    public void initView() {
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("查看便笺");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);


        //初始化按钮和文本框
        saveBtn = (Button) findViewById(R.id.save);
        shareBtn = (Button) findViewById(R.id.share);
        deleteBtn = (Button) findViewById(R.id.delete);
        addTag = (Button) findViewById(R.id.tag);
        ettext = (EditText) findViewById(R.id.ettext);
        timetv = (TextView) findViewById(R.id.showtime);

        //监听按钮事件
        saveBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        addTag.setOnClickListener(this);

        //创建数据库对象
        notesDB = new NotesDB(this, "notes.db", null, 1);
        dbWriter = notesDB.getWritableDatabase();

        //获取从主页面传递过来的数据
        Intent intent = getIntent();
        id = intent.getIntExtra(NotesDB.ID, 0);
        contentPast = intent.getStringExtra(NotesDB.CONTENT);
        String timePast = intent.getStringExtra(NotesDB.TIME).substring(12);
        tagPast = intent.getStringExtra(NotesDB.TAG);

        //设置初始界面,时间和显示内容
        addTag.setText(tagPast);
        timetv.setText(timePast);
        ettext.setText(contentPast);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                SaveNotes();
                break;
            case R.id.delete:
                deleteNotes();
                break;
            case R.id.share:
                shareNotes();
                break;
            case R.id.tag:
                setTag();
                break;
        }
    }

    public void shareNotes() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        //指定参数类型
        intent.setType("text/plain");   //"image/png"
        //设置要分享的文本
        intent.putExtra(Intent.EXTRA_TEXT,ettext.getText().toString());
        startActivity(intent);
    }

    public void deleteNotes() {
        if(!(contentPast.equals(ettext.getText().toString())))
        {
            Toast.makeText(getApplicationContext(),"便笺已修改！暂未保存，不可删除！",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            dbWriter.delete(NotesDB.TABLE_NAME,"content=?",new String[]{contentPast});
            Toast.makeText(getApplicationContext(),"删除便笺成功!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void SaveNotes() {
        if(contentPast.equals(ettext.getText().toString())&&tagPast.equals(addTag.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"便笺无修改，不可更新!",Toast.LENGTH_LONG).show();
            return;
        }else{
            ContentValues cv = new ContentValues();
            //修改时获取当前修改时间
            getTime();
            cv.put(NotesDB.CONTENT,ettext.getText().toString());
            cv.put(NotesDB.TAG,addTag.getText().toString());
            cv.put(NotesDB.TIME,DateNow);
            timetv.setText(timeNow);
            String ID = String.valueOf(id);
            dbWriter.update(NotesDB.TABLE_NAME,cv,"_id=?",new String[]{ID});

            Toast.makeText(getApplicationContext(),"更新便笺信息成功!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //得到便笺的分类
    public void setTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择标签");
        //设置图标
        builder.setIcon(R.mipmap.icon_launcher);
        //设置单选按钮
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取出选择的条目
                String item = items[which];
                addTag.setText(item);
                //关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //获取创建时间
    public String getTime() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        DateNow = format1.format(date);
        timeNow = DateNow.substring(12);
        return DateNow;
    }

}
