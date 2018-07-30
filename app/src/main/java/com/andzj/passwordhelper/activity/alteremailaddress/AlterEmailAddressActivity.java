package com.andzj.passwordhelper.activity.alteremailaddress;

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
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.db.UserDatabaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZJ on 2017/4/6.
 */

public class AlterEmailAddressActivity extends MyAppCompatActivity {
    public static String TAG = "AlterEmailAddressActivity";

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
        toolbar.setSubtitle("设置邮箱新地址");
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
                    Toast.makeText(AlterEmailAddressActivity.this,"请输入邮件地址",Toast.LENGTH_SHORT).show();
                }
                else if (!emailAddressEdit1.getText().toString().equals(emailAddressEdit2.getText().toString()))
                {
                    Toast.makeText(AlterEmailAddressActivity.this,"两次输入地址不一致",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String emailAddress = emailAddressEdit1.getText().toString();
                    Matcher matcher = pattern.matcher(emailAddress);
                    if (matcher.matches() || emailAddressConfirmCheck.isChecked())
                    {
                        UserInfo userInfo = MainActivity.getUserInfo();
                        userInfo.setEmailAddress(emailAddress);
                        UserDatabaseHelper.update(userInfo);
//                        if (userInfo.getId() > 0)
//                        {
//                            userInfo.setEmailAddress(emailAddress);
//                            UserDatabaseHelper.update(userInfo);
//                        }
//                        else
//                        {
//                            MyLog.e(TAG,"no id",true);
//                            userInfo.setEmailAddress(emailAddress);
//                            UserInfo idInfo = UserDatabaseHelper.insert(userInfo);
//                            MainActivity.getUserInfo().update(idInfo);
//                        }
                        Toast.makeText(AlterEmailAddressActivity.this,"邮箱设置完成",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AlterEmailAddressActivity.this,"邮箱格式不正确",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.help_btn:
                Toast.makeText(AlterEmailAddressActivity.this,"请输入新邮箱地址",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,AlterEmailAddressActivity.class);
        context.startActivity(intent);
    }
}
