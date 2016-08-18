package com.example.ganshenml.tomatoman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.ganshenml.tomatoman.R;

/**
 * Created by ganshenml on 2016-08-17.
 */
public class CountTimeTextView extends TextView {

    public CountTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setShadowLayer(3,3,3, ContextCompat.getColor(context, R.color.white));
        setTextSize(28);
        setTextColor(ContextCompat.getColor(context,R.color.dark_yellow));
    }

}
