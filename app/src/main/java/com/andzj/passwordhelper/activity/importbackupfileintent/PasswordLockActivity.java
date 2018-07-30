package com.andzj.passwordhelper.activity.importbackupfileintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.layout.OnClickOkBtnListener;
import com.andzj.passwordhelper.layout.PasswordKeyboardLayout;

/**
 * Created by ZJ on 2017/3/30.
 */

public class PasswordLockActivity extends MyAppCompatActivity{
    public static String TAG = "PasswordLockActivity";

    private PasswordKeyboardLayout passwordLayout;
    private UserInfo userInfo;
    private String filePath = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_lock);
        Toolbar titleBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(titleBar);
        titleBar.setSubtitle("请先输入应用密码");
        userInfo = getIntent().getParcelableExtra("userInfo");
        filePath = getIntent().getStringExtra("path");
        if (userInfo != null )
        {
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
                                //Toast.makeText(PasswordLockActivity.this,"验证成功", Toast.LENGTH_SHORT).show();
                                MainActivity.isConfirmed = true;
                                ImportBackupFileActivity.actionStart(PasswordLockActivity.this,filePath);
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

    public static void actionStart(Context context, UserInfo userInfo,String chosenFilePath)
    {
        Intent intent = new Intent(context,PasswordLockActivity.class);
        intent.putExtra("userInfo",userInfo);
        intent.putExtra("path",chosenFilePath);
        context.startActivity(intent);
    }
}
