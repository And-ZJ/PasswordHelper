package com.andzj.passwordhelper.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andzj.passwordhelper.encrypt.ExportEncryption;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.bean.EncryptionInfo;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.utils.ExportUtil;
import com.andzj.passwordhelper.utils.MyApplication;
import com.andzj.passwordhelper.utils.MyFileOperateUtils;
import com.andzj.passwordhelper.utils.MyTimeUtils;

import java.io.File;
import java.io.FileWriter;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;

/**
 * Created by zj on 2017/2/15.
 */

public class ExportBackupFileActivity extends MyAppCompatActivity implements View.OnClickListener{

    public static String TAG = "ExportBackupFileActivity";

    private EditText exportPasswordEdit;
    private EditText exportPasswordConfirmEdit;
    private EditText passwordHintEdit;
    private EditText filenameEdit;
    private TextView hintView;
    private Button exportBtn;

    private String emailAddress = null;

    private static String regEmailStr = "^\\\\w+([-+.]\\\\w+)*@\\\\w+([-.]\\\\w+)*\\\\.\\\\w+([-.]\\\\w+)*$";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_backup_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("导出");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_btn_selector_50px));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportBackupFileActivity.super.onBackPressed();
            }
        });
        exportPasswordEdit = (EditText) findViewById(R.id.export_password_edit);
        exportPasswordConfirmEdit = (EditText) findViewById(R.id.export_password_confirm_edit);
        filenameEdit = (EditText) findViewById(R.id.filename_edit);
        passwordHintEdit = (EditText) findViewById(R.id.password_hint_edit);
        hintView = (TextView) findViewById(R.id.hint_view);
        exportBtn = (Button) findViewById(R.id.export_btn);
        exportBtn.setOnClickListener(this);
        UserInfo userInfo = MainActivity.getUserInfo();
        emailAddress = userInfo.getEmailAddress();
        if (emailAddress == null || emailAddress.isEmpty())
        {
            hintView.setText("您还未设置邮箱，请先设置邮箱再来导出操作。");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.export_btn:
                String password1 = exportPasswordEdit.getText().toString();
                String password2 = exportPasswordConfirmEdit.getText().toString();
                String passwordHint = passwordHintEdit.getText().toString();
                String filename = filenameEdit.getText().toString();
                if (password1.isEmpty())
                {
                    Toast.makeText(ExportBackupFileActivity.this,"必须设置密码",Toast.LENGTH_SHORT).show();
                }
                else if (password2.isEmpty())
                {
                    Toast.makeText(ExportBackupFileActivity.this,"请再输入一次密码",Toast.LENGTH_SHORT).show();
                }
                else if (!password1.equals(password2))
                {
                    Toast.makeText(ExportBackupFileActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }
                else if (passwordHint.isEmpty())
                {
                    Toast.makeText(ExportBackupFileActivity.this,"还请输入密码提示文字",Toast.LENGTH_SHORT).show();
                }
                else if (filename.isEmpty())
                {
                    Toast.makeText(ExportBackupFileActivity.this,"请输入导出的文件名",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    List<AccountPasswordInfo> infos = MainActivity.allInfoList;
                    if (infos == null || infos.isEmpty())
                    {
                        Toast.makeText(ExportBackupFileActivity.this,"没有数据可供导出",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String res = ExportUtil.exportBackupFileOperation(ExportBackupFileActivity.this,
                                filename,password1,passwordHint,emailAddress,infos);
                        if (res == null)
                        {
                            Toast.makeText(ExportBackupFileActivity.this,"导出出错",Toast.LENGTH_SHORT).show();
                            hintView.setText("    导出出错\n" +
                                    "1.请检查是否给予了存储权限或者剩余空间是否足够\n" +
                                    "2.请检查文件名中是否含有特殊字符\n" +
                                    "3.请检查输入的密码、邮箱等信息是否齐全");
                        }
                        else
                        {
                            Toast.makeText(ExportBackupFileActivity.this,"导出完成",Toast.LENGTH_SHORT).show();
                            hintView.setText("文件存放路径：\n" + res + " ");
                        }

                    }
                }
                break;
            default:
                break;
        }
    }

    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,ExportBackupFileActivity.class);
        context.startActivity(intent);
    }
}
