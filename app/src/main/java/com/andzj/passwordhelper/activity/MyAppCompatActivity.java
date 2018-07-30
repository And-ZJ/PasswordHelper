package com.andzj.passwordhelper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andzj.passwordhelper.log.MyLog;

/**
 * Created by ZJ on 2017/3/4.
 */

public class MyAppCompatActivity extends AppCompatActivity {
    public static String TAG = "MyAppCompatActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MyLog.d(TAG,getClass().getSimpleName() + "  OnCreate.",false);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        MyLog.d(TAG,getClass().getSimpleName() + " OnDestroy.",false);
        ActivityCollector.removeActivity(this);
    }
}
