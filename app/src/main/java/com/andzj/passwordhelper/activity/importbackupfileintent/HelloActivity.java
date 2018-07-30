package com.andzj.passwordhelper.activity.importbackupfileintent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.ActivityCollector;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.utils.MyApplication;

/**
 * Created by ZJ on 2017/3/30.
 */

public class HelloActivity extends MyAppCompatActivity {

    public static String TAG = "HelloActivity";
    public String chosenFilePath = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Intent launchIntent = getIntent();
        Uri uri =  launchIntent.getData();
        if (uri != null)
        {
            chosenFilePath = uri.getPath();
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
                    if (userInfo == null)
                    {
                        Toast.makeText(HelloActivity.this,"应用出现错误，无法启动.",Toast.LENGTH_SHORT).show();
                        ActivityCollector.finishAll();
                    }
                    else if (userInfo.getLoginPassword() == null || userInfo.getEmailAddress() == null)
                    {
                        Toast.makeText(HelloActivity.this,"请先进入本应用设置密码和邮箱，再进行导入操作",Toast.LENGTH_SHORT).show();
                        ActivityCollector.finishAll();
                    }
                    else if (userInfo.getLoginPassword() != null)
                    {
                        PasswordLockActivity.actionStart(HelloActivity.this,userInfo,chosenFilePath);
                        finish();
                    }
                }
            },1000);
        }
        else
        {
            Toast.makeText(HelloActivity.this, "文件获取失败，请检查是否赋予了存储权限", Toast.LENGTH_SHORT).show();
            ActivityCollector.finishAll();
        }
    }
}
