package com.example.ganshenml.tomatoman.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ganshenml.tomatoman.R;

import java.util.ArrayList;

/**
 * Created by ganshenml on 2016/3/25.
 */
public class TomatoSettingAdapter extends BaseAdapter {
    private ArrayList<View> list;
    private Context context;

    public TomatoSettingAdapter(Context context, ArrayList<View> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.e("list size",String.valueOf(list.size()));
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = list.get(position);
        return view;
    }
}
