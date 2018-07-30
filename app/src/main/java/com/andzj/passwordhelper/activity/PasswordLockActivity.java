package com.andzj.passwordhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.layout.OnClickOkBtnListener;
import com.andzj.passwordhelper.layout.PasswordKeyboardLayout;
import com.andzj.passwordhelper.encrypt.MyEncryption;

/**
 * Created by zj on 2017/1/26.
 */

public class PasswordLockActivity extends MyAppCompatActivity {
    public static String TAG = "PasswordLockActivity";

    private PasswordKeyboardLayout passwordLayout;
    UserInfo userInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_lock);
        Toolbar titleBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(titleBar);
        titleBar.setSubtitle("输入应用密码");
        userInfo = getIntent().getParcelableExtra("userInfo");
        if (userInfo != null )
        {
            passwordLayout = (PasswordKeyboardLayout) findViewById(R.id.password_layout1);
            passwordLayout.setOnClickOkBtnListener(new OnClickOkBtnListener() {
                @Override
                public void onClick(String password) {
                    //MyLog.d(TAG,"Password="+password);
                    if (password != null && password.length() >0)
                    {
                        try
                        {
                            String encryptPassword = MyEncryption.encryptDislodgeEmptyString(password, RSAKey.getUserInfoPublicKey());
                            if (encryptPassword != null && encryptPassword.equals(userInfo.getLoginPassword()))
                            {
                                //Toast.makeText(PasswordLockActivity.this,"验证成功", Toast.LENGTH_SHORT).show();
                                MainActivity.isConfirmed = true;
                                MainActivity.actionStart(PasswordLockActivity.this);
                                PasswordLockActivity.this.finish();
                            }
                            else
                            {
                                Toast.makeText(PasswordLockActivity.this,"密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            PasswordLockActivity.this.finish();
                        }
                    }

                }
            });
        }
        else
        {
            Toast.makeText(PasswordLockActivity.this,"应用出错", Toast.LENGTH_SHORT).show();
            ActivityCollector.finishAll();
        }
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
                if ( userInfo != null)
                {
                    ExportPasswordRecoveryFileActivity.actionStart(PasswordLockActivity.this,userInfo.getLoginPassword(),userInfo.getEmailAddress());
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context,UserInfo userInfo)
    {
        Intent intent = new Intent(context,PasswordLockActivity.class);
        intent.putExtra("userInfo",userInfo);
        context.startActivity(intent);
    }
}
