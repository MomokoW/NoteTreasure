package com.example.notes.notestreasure;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.text.*;

public class GraffitiActivity extends AppCompatActivity implements View.OnTouchListener{

    private ImageView iv;
    private Bitmap bitmap;
    private Canvas canvas;
    //起始坐标
    private int startX;
    private int startY;
    private Paint paint;
    NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    final String items[] = {"红色","蓝色","绿色","灰色","黄色","黑色"};
    private String tag = "红色";
    private String PNmae;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graffiti);
        //实例化ImageView对象
        iv = (ImageView) findViewById(R.id.iv);
        //给ImageView设置触摸事件监听
        iv.setOnTouchListener(this);
        //创建一个画笔对象
        paint = new Paint();
        //创建数据库
        notesDB = new NotesDB(this,"notes.db",null,1);
        dbWriter= notesDB.getWritableDatabase();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        //判断动作类型
        switch (event.getAction()){
            //手指按下事件
            case MotionEvent.ACTION_DOWN:
                //如果当前bitmap为空
                if(bitmap==null){
                    //创建一个新的bitmap对象，宽、高使用界面布局中，ImageView对象的宽、高
                    bitmap = Bitmap.createBitmap(iv.getWidth(),iv.getHeight(),Bitmap.Config.ARGB_8888);
                    //根据bitmap对象创建一个画布
                    canvas = new Canvas(bitmap);
                    //设置画布背景色为白色
                    canvas.drawColor(Color.WHITE);

                    //设置画笔的颜色为红色，线条粗细为5磅
                    //paint.setColor(Color.RED);
                    paint.setStrokeWidth(5);
                }
                //记录手指按下时的屏幕坐标
                startX = (int)event.getX();
                startY = (int)event.getY();
            case MotionEvent.ACTION_MOVE:    //手指滑动事件
                //记录移动的位置坐标
                int moveX = (int)event.getX();
                int moveY = (int)event.getY();
                //绘制线条，连接起始位置和当前位置
                canvas.drawLine(startX,startY,moveX,moveY,paint);
                //在ImageView中显示bitmap
                iv.setImageBitmap(bitmap);
                //将起始位置改变为当前移动到的位置
                startX = moveX;
                startY = moveY;
                break;
            default:
                break;
        }
        return true;
    }
    //清除界面
    public void clear(View view){
        bitmap = null;
        iv.setImageBitmap(null);
    }
    public void Color(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("选择颜色");
        //设置图标
        builder.setIcon(R.mipmap.icon_launcher);
        //设置单选按钮
        builder.setSingleChoiceItems(items,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取出选择的条目
                String item = items[which];
                tag = item;
                switch(tag){
                    case "红色":
                        paint.setColor(Color.RED);
                        break;
                    case "蓝色":
                        paint.setColor(Color.BLUE);
                        break;
                    case "绿色":
                        paint.setColor(Color.GREEN);
                        break;
                    case "灰色":
                        paint.setColor(Color.GRAY);
                        break;
                    case "黄色":
                        paint.setColor(Color.YELLOW);
                        break;
                    case "黑色":
                        paint.setColor(Color.BLACK);
                        break;
                     default:break;
                }
                //关闭对话框
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    //将当前绘制的图形保存到文件
    public void save(View view){
        if(bitmap==null){
            Toast.makeText(this,"没有图片可以保存",Toast.LENGTH_SHORT).show();
            return ;
        }
        //创建一个文件对象，为了防止重名，用事件戳，命名
        PNmae = "pic"+System.currentTimeMillis()+".jpg";
        File file = new File(getFilesDir(),PNmae);

        ContentValues cv = new ContentValues();


        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            //以JPEG的图片格式将当前图片以流的形式输出
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            if(compress){
                Toast.makeText(this,"保存成功"+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                cv.put(NotesDB.URL_NAME,PNmae);
                cv.put(NotesDB.URL,file.getAbsolutePath());
                dbWriter.insert(NotesDB.TABLE_URL,null,cv);
            }else{
                Toast.makeText(this,"保存失败"+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"保存失败"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }finally {
            if(stream!=null){
                try{
                    stream.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

    }
}