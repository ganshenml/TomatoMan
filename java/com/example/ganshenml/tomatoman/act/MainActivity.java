package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Extra;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.fragment.HomeFragment;
import com.example.ganshenml.tomatoman.fragment.MyFriendsFragment;
import com.example.ganshenml.tomatoman.fragment.MyTomatoFragment;
import com.example.ganshenml.tomatoman.fragment.RankFragment;
import com.example.ganshenml.tomatoman.fragment.SettingFragment;
import com.example.ganshenml.tomatoman.util.DbTool;
import com.example.ganshenml.tomatoman.util.LogTool;
import com.example.ganshenml.tomatoman.util.ToActivityPage;
import com.example.ganshenml.tomatoman.util.ToFragmentPage;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    private Context thisContext = MainActivity.this;

    private Toolbar tbHome, tbMyTomato, tbMyFriends, tbRank, tbSetting;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    //    MenuItem nav_home,nav_MyTomato,nav_friends,nav_rank,nav_share,nav_setting;
    private NavigationMenuItemView nav_home, nav_MyTomato, nav_friends, nav_rank, nav_share, nav_setting;
    private SimpleDraweeView user_log;
    private Toolbar[] toolbars;//储存所有的toolbar
    private long exitTime = 0;//设定回退事件
    private TextView usernameTv, tvUserIntroduction;

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

    private void initData() {
        //请求Extra表数据并保存至本地
        upDataExtraData();
    }

    //初始化组件
    private void initViews() {
        tbHome = (Toolbar) findViewById(R.id.tbHome);
        setSupportActionBar(tbHome);

        //显示汉堡菜单及为其设置事件
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, tbHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //实例化
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//默认选中当前页面的选项

        tbMyTomato = (Toolbar) findViewById(R.id.tbMyTomato);
        tbMyFriends = (Toolbar) findViewById(R.id.tbMyFriends);
        tbRank = (Toolbar) findViewById(R.id.tbRank);
        tbSetting = (Toolbar) findViewById(R.id.tbSetting);

        toolbars = new Toolbar[]{tbHome, tbMyTomato, tbMyFriends, tbRank, tbSetting, tbSetting};

        View headView = navigationView.getHeaderView(0);

        //初始化个人信息
        usernameTv = (TextView) headView.findViewById(R.id.usernameTv);
        tvUserIntroduction = (TextView) headView.findViewById(R.id.tvUserIntroduction);

        //头像初始化
        user_log = (SimpleDraweeView) headView.findViewById(R.id.user_log);
        user_log.setImageURI("http://pic3.zhongsou.com/image/380710317cdddeb894b.jpg");

        //默认显示HomeFragment
        ToFragmentPage.toFragmentPage(this, R.id.rlHome, new HomeFragment(), getSupportFragmentManager(), tbHome, toolbars);
//        nav_home = (NavigationMenuItemView) navigationView.findViewById(R.id.nav_home);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);//默认选中导航首页项

       }

    private void initDataViews() {
        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
        Person person = BmobUser.getCurrentUser(Person.class);
        if (person == null) {
            finish();
            ToActivityPage.turnToSimpleAct(MainActivity.this, LoginAct.class);
            return;
        }
        usernameTv.setText(person.getUsername());
        String picUrlTemp = person.getImageId();
//        if (!StringTool.isEmpty(picUrlTemp)) {
//            LogTool.log(LogTool.Aaron, "本地用图片不为空");
//            user_log.setImageURI(picUrlTemp);
//        }
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

    }

    /**
     * 请求Extra数据并保存至本地
     */
    private void upDataExtraData() {
        BmobQuery<Extra> extraBmobQuery = new BmobQuery<>();
        extraBmobQuery.findObjects(new FindListener<Extra>() {
            @Override
            public void done(List<Extra> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        DbTool.saveExtraData(list.get(0));
                        LogTool.log(LogTool.Aaron, TAG + " upDataExtraData 查询到了数据并进行本地保存");

                    }
                } else {
                    LogTool.log(LogTool.Aaron, TAG + " upDataExtraData 查询Extra数据出错： " + e.toString());
                }
            }
        });
    }
}
