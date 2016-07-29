package com.example.ganshenml.tomatoman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.TomatoRecord;
import com.example.ganshenml.tomatoman.bean.beant.TomatoRecordT;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganshenml on 2016-07-28.
 */
public class TomatoRecordAdapter extends BaseAdapter {
    private Context context;
    private List<TomatoRecordT> tomatoRecordArrayList;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public TomatoRecordAdapter(Context context, List<TomatoRecordT> tomatoRecordArrayList) {
        this.context = context;
        this.tomatoRecordArrayList = tomatoRecordArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tomatoRecordArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return tomatoRecordArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_item_mytomato, null);
            viewHolder.tomatoObtainLeverSv = (SimpleDraweeView) convertView.findViewById(R.id.tomatoObtainLeverSv);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
            viewHolder.weekTv = (TextView) convertView.findViewById(R.id.weekTv);
            viewHolder.myTomatoNumTv = (TextView) convertView.findViewById(R.id.myTomatoNumTv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TomatoRecordT tomatoRecord = tomatoRecordArrayList.get(position);
        int tomatoNumTemp = tomatoRecord.getTomatoNum();
        showTomatoObtainLeverImage(viewHolder, tomatoNumTemp);
        viewHolder.dateTv.setText(tomatoRecord.getTaskTime());
        viewHolder.weekTv.setText("( " + tomatoRecord.getWeek() + " )");
        viewHolder.myTomatoNumTv.setText(tomatoNumTemp + "个");
        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView tomatoObtainLeverSv;
        TextView dateTv;
        TextView weekTv;
        TextView myTomatoNumTv;
    }

    private void showTomatoObtainLeverImage(ViewHolder viewHolder, int tomatoNum) {
        if (tomatoNum <= 3) {
            viewHolder.tomatoObtainLeverSv.setImageURI("res://com.example.ganshenml.tomatoman/" + R.mipmap.tomato_green);
        } else if (tomatoNum <= 6) {
            viewHolder.tomatoObtainLeverSv.setImageURI("res://com.example.ganshenml.tomatoman/" + R.mipmap.tomato_orange);
        } else if (tomatoNum > 6) {
            viewHolder.tomatoObtainLeverSv.setImageURI("res://com.example.ganshenml.tomatoman/" + R.mipmap.tomato_red);
        }
    }
}
