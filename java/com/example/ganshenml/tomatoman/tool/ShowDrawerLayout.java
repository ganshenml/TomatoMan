package com.example.ganshenml.tomatoman.tool;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ganshenml.tomatoman.R;

/**
 * Created by ganshenml on 2016/4/11.
 * 该类为各个页面显示DrawerLayout的静态方法类
 */
public class ShowDrawerLayout {
    public static void showDrawerLayout(Activity activity, int toolbarId, String toolbarTitle, int drawerLayoutId) {

        //显示汉堡菜单及为其设置事件
        Toolbar myToolbar = (Toolbar) activity.findViewById(toolbarId);
        myToolbar.setNavigationIcon(R.drawable.hamburger_menu);
        myToolbar.setTitle(toolbarTitle);
        final DrawerLayout drawer = (DrawerLayout) activity.findViewById(drawerLayoutId);

        //汉堡菜单初始化及设置点击监听事件:显示DrawLayout
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }
}
