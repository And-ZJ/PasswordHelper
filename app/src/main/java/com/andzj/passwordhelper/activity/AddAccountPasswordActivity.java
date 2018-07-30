package com.andzj.passwordhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.db.PasswordDataBaseHelper;

/**
 * Created by zj on 2017/1/19.
 */

public class AddAccountPasswordActivity extends MyAppCompatActivity implements View.OnClickListener,TextWatcher {

    public static final int REQUEST_CODE = 10;
    public static String TAG = "AddAccountPasswordActivity";
    public static String TAG_CN = "添加帐号和密码";

    private Toolbar titleBar;
    private EditText accountCategoryEdit;
    private EditText accountNameEdit;
    private EditText nicknameEdit;
    private EditText bindPhoneNumberEdit;
    private EditText bindMailboxEdit;
    private CheckBox bindCooperationAccountCheck;
    private TextView bindCooperationAccountView;
    private EditText bindCooperationCategoryEdit;
    private TableLayout bindCooperationAccountTable;
    private EditText bindCooperationAccountNameEdit;
    private EditText bindCooperationPasswordEdit;
    private EditText bindCooperationNoteEdit;
    private EditText passwordEdit;
    private EditText noteEdit;

    private boolean isChanged = false;
    private AccountPasswordInfo accountPasswordInfo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_account_password);
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_account_password, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.save_btn:
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                    View view = getWindow().getCurrentFocus();
                    if (view != null)
                    {
                        imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                saveOperation(true);
                break;
            }
            case R.id.help_btn:
            {
                Toast.makeText(AddAccountPasswordActivity.this,"需要帮助请浏览本页下方填写说明",Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //View.OnClickListener
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bind_cooperation_account_view:
            {
                bindCooperationAccountCheck.setChecked(!bindCooperationAccountCheck.isChecked());
                break;
            }
            default:
                break;
        }
    }

    //TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        isChanged = true;
    }

    @Override
    public void onBackPressed() {
        onBackKeyPressed();
    }

    private void onBackKeyPressed()
    {
        if (isChanged)
        {
            notSavedAlertDialogShow();
        }
        else
        {
            actionFinish(accountPasswordInfo);
        }
    }


    private void saveOperation(boolean isExit)
    {
        if (isInfoCheckedOK())
        {
            if (saveInfo())
            {
                Toast.makeText(AddAccountPasswordActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                isChanged = false;
                if (isExit)
                {
                    actionFinish(accountPasswordInfo);
                }
            }
            else
            {
                Toast.makeText(AddAccountPasswordActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            notCompleteAlertDialogShow();
        }
    }

    private boolean isInfoCheckedOK()
    {
        String accountCategory = accountCategoryEdit.getText().toString();
        String bindCooperationCategory = bindCooperationCategoryEdit.getText().toString();
        if ("".equals(accountCategory) || (bindCooperationAccountCheck.isChecked() && "".equals(bindCooperationCategory)))
        {
            return false;
        }
        return true;
    }

    private boolean saveInfo()
    {
        AccountPasswordInfo info = getInfoFromPage();
        AccountPasswordInfo savedInfo = null;
        if (accountPasswordInfo == null)
        {
            savedInfo = PasswordDataBaseHelper.insert(info);
        }
        else
        {
            info.setId(accountPasswordInfo.getId());
            savedInfo = PasswordDataBaseHelper.update(info);
        }
        if (savedInfo != null && savedInfo.getId() >= 0)
        {
            accountPasswordInfo = savedInfo;
            return true;
        }
        else
        {
            return false;
        }
    }

    private AccountPasswordInfo getInfoFromPage()
    {
        String accountCategory = accountCategoryEdit.getText().toString();
        String accountName = accountNameEdit.getText().toString();
        String nickname = nicknameEdit.getText().toString();
        String bindPhoneNumber = bindPhoneNumberEdit.getText().toString();
        String bindMailbox = bindMailboxEdit.getText().toString();
        String bindCooperationCategory = bindCooperationCategoryEdit.getText().toString();
        String bindCooperationAccountName = bindCooperationAccountNameEdit.getText().toString();
        String bindCooperationPassword = bindCooperationPasswordEdit.getText().toString();
        String bindCooperationNote = bindCooperationNoteEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String note = noteEdit.getText().toString();

        if (!bindCooperationAccountCheck.isChecked())
        {
            bindCooperationCategory = null;
            bindCooperationAccountName = null;
            bindCooperationPassword = null;
            bindCooperationNote = null;
        }
        return  new AccountPasswordInfo(0,accountCategory,accountName,nickname,
                bindPhoneNumber,bindMailbox,bindCooperationCategory,bindCooperationAccountName,bindCooperationPassword,bindCooperationNote,
                password,note);
    }

    private AlertDialog.Builder notCompleteAlertDialogShow()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddAccountPasswordActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("有必填项未填写，请检查后再次保存。");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("确定",null);
        alertDialog.show();
        return alertDialog;
    }

    private AlertDialog.Builder notSavedAlertDialogShow()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddAccountPasswordActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("有更改未保存，是否保存？");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("保存后退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveOperation(true);
            }
        });
        alertDialog.setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionFinish(accountPasswordInfo);
            }
        });
        alertDialog.setNeutralButton("返回继续修改",null);
        alertDialog.show();
        return alertDialog;
    }


    private void initView()
    {
        titleBar = (Toolbar) findViewById(R.id.toolbar);
        titleBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_btn_selector_50px));
        titleBar.setSubtitle(TAG_CN);
        this.setSupportActionBar(titleBar);
        accountCategoryEdit = (EditText) findViewById(R.id.account_category_edit);
        accountNameEdit = (EditText) findViewById(R.id.account_name_edit);
        nicknameEdit = (EditText) findViewById(R.id.nickname_edit);
        bindPhoneNumberEdit = (EditText) findViewById(R.id.bind_phone_number_edit);
        bindMailboxEdit = (EditText) findViewById(R.id.bind_mailbox_edit);
        bindCooperationAccountCheck = (CheckBox) findViewById(R.id.bind_cooperation_account_check);
        bindCooperationAccountView = (TextView) findViewById(R.id.bind_cooperation_account_view);
        bindCooperationAccountTable = (TableLayout) findViewById(R.id.bind_cooperation_account_table);
        bindCooperationCategoryEdit = (EditText) findViewById(R.id.bind_cooperation_category_edit);
        bindCooperationAccountNameEdit = (EditText) findViewById(R.id.bind_cooperation_account_name_edit);
        bindCooperationPasswordEdit = (EditText) findViewById(R.id.bind_cooperation_password_edit);
        bindCooperationNoteEdit = (EditText) findViewById(R.id.bind_cooperation_note_edit);
        bindCooperationAccountTable.setVisibility(View.GONE);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        noteEdit = (EditText) findViewById(R.id.note_edit);
        addViewListener();
    }

    private void addViewListener()
    {
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackKeyPressed();
            }
        });
        accountCategoryEdit.addTextChangedListener(this);
        accountNameEdit.addTextChangedListener(this);
        nicknameEdit.addTextChangedListener(this);
        bindPhoneNumberEdit.addTextChangedListener(this);
        bindMailboxEdit.addTextChangedListener(this);
        bindCooperationAccountCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bindCooperationAccountTable.setVisibility(isChecked?View.VISIBLE:View.GONE);
            }
        });
        bindCooperationAccountView.setOnClickListener(this);
        bindCooperationCategoryEdit.addTextChangedListener(this);
        bindCooperationAccountNameEdit.addTextChangedListener(this);
        bindCooperationPasswordEdit.addTextChangedListener(this);
        bindCooperationNoteEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        noteEdit.addTextChangedListener(this);
    }

    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,AddAccountPasswordActivity.class);
        ((Activity)context).startActivityForResult(intent,REQUEST_CODE);
    }

    private void actionFinish(AccountPasswordInfo accountPasswordInfo)
    {
        if (accountPasswordInfo != null)
        {
            Intent intent = new Intent();
            intent.putExtra("info",accountPasswordInfo);
            setResult(RESULT_OK,intent);
            finish();
        }
        else
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

}
