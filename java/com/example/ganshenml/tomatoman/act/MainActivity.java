package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.util.ConstantCode;
import com.example.ganshenml.tomatoman.util.DbTool;
import com.example.ganshenml.tomatoman.util.ImageTool;
import com.example.ganshenml.tomatoman.util.LogTool;
import com.example.ganshenml.tomatoman.util.NotificationUtls;
import com.example.ganshenml.tomatoman.util.StringTool;
import com.example.ganshenml.tomatoman.util.ToActivityPage;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;
import com.example.ganshenml.tomatoman.view.StartCountTimeCircleView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.BitmapMemoryCacheFactory;

import org.litepal.LitePalApplication;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    private Context thisContext = MainActivity.this;

    private StartCountTimeCircleView startCountTimeCircleViewId;
    private ClearEditTextView etTaskName;
    private LinearLayout llHomeFragment, LLNavHeader;

    private DrawerLayout drawer;
    private Toolbar tbHome;

    private ActionBarDrawerToggle toggle;
    private NavigationMenuItemView nav_home, nav_MyTomato, nav_friends, nav_rank, nav_share, nav_setting;
    private SimpleDraweeView user_log;
    private long exitTime = 0;//设定回退事件
    private TextView usernameTv, tvUserIntroduction;
    private ImageView hamburgerMenuIv, tomatoSettingIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        initDataViews();
        initListeners();
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
        } else if (id == R.id.nav_myTomato) {
            //显示“我的番茄”页面
            Intent intent = new Intent(MainActivity.this, MyTomatoAct.class);
            startActivityForResult(intent, StaticData.REQUEST_TO_MYTOMATO_ACT);

        } else if (id == R.id.nav_friends) {
            //显示“我的好友”页面
            Intent intent = new Intent(MainActivity.this, MyFriendsAct.class);
            startActivityForResult(intent, StaticData.REQUEST_TO_MYFRIENDS_ACT);
        } else if (id == R.id.nav_rank) {
            //显示“排行”页面
            Intent intent = new Intent(MainActivity.this, RankAct.class);
            startActivityForResult(intent, StaticData.REQUEST_TO_RANK_ACT);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {
            //显示“设置”页面
            Intent intent = new Intent(MainActivity.this, SettingAct.class);
            startActivityForResult(intent, StaticData.REQUEST_TO_SETTING_ACT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            default:
                drawer.openDrawer(GravityCompat.START);
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);//默认选中导航首页项
                break;
        }
    }


    //------------------------------------------以下为自定义方法--------------------------------------------------------

    private void initData() {
        //请求Extra表数据并保存至本地
        DbTool.upDataExtraData();
    }

    //初始化组件
    private void initViews() {
        tbHome = (Toolbar) findViewById(R.id.tbHome);
        setSupportActionBar(tbHome);

        //显示汉堡菜单及为其设置事件
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //实例化
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//默认选中当前页面的选项

        View headView = navigationView.getHeaderView(0);

        //初始化个人信息
        usernameTv = (TextView) headView.findViewById(R.id.usernameTv);
        tvUserIntroduction = (TextView) headView.findViewById(R.id.tvUserIntroduction);

        //头像初始化
        user_log = (SimpleDraweeView) headView.findViewById(R.id.user_log);

        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);//默认选中导航首页项

        startCountTimeCircleViewId = (StartCountTimeCircleView) findViewById(R.id.startViewId);
        etTaskName = (ClearEditTextView) findViewById(R.id.etTaskName);
        llHomeFragment = (LinearLayout) findViewById(R.id.llHomeFragment);

        hamburgerMenuIv = (ImageView) findViewById(R.id.hamburgerMenuIv);
        tomatoSettingIv = (ImageView) findViewById(R.id.tomatoSettingIv);

        LLNavHeader = (LinearLayout) headView.findViewById(R.id.LLNavHeader);
    }


    private void initDataViews() {
        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
        Person person = BmobUser.getCurrentUser(Person.class);
        if (person == null) {
            ToActivityPage.turnToSimpleAct(MainActivity.this, LoginAct.class);
            finish();
            return;
        }
        usernameTv.setText(person.getUsername());
        String picUrlTemp = person.getImageId();
        if (!StringTool.isEmpty(picUrlTemp)) {
            LogTool.log(LogTool.Aaron, "本地用图片不为空");
            user_log.setImageURI(picUrlTemp);
        }

        //初始化背景图片
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.background);
//        Bitmap bitmap1 = ImageTool.doBlur(bitmap, 20, true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        LLNavHeader.setBackground(drawable);
    }

    private void initListeners() {

        //事件监听
        user_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserHomePageAct.class);//跳转至用户个人主页
                startActivity(intent);
            }
        });

        startCountTimeCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//1.样式变换；2.notification开启；3.finish当前activity；4.跳转至下一个activity并传递数据
                //1.样式：设置背景alpha变化以下
                v.setAlpha(0.5f);

                //2.notification开启
                Intent intent = new Intent(MainActivity.this, TomatoCountTimeAct.class);
                NotificationUtls.sendNotification(MainActivity.this, ConstantCode.HOMEFRAGMETN_REQUEST_CODE, intent, R.layout.notification_layout);//调用自定义工具类方法发送notification

                //3.finish当前主activity
                finish();

                //4.逻辑：跳转至下一个Activity
                startActivity(intent);
            }
        });

        //判断软键盘是否打开——>如果打开，则在点击界面后进行隐藏
        llHomeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(llHomeFragment.getWindowToken(), 0);
                }
            }
        });

        hamburgerMenuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        tomatoSettingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TomatoSettingAct.class);
                startActivity(intent);
            }
        });
    }

}
