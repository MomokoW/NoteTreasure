package com.example.notes.notestreasure;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取兼容低版本的ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("记事宝");
        toolbar.setSubtitle("———记录美好生活");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

    }

    //实现添加便笺按钮跳转事件，使用显示意图进行跳转
    public void addMemo(View v) {
        //参数1上下文，参数2跳转Activity的字节码
        Intent intent = new Intent(getApplicationContext(),MemoActivity.class);
        startActivity(intent);
    }

    //实现添加心情日记按钮跳转事件
    public void addDiary(View v){
        //参数1上下文，参数2跳转Activity的字节码
        Intent intent = new Intent(getApplicationContext(),DiaryActivity.class);
        startActivity(intent);
    }

    //实现添加待办事项按钮跳转事件
    public void addDoList(View v){
        //参数1上下文，参数2跳转Activity的字节码
        Intent intent = new Intent(getApplicationContext(),DoListActivity.class);
        startActivity(intent);
    }
}
