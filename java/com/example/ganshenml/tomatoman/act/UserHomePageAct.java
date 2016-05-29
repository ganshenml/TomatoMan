package com.example.ganshenml.tomatoman.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.fragment.HomeFragment;
import com.example.ganshenml.tomatoman.util.ImageViewUtils;
import com.example.ganshenml.tomatoman.util.ToFragmentPage;

public class UserHomePageAct extends AppCompatActivity {
    ImageView ivUserLogoHomepage,ivMyTomatoHomepage,ivMyFriendsHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_person_home_page);

        initViews();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------以下为自定义方法--------------------------------------------------------

    private void initViews() {
        //设置Activity的Title
        getSupportActionBar().setTitle("个人主页");

        //初始并实例化化控件
        ivUserLogoHomepage = (ImageView) findViewById(R.id.ivUserLogoHomepage);
        Bitmap bitmapUserLogoHomePage = BitmapFactory.decodeResource(getResources(),R.drawable.logo_person);
        ivUserLogoHomepage.setImageBitmap(ImageViewUtils.getRoundedCornerBitmap(bitmapUserLogoHomePage,150));
        ivMyTomatoHomepage = (ImageView) findViewById(R.id.ivMyTomatoHomepage);
        ivMyFriendsHomepage = (ImageView) findViewById(R.id.ivMyFriendsHomepage);
        Toolbar tbHome = (Toolbar)findViewById(R.id.tbHome);
        final Toolbar tbMyTomato = (Toolbar) findViewById(R.id.tbMyTomato);
        Toolbar tbMyFriends = (Toolbar) findViewById(R.id.tbMyFriends);
        Toolbar tbRank = (Toolbar) findViewById(R.id.tbRank);
        Toolbar tbSetting = (Toolbar) findViewById(R.id.tbSetting);
        final Toolbar[] toolbars = new Toolbar[]{tbHome,tbMyTomato,tbMyFriends,tbRank,tbSetting,tbSetting};


        ivMyTomatoHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();//必须先结束当前Activity，让MainActivity重新得以resume才能进行一下fragment页面的替换
//                ToFragmentPage.toFragmentPage(UserHomePageAct.this, R.id.rlHome, new HomeFragment(), getSupportFragmentManager(), tbMyTomato, toolbars);
            }
        });

    }
}
