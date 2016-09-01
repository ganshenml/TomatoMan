package com.example.ganshenml.tomatoman.act;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.bean.data.PushMessage;
import com.example.ganshenml.tomatoman.bean.data.StaticData;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.net.NetRequest;
import com.example.ganshenml.tomatoman.receiver.MyPushMessageReceiver;
import com.example.ganshenml.tomatoman.tool.BmobTool;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.ConstantCode;
import com.example.ganshenml.tomatoman.tool.ContextManager;
import com.example.ganshenml.tomatoman.tool.DbTool;
import com.example.ganshenml.tomatoman.tool.GBlurPic;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.NotificationUtls;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.SpTool;
import com.example.ganshenml.tomatoman.tool.StringTool;
import com.example.ganshenml.tomatoman.tool.ThreadTool;
import com.example.ganshenml.tomatoman.tool.ToActivityPage;
import com.example.ganshenml.tomatoman.view.ClearEditTextView;
import com.example.ganshenml.tomatoman.view.StartCountTimeCircleView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    private Context thisContext = MainActivity.this;

    private StartCountTimeCircleView startCountTimeCircleViewId;
    private ClearEditTextView etTaskName;
    private LinearLayout llHomeFragment, LLNavHeader, redPointLl;//红点提示（3）

    private DrawerLayout drawer;
    private Toolbar tbHome;

    private ActionBarDrawerToggle toggle;
    private NavigationMenuItemView nav_home, nav_MyTomato, nav_friends, nav_rank, nav_share, nav_setting;
    private SimpleDraweeView user_log;
    private long exitTime = 0;//设定回退事件
    private TextView usernameTv, tvUserIntroduction;
    private ImageView hamburgerMenuIv, tomatoSettingIv;

    private GBlurPic mGBlurPic;

    private Bitmap mBitmapIn;
    private Bitmap mBitmapOut;

    private String latestCreatedAtStr;//最新消息的创建时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BmobInstallation.getCurrentInstallation().save();
//        // 启动推送服务
//        BmobPush.startWork(MainActivity.this);

        initViews();
        initData();
        initDataViews();
        initListeners();

        ContextManager.addContext(this);
        //重置之前的完成的番茄个数和高效时间
        CommonUtils.resetSpData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //如果网络顺畅，则将本地未上传的TomatoRecordT数据进行上传，返回成功后并更新本地数据的objectId和createdAt字段
        if (CommonUtils.judgeNetWork(this)) {//先假定网络顺畅
            ThreadTool.runOnNewThread(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.uploadRestTomatoRecordTData(null);
                }
            });

            //更新通知消息（对比后确定是否显示通知样式）
            final String timeStr = SpTool.getString(StaticData.SPPUSHMESSAGE, "");
            NetRequest.returnLatestMessage(new HttpCallback<PushMessage>() {
                @Override
                public void onSuccess(List<PushMessage> data) {
                    super.onSuccess(data);
                    if (StringTool.hasData(data)) {
                        PushMessage pushMessageTemp = data.get(0);
                        LogTool.log(LogTool.Aaron,"请求消息通知结果内容为： "+pushMessageTemp.getContent());

                        latestCreatedAtStr = pushMessageTemp.getCreatedAt();
                        if (pushMessageTemp.getUsing() && (StringTool.isEmpty(latestCreatedAtStr) ||
                                (latestCreatedAtStr.compareToIgnoreCase(timeStr) > 0
                                        && latestCreatedAtStr.compareToIgnoreCase(CommonUtils.getCurrentDataAndTime()) > 0))) {//如果当前消息状态为正常，截止时间为空或者不为空时大于当前时间和本地保存的上一次已查看消息对应的创建时间
                            if (CommonUtils.getCurrentDataAndTime().compareToIgnoreCase(pushMessageTemp.getPushTime()) >= 0)//如果当前事件等于或超过发布时间
//                            SpTool.putString(StaticData.SPPUSHMESSAGE,latestCreatedAtStr);
                                showPutMessage(data.get(0));
                        }
                    }else {
                        LogTool.log(LogTool.Aaron,"请求消息通知结果为空");
                    }
                }
            });
        }

        showPersonInfoView();
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
            toShareApp();

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

        redPointLl = (LinearLayout) navigationView.getMenu().findItem(R.id.nav_setting).getActionView().findViewById(R.id.redPointLl);
    }


    //    @TargetApi(Build.VERSION_CODES.M)
    private void initDataViews() {

        //是否有新版本——>有（红点提示）
        if (CommonUtils.isHasNewVersion(MainActivity.this)) {
            redPointLl.setVisibility(View.VISIBLE);
        }

        showPersonInfoView();//展示用户头像、名称、简介等信息
    }

    private void initListeners() {

        //事件监听
        user_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Person.getCurrentUser() == null){
//                    ShowDialogUtils.showSimpleHintDialog(MainActivity.this, "登录后才能查看个人主页，去登录？", new HttpCallback() {
//                        @Override
//                        public void onComplete(Object data) {
//                            super.onComplete(data);
//                            ToActivityPage.turnToSimpleAct(MainActivity.this,LoginAct.class);
//                            finish();
//                        }
//                    });
//                    return;
//                }

                Intent intent = new Intent(MainActivity.this, UserHomePageAct.class);//跳转至用户个人主页
                startActivity(intent);
            }
        });

        startCountTimeCircleViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//1.样式变换；2.notification开启；3.finish当前activity；4.跳转至下一个activity并传递数据
                //保存任务名称至sp
                String taskNameStr = etTaskName.getText().toString();
                LogTool.log(LogTool.Aaron, "MainActivity initListeners 任务名称是： " + taskNameStr);
                SpTool.putString(StaticData.SPTASKNAME, taskNameStr == null ? "" : taskNameStr);

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

    /**
     * 构造Bitmap对象
     *
     * @param resource
     * @return
     */
    private Bitmap loadBitmap(int resource) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(), resource, options);
    }

    /**
     * 分享这款app（网页下载地址）
     */
    private void toShareApp() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://tomatoman.bmob.cn/");
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享软件下载地址到"));
    }

    /**
     * 显示推送消息的样式
     *
     * @param pushMessage
     */
    private void showPutMessage(PushMessage pushMessage) {
        TextView textView = new TextView(this);
        textView.setText(pushMessage.getContent());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundColor(getResources().getColor(R.color.custom_blue));
        textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
        lp.setMargins(0, 0, 0, 150);
        textView.setLayoutParams(lp);

        llHomeFragment.addView(textView);

        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f);
        scaleAnimation.setDuration(1500);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(scaleAnimation);

        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,300,0);
        translateAnimation.setDuration(1500);
        translateAnimation.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(translateAnimation);

        textView.startAnimation(animationSet);
    }

    /**
     * 展示用户头像、名称、简介等信息
     */
    private void showPersonInfoView(){
        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
        Person person = BmobUser.getCurrentUser(Person.class);
        if (person == null) {
            ToActivityPage.turnToSimpleAct(MainActivity.this, LoginAct.class);
            finish();
            return;
        }
        usernameTv.setText(person.getUsername());
        tvUserIntroduction.setText(person.getIntroduction());
        String picUrlTemp = person.getImageId();
        if (!StringTool.isEmpty(picUrlTemp)) {
            LogTool.log(LogTool.Aaron, "本地用图片不为空");
            user_log.setImageURI(picUrlTemp);
        }
    }
}
