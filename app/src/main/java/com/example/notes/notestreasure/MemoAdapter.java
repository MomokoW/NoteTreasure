package com.example.notes.notestreasure;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MemoAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public MemoAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();

    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.activity_memo,null);
        TextView contenttv = (TextView) layout.findViewById(R.id.ettext);
        TextView timetv =(TextView) layout.findViewById(R.id.time);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        contenttv.setText(content);
        timetv.setText(time);
        return layout;
    }
}
