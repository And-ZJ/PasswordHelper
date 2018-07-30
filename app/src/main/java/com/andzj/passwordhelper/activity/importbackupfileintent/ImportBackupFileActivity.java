package com.andzj.passwordhelper.activity.importbackupfileintent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.MyAppCompatActivity;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.utils.ImportUtil;

import java.util.Map;

/**
 * Created by ZJ on 2017/3/26.
 */

/**
 *
 *  修改导入数据界面设计，修改hint的显示方案
 *  设计找回密码文件格式，和写入方案
 *
 *
 */
public class ImportBackupFileActivity extends MyAppCompatActivity implements View.OnClickListener{

    public static String TAG="ImportBackupFileActivity";
    public final static int REQUEST_CODE_FILE = 20;
    private TextView chosenFilenameView;
    private EditText importPasswordEdit;
    private TextView passwordHintView;
    private TextView hintView;
    private Button chooseFileBtn;
    private Button importBtn;
    private Button passwordRecoveryBtn;

    private String chooseFilePath;
    private String encryptPassword;
    private String passwordHint;
    private String emailAddress;
    private boolean fileReadSuccess = false;
    private boolean fileReadCompleted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_backup_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("导入到密码小助手");
        chooseFilePath =  getIntent().getStringExtra("path");
        chosenFilenameView = (TextView) findViewById(R.id.chosen_filename_view);
        importPasswordEdit = (EditText) findViewById(R.id.import_password_edit);
        passwordHintView = (TextView) findViewById(R.id.password_hint_view);
        hintView = (TextView) findViewById(R.id.hint_view);
        chooseFileBtn = (Button)findViewById(R.id.choose_file_btn);
        chooseFileBtn.setOnClickListener(this);
        importBtn = (Button) findViewById(R.id.import_btn);
        importBtn.setOnClickListener(this);
        getFileInfo(chooseFilePath);
        passwordRecoveryBtn = (Button) findViewById(R.id.password_recovery_btn);
        passwordRecoveryBtn.setOnClickListener(this);
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQUEST_CODE_FILE:
                if (resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    if (chooseFilePath != null && chooseFilePath.equals(path) && fileReadCompleted)
                    {
                        Toast.makeText(ImportBackupFileActivity.this,"此文件已导入，勿重复导入",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        fileReadCompleted = false;
                        getFileInfo(path);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.choose_file_btn:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE_FILE);
                break;
            case R.id.import_btn:
                if (chooseFilePath == null || chooseFilePath.isEmpty())
                {
                    Toast.makeText(ImportBackupFileActivity.this,"请选择文件",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fileReadCompleted)
                {
                    Toast.makeText(ImportBackupFileActivity.this,"此文件已导入，勿重复导入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fileReadSuccess)
                {
                    Toast.makeText(ImportBackupFileActivity.this,"当前文件无法识别！请重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = importPasswordEdit.getText().toString();
                if ("".equals(password))
                {
                    Toast.makeText(ImportBackupFileActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String enPassword = MyEncryption.encryptExportPassword(password,emailAddress);
                if (enPassword == null || !enPassword.equals(encryptPassword))
                {
                    Toast.makeText(ImportBackupFileActivity.this, "密码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( ImportUtil.importBackupFileOperation(ImportBackupFileActivity.this,chooseFilePath,password,emailAddress))
                {
                    fileReadCompleted = true;
                    Toast.makeText(ImportBackupFileActivity.this,"导入完成",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ImportBackupFileActivity.this,"导入出错",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.password_recovery_btn:
                if (chooseFilePath == null || chooseFilePath.isEmpty())
                {
                    Toast.makeText(ImportBackupFileActivity.this,"请选择文件",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fileReadCompleted)
                {
                    Toast.makeText(ImportBackupFileActivity.this,"此文件已导入，无需找回密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fileReadSuccess)
                {
                    Toast.makeText(ImportBackupFileActivity.this,"当前文件无法识别！请重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                ExportPasswordRecoveryFileActivity.actionStart(ImportBackupFileActivity.this,encryptPassword,emailAddress);
                break;
            default:
                break;
        }
    }

    private void getFileInfo(String filename)
    {
        chooseFilePath = filename;
        chosenFilenameView.setText(chooseFilePath);
        Map<String,String> rstMap = ImportUtil.distinguishImportFile(filename);
        String error = null;
        if ((error = rstMap.get("error")) != null)
        {
            fileReadSuccess=false;
            Toast.makeText(ImportBackupFileActivity.this,error,Toast.LENGTH_SHORT).show();
        }
        String success = null;
        if ((success = rstMap.get("success")) != null)
        {
            fileReadSuccess = true;
            Toast.makeText(ImportBackupFileActivity.this,success,Toast.LENGTH_SHORT).show();
            this.encryptPassword = rstMap.get("encryptPassword");
            this.emailAddress = rstMap.get("emailAddress");
            this.passwordHint = rstMap.get("passwordHint");
            fileReadSuccess = true;
            passwordHintView.setText(passwordHint);
        }
    }

    public static void actionStart(Context context,String chosenFilePath)
    {
        Intent intent = new Intent(context,ImportBackupFileActivity.class);
        intent.putExtra("path",chosenFilePath);
        context.startActivity(intent);
    }
}
