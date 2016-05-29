package com.example.ganshenml.tomatoman.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ganshenml on 2016/4/8.
 * 实现该自定义ListView——>方便ScrollView在onMeasure的时候得到ListView设定的默认值，否则ScrollView滑动不起来
 */
public class SimpleListView extends ListView {
    public SimpleListView(Context context) {
        super(context);
    }

    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,//设定ListView一个默认值，否则包裹的ScrollView无法onMeasure
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
