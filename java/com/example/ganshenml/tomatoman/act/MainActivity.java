package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.fragment.HomeFragment;
import com.example.ganshenml.tomatoman.fragment.MyFriendsFragment;
import com.example.ganshenml.tomatoman.fragment.MyTomatoFragment;
import com.example.ganshenml.tomatoman.fragment.RankFragment;
import com.example.ganshenml.tomatoman.fragment.SettingFragment;
import com.example.ganshenml.tomatoman.util.ConstantCode;
import com.example.ganshenml.tomatoman.util.ImageViewUtils;
import com.example.ganshenml.tomatoman.util.ToFragmentPage;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar tbHome, tbMyTomato, tbMyFriends, tbRank, tbSetting;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    //    MenuItem nav_home,nav_MyTomato,nav_friends,nav_rank,nav_share,nav_setting;
    NavigationMenuItemView nav_home, nav_MyTomato, nav_friends, nav_rank, nav_share, nav_setting;
    ImageView ivPersonLogo;
    Toolbar[] toolbars;//储存所有的toolbar
    long exitTime = 0;//设定回退事件
    private TextView tvUserIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 Bmob SDK
        Bmob.initialize(this, ConstantCode.ApplicationID);

        initViews();

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //显示汉堡菜单及为其设置事件
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, tbHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //如果点击setting，则跳转至番茄设置的Activity进行设置
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, TomatoSettingAct.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //显示“首页”页面
            ToFragmentPage.toFragmentPage(this, R.id.rlHome, new HomeFragment(), getSupportFragmentManager(), tbHome, toolbars);

        } else if (id == R.id.nav_myTomato) {
            //显示“我的番茄”页面
            ToFragmentPage.toFragmentPage(this, R.id.rlHome, new MyTomatoFragment(), getSupportFragmentManager(), tbMyTomato, toolbars);

        } else if (id == R.id.nav_friends) {
            //显示“我的好友”页面
            ToFragmentPage.toFragmentPage(this, R.id.rlHome, new MyFriendsFragment(), getSupportFragmentManager(), tbMyFriends, toolbars);

        } else if (id == R.id.nav_rank) {
            //显示“排行”页面
            ToFragmentPage.toFragmentPage(this, R.id.rlHome, new RankFragment(), getSupportFragmentManager(), tbRank, toolbars);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {
            //显示“设置”页面
            ToFragmentPage.toFragmentPage(this, R.id.rlHome, new SettingFragment(), getSupportFragmentManager(), tbSetting, toolbars);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//退出程序FF
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return false;
            }
        } else {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }


    //------------------------------------------以下为自定义方法--------------------------------------------------------


    //初始化组件
    private void initViews() {

        tbHome = (Toolbar) findViewById(R.id.tbHome);
        setSupportActionBar(tbHome);

        //实例化
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tbMyTomato = (Toolbar) findViewById(R.id.tbMyTomato);
        tbMyFriends = (Toolbar) findViewById(R.id.tbMyFriends);
        tbRank = (Toolbar) findViewById(R.id.tbRank);
        tbSetting = (Toolbar) findViewById(R.id.tbSetting);
        toolbars = new Toolbar[]{tbHome, tbMyTomato, tbMyFriends, tbRank, tbSetting, tbSetting};


        View headView = navigationView.getHeaderView(0);
        ivPersonLogo = (ImageView) headView.findViewById(R.id.ivPersonLogo);

        //初始化个人简介
        tvUserIntroduction = (TextView) headView.findViewById(R.id.tvUserIntroduction);

        //显示侧边栏的用户头像(调用自定义工具类将bitmap转换成圆形)
        Bitmap bitmapPersonLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_person);
        ivPersonLogo.setImageBitmap(ImageViewUtils.getRoundedCornerBitmap(bitmapPersonLogo, 100));//用户头像尺寸暂定100 pixels

        //默认选中当前页面的选项
        navigationView.setNavigationItemSelectedListener(this);


        //默认显示HomeFragment
        ToFragmentPage.toFragmentPage(this, R.id.rlHome, new HomeFragment(), getSupportFragmentManager(), tbHome, toolbars);
        nav_home = (NavigationMenuItemView) navigationView.findViewById(R.id.nav_home);

        //默认选中导航首页项
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        //事件监听
        ivPersonLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserHomePageAct.class);//跳转至用户个人主页
                startActivity(intent);
            }
        });

        //简介事件监听——>先设定为跳入登录页
        tvUserIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginAct.class);
                startActivity(intent);
            }
        });

    }
}