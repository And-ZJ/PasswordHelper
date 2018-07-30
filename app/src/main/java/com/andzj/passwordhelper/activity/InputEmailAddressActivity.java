package com.andzj.passwordhelper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.db.UserDatabaseHelper;
import com.andzj.passwordhelper.log.MyLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZJ on 2017/3/2.
 */
public class InputEmailAddressActivity extends MyAppCompatActivity {
    public static String TAG = "InputEmailAddressActivity";

    private EditText emailAddressEdit1;
    private EditText emailAddressEdit2;
    private CheckBox emailAddressConfirmCheck;
    Pattern pattern = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_email_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("设置邮箱地址");
        emailAddressEdit1 = (EditText) findViewById(R.id.email_address_edit1);
        emailAddressEdit2 = (EditText) findViewById(R.id.email_address_edit2);
        emailAddressConfirmCheck = (CheckBox) findViewById(R.id.email_address_confirm_check);
        String regEmailStr = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        pattern = Pattern.compile(regEmailStr);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_input_email_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save_btn:
                if (emailAddressEdit1.getText().toString().isEmpty() || emailAddressEdit2.getText().toString().isEmpty())
                {
                    Toast.makeText(InputEmailAddressActivity.this,"请输入邮件地址",Toast.LENGTH_SHORT).show();
                }
                else if (!emailAddressEdit1.getText().toString().equals(emailAddressEdit2.getText().toString()))
                {
                    Toast.makeText(InputEmailAddressActivity.this,"两次输入地址不一致",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String emailAddress = emailAddressEdit1.getText().toString();
                    Matcher matcher = pattern.matcher(emailAddress);
                    if (matcher.matches() || emailAddressConfirmCheck.isChecked())
                    {
                        UserInfo userInfo = MainActivity.getUserInfo();
                        if (userInfo.getId() > 0)
                        {
                            userInfo.setEmailAddress(emailAddress);
                            UserDatabaseHelper.update(userInfo);
                        }
                        else
                        {
                            MyLog.e(TAG,"no id",true);
                        }
                        Toast.makeText(InputEmailAddressActivity.this,"邮箱设置完成",Toast.LENGTH_SHORT).show();
                        if (MainActivity.getUserInfo().getLoginPassword() == null)
                        {
                            PasswordLockSetActivity.actionStart(InputEmailAddressActivity.this);
                            finish();
                        }
                        else
                        {
                            MainActivity.actionStart(InputEmailAddressActivity.this);
                            finish();
                        }

                    }
                    else
                    {
                        Toast.makeText(InputEmailAddressActivity.this,"邮箱格式不正确",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.help_btn:
                Toast.makeText(InputEmailAddressActivity.this,"需要帮助请浏览本页下方填写说明",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,InputEmailAddressActivity.class);
        context.startActivity(intent);
    }
}
