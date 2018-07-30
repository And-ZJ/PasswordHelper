package com.andzj.passwordhelper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.utils.MyApplication;

/**
 * Created by zj on 2017/1/29.
 */

public class HelloActivity extends MyAppCompatActivity {

    public static String TAG = "HelloActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApplication.firstLaunchCheck();
            }
        },200);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo userInfo = MyApplication.systemInit();
                ///////////////////////////////////////////////////////
                //MyLog.d(TAG,userInfo.toString());
                if (userInfo == null)
                {
                    Toast.makeText(HelloActivity.this,"应用出现错误，无法启动.",Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                }
                else if (userInfo.getLoginPassword() != null)
                {
                    PasswordLockActivity.actionStart(HelloActivity.this,userInfo);
                    finish();
                }
                else if (userInfo.getEmailAddress() == null)
                {
                    InputEmailAddressActivity.actionStart(HelloActivity.this);
                    finish();
                }
                else if (userInfo.getLoginPassword() == null)
                {
                    PasswordLockSetActivity.actionStart(HelloActivity.this);
                    finish();
                }
            }
        },1000);
    }
}
