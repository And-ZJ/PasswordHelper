package com.andzj.passwordhelper.activity.importbackupfileintent;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.utils.ExportUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ZJ on 2017/4/4.
 */

public class ExportPasswordRecoveryFileActivity extends MyAppCompatActivity implements View.OnClickListener {
    public static String TAG = "ExportPasswordRecoveryFileActivity";
    private Button generatePasswordRecoveryBtn;
    private TextView hintView;
    private TextView emailAddressHintView;
    private String encryptedPassword = null;
    private String emailAddress = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_password_recovery_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("找回密码");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_btn_selector_50px));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportPasswordRecoveryFileActivity.super.onBackPressed();
            }
        });
        Intent intent = getIntent();
        encryptedPassword = intent.getStringExtra("password");
        emailAddress = intent.getStringExtra("emailAddress");
        generatePasswordRecoveryBtn = (Button) findViewById(R.id.generate_password_recovery_btn);
        generatePasswordRecoveryBtn.setOnClickListener(this);
        hintView = (TextView) findViewById(R.id.hint_view);
        emailAddressHintView = (TextView) findViewById(R.id.email_address_hint_view);
        if (encryptedPassword == null || emailAddress == null)
        {
            Toast.makeText(ExportPasswordRecoveryFileActivity.this,"数据出错,请返回重试",Toast.LENGTH_SHORT).show();
        }
        else
        {
            emailAddressHintView.setText("3.登录邮箱： " + UserInfo.calEmailAddressShort(emailAddress) + " ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.generate_password_recovery_btn:
                if (encryptedPassword == null || emailAddress == null)
                {
                    Toast.makeText(ExportPasswordRecoveryFileActivity.this,"数据出错,请返回重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                try
                {
                    String encryptEmailAddress = MyEncryption.encryptDislodgeEmptyString(emailAddress, RSAKey.getImportPasswordPublicKey());
                    String rst = ExportUtil.exportPasswordRecoveryFile(ExportPasswordRecoveryFileActivity.this,encryptedPassword,encryptEmailAddress);
                    if (rst == null)
                    {
                        Toast.makeText(ExportPasswordRecoveryFileActivity.this,"生成出错",Toast.LENGTH_SHORT).show();
                        hintView.setText("    生成出错\n 1.请检查是否给予了存储权限或者剩余空间是否足够");
                    }
                    else
                    {
                        Toast.makeText(ExportPasswordRecoveryFileActivity.this,"生成成功",Toast.LENGTH_SHORT).show();
                        hintView.setText("文件存放路径：\n" + rst + " ");
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(ExportPasswordRecoveryFileActivity.this,"加密时出现错误，请重试",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void actionStart(Context context, String encryptedPassword, String emailAddress)
    {
        Intent intent = new Intent(context,ExportPasswordRecoveryFileActivity.class);
        intent.putExtra("password",encryptedPassword);
        intent.putExtra("emailAddress",emailAddress);
        context.startActivity(intent);
    }
}
