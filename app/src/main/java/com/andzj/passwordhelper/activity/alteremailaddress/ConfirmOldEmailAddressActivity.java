package com.andzj.passwordhelper.activity.alteremailaddress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.bean.UserInfo;

/**
 * Created by ZJ on 2017/4/6.
 */

public class ConfirmOldEmailAddressActivity extends MyAppCompatActivity {
    public static String TAG = "ConfirmOldEmailAddressActivity";

    private EditText emailAddressEdit1;
    private LinearLayout emailAddressLayout2;
    //private EditText emailAddressEdit2;
    private LinearLayout emailAddressConfirmLayout;
    //private CheckBox emailAddressConfirmCheck;
    private LinearLayout softwareStatement;
    //Pattern pattern = null;
    private LinearLayout inputEmailHintLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_email_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("请先验证旧邮箱地址");
        emailAddressEdit1 = (EditText) findViewById(R.id.email_address_edit1);
        //emailAddressEdit2 = (EditText) findViewById(R.id.email_address_edit2);
        emailAddressLayout2 = (LinearLayout) findViewById(R.id.email_address_layout2);
        emailAddressLayout2.setVisibility(View.GONE);
        emailAddressConfirmLayout = (LinearLayout) findViewById(R.id.email_address_confirm_layout);
        emailAddressConfirmLayout.setVisibility(View.GONE);
        //emailAddressConfirmCheck = (CheckBox) findViewById(R.id.email_address_confirm_check);
        softwareStatement = (LinearLayout) findViewById(R.id.software_statement);
        softwareStatement.setVisibility(View.GONE);
        //String regEmailStr = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //pattern = Pattern.compile(regEmailStr);
        inputEmailHintLayout = (LinearLayout) findViewById(R.id.input_email_hint_layout);
        inputEmailHintLayout.setVisibility(View.GONE);
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
                if (emailAddressEdit1.getText().toString().isEmpty())
                {
                    Toast.makeText(ConfirmOldEmailAddressActivity.this,"请输入邮件地址",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String emailAddress = emailAddressEdit1.getText().toString();
                    UserInfo userInfo = MainActivity.getUserInfo();
                    if ( emailAddress.equals(userInfo.getEmailAddress()))
                    {
                        Toast.makeText(ConfirmOldEmailAddressActivity.this,"旧邮箱验证完毕",Toast.LENGTH_SHORT).show();
                        AlterEmailAddressActivity.actionStart(ConfirmOldEmailAddressActivity.this);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ConfirmOldEmailAddressActivity.this,"旧邮箱不正确",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.help_btn:
                Toast.makeText(ConfirmOldEmailAddressActivity.this,"更改邮箱地址前需要验证旧邮箱地址",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,ConfirmOldEmailAddressActivity.class);
        context.startActivity(intent);
    }
}
