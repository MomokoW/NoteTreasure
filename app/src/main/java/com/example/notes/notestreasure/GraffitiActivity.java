package com.example.notes.notestreasure;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
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

public class GraffitiActivity extends AppCompatActivity implements View.OnTouchListener{

    private ImageView iv;
    private Bitmap bitmap;
    private Canvas canvas;
    //起始坐标
    private int startX;
    private int startY;
    private Paint paint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graffiti);
        //实例化ImageView对象
        iv = (ImageView) findViewById(R.id.iv);
        //给ImageView设置触摸事件监听
        iv.setOnTouchListener(this);
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
                    //创建一个画笔对象
                    paint = new Paint();
                    //设置画笔的颜色为红色，线条粗细为5磅
                    paint.setColor(Color.RED);
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
    //将当前绘制的图形保存到文件
    public void save(View view){
        if(bitmap==null){
            Toast.makeText(this,"没有图片可以保存",Toast.LENGTH_SHORT).show();
            return ;
        }
        //创建一个文件对象，为了防止重名，用事件戳，命名
        File file = new File(getFilesDir(),"pic"+System.currentTimeMillis()+".jpg");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            //以JPEG的图片格式将当前图片以流的形式输出
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            if(compress){
                Toast.makeText(this,"保存成功"+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
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