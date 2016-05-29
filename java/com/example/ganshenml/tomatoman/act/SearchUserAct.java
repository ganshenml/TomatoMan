package com.example.ganshenml.tomatoman.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;

/*
搜索用户的界面
 */
public class SearchUserAct extends AppCompatActivity {
    private RelativeLayout rlSearchUser;
    private SearchView svSearchFriends;
    private Toolbar tbSearchUser;
    private ImageView ivSearchUserDivider;
    private TextView tvSearchContent;//"下面布局"中文本组件对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initViews();
        listenerMethod();
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

    //-----------------------------------------以下为自定义方法----------------------------------

    private void initViews() {
        tbSearchUser = (Toolbar) findViewById(R.id.tbSearchUser);
        setSupportActionBar(tbSearchUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        svSearchFriends = (SearchView) findViewById(R.id.svSearchFriends);

        rlSearchUser = (RelativeLayout) findViewById(R.id.rlSearchUser);
        ivSearchUserDivider = (ImageView) findViewById(R.id.ivSearchUserDivider);

        tvSearchContent = (TextView) findViewById(R.id.tvSearchContent);
    }

    private void listenerMethod() {

        svSearchFriends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {//当搜索框文本内容不为空时，则显示下面的“搜索布局”，否则隐藏
                    rlSearchUser.setVisibility(View.INVISIBLE);
                    ivSearchUserDivider.setVisibility(View.INVISIBLE);
                } else {
                    rlSearchUser.setVisibility(View.VISIBLE);
                    ivSearchUserDivider.setVisibility(View.VISIBLE);
                }

                //对“搜索布局”中的文本内容及颜色做即时显示处理
                tvSearchContent.setText(Html.fromHtml("<font>搜索：</font><font color=\"#3CB371\">"+newText+"</font>"));
                return false;
            }
        });

        rlSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchUserAct.this, "点击搜索", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
