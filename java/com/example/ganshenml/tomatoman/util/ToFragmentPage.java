package com.example.ganshenml.tomatoman.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.fragment.HomeFragment;
import com.example.ganshenml.tomatoman.fragment.MyFriendsFragment;
import com.example.ganshenml.tomatoman.fragment.MyTomatoFragment;
import com.example.ganshenml.tomatoman.fragment.RankFragment;
import com.example.ganshenml.tomatoman.fragment.SettingFragment;

/**
 * Created by ganshenml on 2016/3/26.
 */
public class ToFragmentPage {
    //通过左侧抽屉导航，点击显示不同的Fragment页面
    public static void toFragmentPage(Context context, int rId, Fragment myFragment, FragmentManager manager, Toolbar toolbar,Toolbar[] toolbars) {

        //根据不同的Fragment页面显示正确的toolbar
        indicateRightToolBar(myFragment,toolbar,toolbars);


        if (myFragment != null) {
            FragmentManager fm = manager;
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(rId, myFragment);
//            ft.addToBackStack(null);
//            ft.commit();
            ft.commitAllowingStateLoss();
        } else {
            Log.e("fragment", "ToFragmentPage__toFragmentPage is null");
        }
    }


    //根据不同的Fragment页面显示正确的toolbar
    private static void indicateRightToolBar(Fragment myFragment, Toolbar toolbar, Toolbar[] toolbars) {
        Toolbar myToolbar = toolbar;
        Toolbar[] myToolbars = toolbars;

        if(myFragment==null){
            return;
        }


        //判断传递过来的fragment是哪一个
        if (myFragment instanceof HomeFragment) {
            if(myToolbar.getVisibility()==View.INVISIBLE)
            myToolbar.setVisibility(View.VISIBLE);
            for (int i = 0; i < toolbars.length; i++) {
                if(toolbars[i]!=myToolbar){
                    toolbars[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else if (myFragment instanceof MyTomatoFragment) {
            if(myToolbar.getVisibility()==View.INVISIBLE)
                myToolbar.setVisibility(View.VISIBLE);
            for (int i = 0; i < toolbars.length; i++) {
                if(toolbars[i]!=myToolbar){
                    toolbars[i].setVisibility(View.INVISIBLE);
                }
            }
       } else if (myFragment instanceof MyFriendsFragment) {
            if (myToolbar.getVisibility() == View.INVISIBLE)
                myToolbar.setVisibility(View.VISIBLE);
            for (int i = 0; i < toolbars.length; i++) {
                if (toolbars[i] != myToolbar) {
                    toolbars[i].setVisibility(View.INVISIBLE);
                }
            }
        } else if (myFragment instanceof RankFragment) {
            if (myToolbar.getVisibility() == View.INVISIBLE)
                myToolbar.setVisibility(View.VISIBLE);
            for (int i = 0; i < toolbars.length; i++) {
                if (toolbars[i] != myToolbar) {
                    toolbars[i].setVisibility(View.INVISIBLE);
                }
            }
        } else if (myFragment instanceof SettingFragment) {
            if (myToolbar.getVisibility() == View.INVISIBLE)
                myToolbar.setVisibility(View.VISIBLE);
            for (int i = 0; i < toolbars.length; i++) {
                if (toolbars[i] != myToolbar) {
                    toolbars[i].setVisibility(View.INVISIBLE);
                }
            }
        }

    }
}
