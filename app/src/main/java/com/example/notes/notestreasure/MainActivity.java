package com.example.notes.notestreasure;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private ListView lv;
    private Button addNotes;
    private Button addGra;
    private Button addDoList;
    private Intent intent;

    private MemoAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("记事宝");
        toolbar.setSubtitle("———记录美好生活");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        initView();

    }
    //初始化绑定事件
    public void initView()
    {
        lv = (ListView) findViewById(R.id.list);
        addNotes = (Button) findViewById(R.id.memo);
        addDoList = (Button) findViewById(R.id.do_list);
        addGra = (Button) findViewById(R.id.diary);
        addNotes.setOnClickListener(this);
        addGra.setOnClickListener(this);
        addDoList.setOnClickListener(this);
        //创建数据库
        notesDB = new NotesDB(this,"notes.db",null,1);
        dbReader = notesDB.getWritableDatabase();

        Toast.makeText(this,"创建数据库成功",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.memo:
                intent = new Intent(getApplicationContext(),MemoActivity.class);
                break;
            case R.id.diary:
                intent = new Intent(getApplicationContext(),GraffitiActivity.class);
                break;
            case R.id.do_list:
                intent = new Intent(getApplicationContext(),DoListActivity.class);
                break;
        }
        startActivity(intent);
    }

    public void selectDB(){
        Cursor cursor =dbReader.query(NotesDB.TABLE_NAME,null,null,
                null,null,null,null);
        adapter = new MemoAdapter(this,cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
