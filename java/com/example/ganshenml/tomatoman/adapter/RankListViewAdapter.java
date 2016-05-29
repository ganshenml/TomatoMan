package com.example.ganshenml.tomatoman.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganshenml on 2016/4/6.
 */
public class RankListViewAdapter extends BaseAdapter {
    List<User> list = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    ViewHolder viewHolder;

    public RankListViewAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.rank_list_item, null);
            viewHolder.tvRankId = (TextView) convertView.findViewById(R.id.tvRankId);
            viewHolder.ivRankUserLogo = (ImageView) convertView.findViewById(R.id.ivRankUserLogo);
            viewHolder.tvRankUserName = (TextView) convertView.findViewById(R.id.tvRankUserName);
            viewHolder.tvRankTomatoNum = (TextView) convertView.findViewById(R.id.tvRankTomatoNum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = list.get(position);
        viewHolder.tvRankId.setText(String.valueOf(user.getUserId()));
        //对前三名的排行数字样式做处理
        operateTop3Rank(position);
        viewHolder.ivRankUserLogo.setImageResource(user.getImageId());
        viewHolder.tvRankUserName.setText(user.getUserName());
        viewHolder.tvRankTomatoNum.setText(user.getUserTomatoNum() + "个，高效时间55分钟");
        Log.e("positon", String.valueOf(position));
        return convertView;
    }


    //--------------------------------------------------以下为自定义方法或类----------------------------------------------

    //对前三名的排行数字样式做处理
    private void operateTop3Rank(int position) {
        //如果当前项是前三项，则使用特殊颜色，否则统一用另外一种颜色
        if (position == 0) {
            viewHolder.tvRankId.setTextColor(Color.parseColor("#FF3030"));
            viewHolder.tvRankId.setTextSize(18);
        } else if (position == 1) {
            viewHolder.tvRankId.setTextColor(Color.parseColor("#FF4500"));
            viewHolder.tvRankId.setTextSize(18);
        } else if (position == 2) {
            viewHolder.tvRankId.setTextColor(Color.parseColor("#FFA54F"));
            viewHolder.tvRankId.setTextSize(18);
        } else {
            viewHolder.tvRankId.setTextColor(Color.BLACK);
            viewHolder.tvRankId.setTextSize(15);
        }
    }

    class ViewHolder {
        TextView tvRankId;
        ImageView ivRankUserLogo;
        TextView tvRankUserName;
        TextView tvRankTomatoNum;
    }
}
