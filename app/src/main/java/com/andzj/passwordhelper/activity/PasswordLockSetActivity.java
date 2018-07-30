package com.andzj.passwordhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.db.UserDatabaseHelper;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.layout.OnClickOkBtnListener;
import com.andzj.passwordhelper.layout.PasswordKeyboardLayout;
import com.andzj.passwordhelper.log.MyLog;

/**
 * Created by ZJ on 2017/3/25.
 */

public class PasswordLockSetActivity extends MyAppCompatActivity {
    public static String TAG = "PasswordLockSetActivity";

    private PasswordKeyboardLayout passwordLayout1;
    private PasswordKeyboardLayout passwordLayout2;
    private Toolbar titleBar;
    private String enPassword1;
    private String enPassword2;

    private static int MODE1 = 1;
    private static int MODE2 = 2;
    private int currentMode = MODE1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_lock);
        titleBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(titleBar);
        passwordLayout1 = (PasswordKeyboardLayout) findViewById(R.id.password_layout1);
        passwordLayout2 = (PasswordKeyboardLayout) findViewById(R.id.password_layout2);
        password1View();
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackAction();
            }
        });
        passwordLayout1.setOnClickOkBtnListener(new OnClickOkBtnListener() {
            @Override
            public void onClick(String password) {
                if (password != null && password.length() >0)
                {
                    try
                    {
                        enPassword1 = MyEncryption.encryptDislodgeEmptyString(password, RSAKey.getUserInfoPublicKey());
                        password2View();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        passwordLayout2.setOnClickOkBtnListener(new OnClickOkBtnListener() {
            @Override
            public void onClick(String password) {
                if (password != null && password.length() >0)
                {
                    try
                    {
                        enPassword2 = MyEncryption.encryptDislodgeEmptyString(password, RSAKey.getUserInfoPublicKey());
                        if (enPassword2 != null && enPassword2.equals(enPassword1))
                        {
                            UserInfo userInfo = MainActivity.getUserInfo();
                            if (userInfo.getId() > 0)
                            {
                                userInfo.setLoginPassword(enPassword2);
                                UserDatabaseHelper.update(userInfo);
                            }
                            else
                            {
                                MyLog.w(TAG,"no id");
                                userInfo.setEmailAddress(enPassword2);
                                UserInfo idInfo = UserDatabaseHelper.insert(userInfo);
                                MainActivity.getUserInfo().update(idInfo);
                            }
                            Toast.makeText(PasswordLockSetActivity.this,"密码设置完成，请开始使用",Toast.LENGTH_SHORT).show();
                            MainActivity.actionStart(PasswordLockSetActivity.this);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(PasswordLockSetActivity.this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
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
                Toast.makeText(PasswordLockSetActivity.this,"请设置应用密码，进入此应用需要输入此密码。",Toast.LENGTH_SHORT).show();
                Toast.makeText(PasswordLockSetActivity.this,"当您忘记了应用密码时，点此按钮，可进行找回密码操作",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    private void onBackAction(){
        if (currentMode == MODE1)
        {
            super.onBackPressed();
        }
        else
        {
            password1View();
        }
    }

    private void password1View()
    {
        currentMode = MODE1;
        titleBar.setSubtitle("设置应用密码");
        passwordLayout2.setVisibility(View.GONE);
        passwordLayout1.setVisibility(View.VISIBLE);
        titleBar.setNavigationIcon(null);

    }

    private void password2View()
    {
        currentMode = MODE2;
        titleBar.setSubtitle("再次设置应用密码");
        passwordLayout1.setVisibility(View.GONE);
        passwordLayout2.setVisibility(View.VISIBLE);
        titleBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_btn_selector_50px));
    }


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,PasswordLockSetActivity.class);
        context.startActivity(intent);
    }
}
