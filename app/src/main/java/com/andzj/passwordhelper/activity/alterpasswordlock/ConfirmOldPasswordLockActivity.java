package com.andzj.passwordhelper.activity.alterpasswordlock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.ActivityCollector;
import com.andzj.passwordhelper.activity.ExportPasswordRecoveryFileActivity;
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.activity.PasswordLockActivity;
import com.andzj.passwordhelper.activity.alteremailaddress.ConfirmOldEmailAddressActivity;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.layout.OnClickOkBtnListener;
import com.andzj.passwordhelper.layout.PasswordKeyboardLayout;

/**
 * Created by ZJ on 2017/4/6.
 */

public class ConfirmOldPasswordLockActivity extends MyAppCompatActivity {
    public static String TAG = "ConfirmOldPasswordLockActivity";

    private PasswordKeyboardLayout passwordLayout;
    UserInfo userInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_lock);
        Toolbar titleBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(titleBar);
        titleBar.setSubtitle("请先验证旧应用密码");
        userInfo = MainActivity.getUserInfo();
        passwordLayout = (PasswordKeyboardLayout) findViewById(R.id.password_layout1);
        passwordLayout.setOnClickOkBtnListener(new OnClickOkBtnListener() {
            @Override
            public void onClick(String password) {
                if (password != null && password.length() >0)
                {
                    try
                    {
                        String encryptPassword = MyEncryption.encryptDislodgeEmptyString(password, RSAKey.getUserInfoPublicKey());
                        if (encryptPassword != null && encryptPassword.equals(userInfo.getLoginPassword()))
                        {
                            Toast.makeText(ConfirmOldPasswordLockActivity.this,"旧应用密码验证完毕", Toast.LENGTH_SHORT).show();
                            AlterPasswordLockActivity.actionStart(ConfirmOldPasswordLockActivity.this);
                            ConfirmOldPasswordLockActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(ConfirmOldPasswordLockActivity.this,"旧应用密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        ConfirmOldPasswordLockActivity.this.finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_password_lock,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.help_btn:
                Toast.makeText(ConfirmOldPasswordLockActivity.this,"更改应用密码前需要验证旧应用密码",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,ConfirmOldPasswordLockActivity.class);
        context.startActivity(intent);
    }
}
