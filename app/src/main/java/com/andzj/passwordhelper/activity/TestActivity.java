package com.andzj.passwordhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.adapter.MyViewPagerAdapter;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2017/1/21.
 */

public class TestActivity extends MyAppCompatActivity implements View.OnClickListener {

    public static String TAG = "TestActivity";

    private ViewPager viewPager;
    private View accountPasswordInfoLayout;
    private View accountPasswordHistoryInfoLayout;
    private View accountPasswordHistoryInfoLayout2;
    private List<View> viewList = new ArrayList<>();
    private MyViewPagerAdapter viewPagerAdapter;
    private static final int MODE_VIEW = 21;
    private static final int MODE_EDIT = 22;
    private int  currentMode = MODE_VIEW;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        accountPasswordInfoLayout =   getLayoutInflater().inflate(R.layout.layout_account_password_detail_info,null);

        accountPasswordHistoryInfoLayout = getLayoutInflater().inflate(R.layout.layout_account_password_detail_history_info,null);

        accountPasswordHistoryInfoLayout2 = getLayoutInflater().inflate(R.layout.layout_account_password_detail_history_info,null);


        viewList.add(accountPasswordInfoLayout);
        viewList.add(accountPasswordHistoryInfoLayout);
        viewList.add(accountPasswordHistoryInfoLayout2);
        viewPagerAdapter = new MyViewPagerAdapter(viewList);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok_btn)
        {
            switch (currentMode)
            {
                case MODE_VIEW:
                    accountPasswordHistoryInfoLayout.setVisibility(View.GONE);
                    currentMode = MODE_EDIT;
                    break;
                case MODE_EDIT:
                    accountPasswordHistoryInfoLayout.setVisibility(View.VISIBLE);
                    currentMode = MODE_VIEW;
                    break;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,TestActivity.class);
        context.startActivity(intent);
    }
}
