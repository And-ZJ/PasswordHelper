package com.andzj.passwordhelper.layout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.bean.AccountPasswordHistoryInfo;

/**
 * Created by zj on 2017/1/29.
 */

public class HistoryInfoLayout extends LinearLayout {

    public static String TAG = "HistoryInfoLayout";

    private TextView accountCategoryView;
    private TextView accountNameView;
    private TextView nicknameView;
    private TextView bindPhoneNumberView;
    private TextView bindMailboxView;
    private TextView bindCooperationCategoryView;
    private TextView bindCooperationAccountNameView;
    private TextView bindCooperationPasswordView;
    private TextView bindCooperationNoteView;
    private TextView passwordView;
    private TextView noteView;
    private TextView updateTimeView;

    private AccountPasswordHistoryInfo historyInfo = null;

    public HistoryInfoLayout(Context context, AccountPasswordHistoryInfo historyInfo) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_account_password_history_info,this);

        accountCategoryView = (TextView) findViewById(R.id.account_category_view);
        accountNameView = (TextView) findViewById(R.id.account_name_view);
        nicknameView = (TextView) findViewById(R.id.nickname_view);
        bindPhoneNumberView = (TextView) findViewById(R.id.bind_phone_number_view);
        bindMailboxView = (TextView) findViewById(R.id.bind_mailbox_view);
        bindCooperationCategoryView = (TextView) findViewById(R.id.bind_cooperation_category_view);
        bindCooperationAccountNameView = (TextView) findViewById(R.id.bind_cooperation_account_name_view);
        bindCooperationPasswordView = (TextView) findViewById(R.id.bind_cooperation_password_view);
        bindCooperationNoteView = (TextView) findViewById(R.id.bind_cooperation_note_view);
        passwordView = (TextView) findViewById(R.id.password_view);
        noteView = (TextView) findViewById(R.id.note_view);
        updateTimeView = (TextView) findViewById(R.id.update_time_view);

        this.historyInfo = historyInfo;
        loadView(historyInfo);
    }


    private void loadView(AccountPasswordHistoryInfo historyInfo)
    {
        if (historyInfo == null)
        {
            return;
        }
        accountCategoryView.setText(historyInfo.getAccountCategory());
        accountNameView.setText(historyInfo.getAccountName());
        nicknameView.setText(historyInfo.getNickname());
        bindPhoneNumberView.setText(historyInfo.getBindPhoneNumber());
        bindMailboxView.setText(historyInfo.getBindMailbox());
        bindCooperationCategoryView.setText(historyInfo.getBindCooperationCategory());
        bindCooperationAccountNameView.setText(historyInfo.getBindCooperationAccountName());
        bindCooperationPasswordView.setText(historyInfo.getBindCooperationPassword());
        bindCooperationNoteView.setText(historyInfo.getBindCooperationNote());
        passwordView.setText(historyInfo.getPassword());
        noteView.setText(historyInfo.getPassword());
        updateTimeView.setText(historyInfo.getUpdateTime());

    }

    public Integer getHistoryInfoId()
    {
        if (historyInfo != null)
        {
            return historyInfo.getId();
        }
        return 0;
    }



}
