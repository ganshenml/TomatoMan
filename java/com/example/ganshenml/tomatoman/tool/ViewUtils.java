package com.example.ganshenml.tomatoman.tool;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.act.TomatoCompleteAct;
import com.example.ganshenml.tomatoman.act.TomatoCountTimeAct;
import com.example.ganshenml.tomatoman.act.TomatoEfficiencyAct;
import com.example.ganshenml.tomatoman.act.TomatoRestAct;
import com.example.ganshenml.tomatoman.act.TomatoTemporaryAct;

/**
 * Created by ganshenml on 2016/4/21.
 * 专门用来设置View的工具类，含：toolbar的设置
 */
public class ViewUtils {

    //设置toolbar
    public static void setToolbar(Context activity, Toolbar tbToolbar_public, TextView tvTitle_public) {
        if(activity == null || tbToolbar_public == null | tvTitle_public ==null){
            return;
        }

        if (activity instanceof TomatoCompleteAct) {  //如果是“番茄完成页”
            tvTitle_public.setText("番茄数据");
            tvTitle_public.setTextSize(24);
            tbToolbar_public.findViewById(R.id.rightIv).setVisibility(View.VISIBLE);
            tbToolbar_public.getBackground().setAlpha(0);
        } else if (activity instanceof TomatoCountTimeAct) {//如果是“番茄工作计时页
            tvTitle_public.setText("番茄工作中");
        }else if (activity instanceof TomatoTemporaryAct) {//如果是“番茄临时时页（由一次番茄工作完成转临时）
            tvTitle_public.setText("番茄驿站");
            tbToolbar_public.getBackground().setAlpha(0);
        }else if (activity instanceof TomatoRestAct) {//如果是“番茄休息计时页
            tvTitle_public.setText("番茄休息中");
            tbToolbar_public.setBackgroundColor(ContextCompat.getColor(activity,R.color.lighter_green));
        }else if (activity instanceof TomatoEfficiencyAct) {//如果是“番茄高效领域计时页
            tvTitle_public.setText("高效工作中");
            tbToolbar_public.setBackgroundColor(ContextCompat.getColor(activity,R.color.yellow));
        }
        tvTitle_public.setTextColor(Color.WHITE);
    }
}
