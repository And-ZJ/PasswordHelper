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
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.db.PasswordDataBaseHelper;

/**
 * Created by zj on 2017/1/23.
 */

public class AccountPasswordDetailInfoActivity  extends MyAppCompatActivity implements View.OnClickListener,TextWatcher {

    public static final int REQUEST_CODE = 20;
    public static String TAG = "AccountPasswordDetailInfoActivity";
    public static String TAG_VIEW_CN = "详细信息";
    public static String TAG_EDIT_CN = "修改信息";

    public static final int MODE_VIEW = 21;
    public static final int MODE_EDIT = 22;

    private Toolbar titleBar;
    private EditText accountCategoryEdit;
    private TextView accountCategoryMarkView;
    private EditText accountNameEdit;
    private EditText nicknameEdit;
    private EditText bindPhoneNumberEdit;
    private EditText bindMailboxEdit;
    private CheckBox bindCooperationAccountCheck;
    private TextView bindCooperationAccountView;
    private EditText bindCooperationCategoryEdit;
    private TextView bindCooperationCategoryMarkView;
    private TableLayout bindCooperationAccountTable;
    private EditText bindCooperationAccountNameEdit;
    private EditText bindCooperationPasswordEdit;
    private EditText bindCooperationNoteEdit;
    private EditText passwordEdit;
    private EditText noteEdit;
    private TableRow updateTimeRow;
    private TextView updateTimeView;
    private LinearLayout editHintLayout;


    private boolean isChanged = false;
    private boolean isUpdated = false;
    private boolean isDeleted = false;
    private int currentMode = MODE_VIEW;
    private AccountPasswordInfo accountPasswordInfo = null;
    private KeyListener defaultKeyListener = null;
    private KeyListener phoneNumberKeyListener = null;
    private KeyListener emailKeyListener = null;

    InputMethodManager imm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_password_detail_info);
        initView();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        accountPasswordInfo = getIntent().getParcelableExtra("info");
        setPageFromInfo(accountPasswordInfo);
        setViewMode();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_account_password_detail_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (currentMode)
        {
            case MODE_EDIT:
            {
                menu.findItem(R.id.save_btn).setVisible(true);
                menu.findItem(R.id.edit_btn).setVisible(false);
                break;
            }
            case MODE_VIEW:
            {
                menu.findItem(R.id.save_btn).setVisible(false);
                menu.findItem(R.id.edit_btn).setVisible(true);
                break;
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.delete_btn:
            {
                deleteAlertDialogShow();
                break;
            }
            case R.id.edit_btn:
            {
                setEditMode();
                break;
            }
            case R.id.save_btn:
            {
                saveOperation();
                break;
            }
            case R.id.help_btn:
            {
                Toast.makeText(AccountPasswordDetailInfoActivity.this,"点击修改标记可直接修改，点击删除标记即删除",Toast.LENGTH_SHORT).show();
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
                bindCooperationAccountCheck.setChecked(bindCooperationAccountCheck.isEnabled() && !bindCooperationAccountCheck.isChecked());
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
        if (currentMode == MODE_EDIT)
        {
            isChanged = true;
        }
    }

    @Override
    public void onBackPressed() {
        onBackKeyPressed();
    }

    private void onBackKeyPressed()
    {
        if (currentMode == MODE_EDIT && isChanged)
        {
            notSavedAlertDialogShow();
        }
        else
        {
            actionFinish();
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

    private void saveOperation()
    {
        if (isInfoCheckedOK())
        {
            AccountPasswordInfo info = getInfoFromPage();
            AccountPasswordInfo savedInfo = null;
            if (accountPasswordInfo != null)
            {
                info.setId(accountPasswordInfo.getId());
                savedInfo = PasswordDataBaseHelper.update(info);
            }
            if (savedInfo != null && savedInfo.getId() >= 0)
            {
                accountPasswordInfo = savedInfo;
                Toast.makeText(AccountPasswordDetailInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                isChanged = false;
                isUpdated = true;
                actionFinish();
            }
            else
            {
                Toast.makeText(AccountPasswordDetailInfoActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            notCompleteAlertDialogShow();
        }
    }

    private void deleteOperation()
    {
        if (accountPasswordInfo != null)
        {
            AccountPasswordInfo deletedInfo = PasswordDataBaseHelper.delete(accountPasswordInfo);
            if (deletedInfo != null)
            {
                isDeleted = true;
                accountPasswordInfo = deletedInfo;
                Toast.makeText(AccountPasswordDetailInfoActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                actionFinish();
                return;
            }
        }
        Toast.makeText(AccountPasswordDetailInfoActivity.this,"删除失败",Toast.LENGTH_SHORT).show();

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

    private void setPageFromInfo(AccountPasswordInfo info)
    {
        accountCategoryEdit.setText(info.getAccountCategory());
        accountNameEdit.setText(info.getAccountName());
        nicknameEdit.setText(info.getNickname());
        bindPhoneNumberEdit.setText(info.getBindPhoneNumber());
        bindMailboxEdit.setText(info.getBindMailbox());
        if (info.getBindCooperationCategory() == null || "".equals(info.getBindCooperationCategory()))
        {
            bindCooperationAccountCheck.setChecked(false);
            bindCooperationAccountTable.setVisibility(View.GONE);
        }
        else
        {
            bindCooperationAccountCheck.setChecked(true);
            bindCooperationAccountTable.setVisibility(View.VISIBLE);
            bindCooperationCategoryEdit.setText(info.getBindCooperationCategory());
            bindCooperationAccountNameEdit.setText(info.getBindCooperationAccountName());
            bindCooperationPasswordEdit.setText(info.getBindCooperationPassword());
            bindCooperationNoteEdit.setText(info.getBindCooperationNote());
        }
        passwordEdit.setText(info.getPassword());
        noteEdit.setText(info.getNote());
        updateTimeView.setText(info.getUpdateTime());
    }

    private AlertDialog.Builder deleteAlertDialogShow()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountPasswordDetailInfoActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("删除后不可恢复，确认删除？");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOperation();
            }
        });
        alertDialog.setNegativeButton("取消", null);
        alertDialog.show();
        return alertDialog;
    }

    private AlertDialog.Builder notCompleteAlertDialogShow()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountPasswordDetailInfoActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("有必填项未填写，请检查后再次保存。");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("确定",null);
        alertDialog.show();
        return alertDialog;
    }

    private AlertDialog.Builder notSavedAlertDialogShow()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountPasswordDetailInfoActivity.this);
        alertDialog.setTitle("警告");
        alertDialog.setMessage("有更改未保存，是否保存？");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveOperation();
            }
        });
        alertDialog.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionFinish();
            }
        });
        alertDialog.setNeutralButton("继续修改",null);
        alertDialog.show();
        return alertDialog;
    }


    private void initView()
    {
        titleBar = (Toolbar) findViewById(R.id.toolbar);
        titleBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_btn_selector_50px));
        titleBar.setSubtitle(TAG_VIEW_CN);
        this.setSupportActionBar(titleBar);
        //menu = titleBar.getMenu();
        //getMenuInflater().inflate(R.menu.activity_account_password_detail_info,menu);
        accountCategoryEdit = (EditText) findViewById(R.id.account_category_edit);
        accountCategoryMarkView = (TextView) findViewById(R.id.account_category_mark_view);
        defaultKeyListener = accountCategoryEdit.getKeyListener();
        accountNameEdit = (EditText) findViewById(R.id.account_name_edit);
        nicknameEdit = (EditText) findViewById(R.id.nickname_edit);
        bindPhoneNumberEdit = (EditText) findViewById(R.id.bind_phone_number_edit);
        phoneNumberKeyListener = bindPhoneNumberEdit.getKeyListener();
        bindMailboxEdit = (EditText) findViewById(R.id.bind_mailbox_edit);
        emailKeyListener = bindMailboxEdit.getKeyListener();
        bindCooperationAccountCheck = (CheckBox) findViewById(R.id.bind_cooperation_account_check);
        bindCooperationAccountView = (TextView) findViewById(R.id.bind_cooperation_account_view);
        bindCooperationAccountTable = (TableLayout) findViewById(R.id.bind_cooperation_account_table);
        bindCooperationCategoryEdit = (EditText) findViewById(R.id.bind_cooperation_category_edit);
        bindCooperationCategoryMarkView = (TextView) findViewById(R.id.bind_cooperation_category_mark_view);
        bindCooperationAccountNameEdit = (EditText) findViewById(R.id.bind_cooperation_account_name_edit);
        bindCooperationPasswordEdit = (EditText) findViewById(R.id.bind_cooperation_password_edit);
        bindCooperationNoteEdit = (EditText) findViewById(R.id.bind_cooperation_note_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        noteEdit = (EditText) findViewById(R.id.note_edit);
        updateTimeRow = (TableRow) findViewById(R.id.update_time_row);
        updateTimeView = (TextView) findViewById(R.id.update_time_view);
        editHintLayout = (LinearLayout) findViewById(R.id.edit_hint_layout);
        addViewListener();
    }

    private void setViewMode()
    {

        currentMode = MODE_VIEW;
        invalidateOptionsMenu();
        titleBar.setSubtitle(TAG_VIEW_CN);
        accountCategoryEdit.setKeyListener(null);
        accountCategoryEdit.setHint("未设置");
        accountCategoryMarkView.setText(" ");
        accountNameEdit.setKeyListener(null);
        accountNameEdit.setHint("未设置");
        nicknameEdit.setKeyListener(null);
        nicknameEdit.setHint("未设置");
        bindPhoneNumberEdit.setKeyListener(null);
        bindPhoneNumberEdit.setHint("未设置");
        bindMailboxEdit.setKeyListener(null);
        bindMailboxEdit.setHint("未设置");
        bindCooperationAccountCheck.setEnabled(false);
        bindCooperationCategoryEdit.setKeyListener(null);
        bindCooperationCategoryEdit.setHint("未设置");
        bindCooperationCategoryMarkView.setText(" ");
        bindCooperationAccountNameEdit.setKeyListener(null);
        bindCooperationAccountNameEdit.setHint("未设置");
        bindCooperationPasswordEdit.setKeyListener(null);
        bindCooperationPasswordEdit.setHint("未设置");
        bindCooperationNoteEdit.setKeyListener(null);
        bindCooperationNoteEdit.setHint("未设置");
        passwordEdit.setKeyListener(null);
        passwordEdit.setHint("未设置");
        noteEdit.setKeyListener(null);
        noteEdit.setHint("未设置");
        editHintLayout.setVisibility(View.GONE);
        updateTimeRow.setVisibility(View.VISIBLE);
        if (imm != null)
        {
            View view = getWindow().getCurrentFocus();
            if (view != null)
            {
                imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    private void setEditMode()
    {

        currentMode = MODE_EDIT;
        invalidateOptionsMenu();
        titleBar.setSubtitle(TAG_EDIT_CN);
        accountCategoryEdit.setKeyListener(defaultKeyListener);
        accountCategoryEdit.setHint("如QQ帐号");
        accountCategoryMarkView.setText("*");
        accountNameEdit.setKeyListener(defaultKeyListener);
        accountNameEdit.setHint("用于登录的帐号名");
        nicknameEdit.setKeyListener(defaultKeyListener);
        nicknameEdit.setHint("用于登录的昵称");
        bindPhoneNumberEdit.setKeyListener(phoneNumberKeyListener);
        bindPhoneNumberEdit.setHint("绑定的手机号");
        bindMailboxEdit.setKeyListener(emailKeyListener);
        bindMailboxEdit.setHint("绑定的邮箱");
        bindCooperationAccountCheck.setEnabled(true);
        bindCooperationCategoryEdit.setKeyListener(defaultKeyListener);
        bindCooperationCategoryEdit.setHint("如绑定微信帐号");
        bindCooperationCategoryMarkView.setText("*");
        bindCooperationAccountNameEdit.setKeyListener(defaultKeyListener);
        bindCooperationAccountNameEdit.setHint("合作帐号的登录帐号");
        bindCooperationPasswordEdit.setKeyListener(defaultKeyListener);
        bindCooperationPasswordEdit.setHint("合作帐号的登录密码");
        bindCooperationNoteEdit.setKeyListener(defaultKeyListener);
        bindCooperationNoteEdit.setHint("输入你想输入的备注");
        passwordEdit.setKeyListener(defaultKeyListener);
        passwordEdit.setHint("如QQ帐号的密码");
        noteEdit.setKeyListener(defaultKeyListener);
        noteEdit.setHint("输入你想输入的备注");
        editHintLayout.setVisibility(View.VISIBLE);
        updateTimeRow.setVisibility(View.GONE);

        if (imm != null)
        {
            accountCategoryEdit.requestFocus();
            imm.showSoftInput(accountCategoryEdit,0);
            accountCategoryEdit.setSelection(accountCategoryEdit.getText().length());
        }
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

    public static void actionStart(Context context,AccountPasswordInfo info)
    {
        Intent intent = new Intent(context,AccountPasswordDetailInfoActivity.class);
        intent.putExtra("info",info);
        ((Activity)context).startActivityForResult(intent,REQUEST_CODE);
    }

    private void actionFinish()
    {
        if (isDeleted)
        {
            Intent intent = new Intent();
            intent.putExtra("info",accountPasswordInfo);
            intent.putExtra("deleted",true);
            setResult(RESULT_OK,intent);
            finish();
        }
        if (currentMode == MODE_EDIT)
        {
            setViewMode();
            setPageFromInfo(accountPasswordInfo);
        }
        else if (currentMode == MODE_VIEW)
        {
            if (isUpdated)
            {
                Intent intent = new Intent();
                intent.putExtra("info",accountPasswordInfo);
                intent.putExtra("deleted",false);
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
}
