package com.andzj.passwordhelper.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.andzj.passwordhelper.R;


/**
 * Created by zj on 2017/1/26.
 */

public class PasswordKeyboardLayout extends LinearLayout implements View.OnClickListener {

    private TextView passwordView;
    private Switch showPasswordSwitch;
    private Button keyBtn0;
    private Button keyBtn1;
    private Button keyBtn2;
    private Button keyBtn3;
    private Button keyBtn4;
    private Button keyBtn5;
    private Button keyBtn6;
    private Button keyBtn7;
    private Button keyBtn8;
    private Button keyBtn9;
    private Button keyBtnDelete;
    private Button keyBtnOk;

    private StringBuffer passwordStr = new StringBuffer();

    public PasswordKeyboardLayout(Context context, AttributeSet attrs)  {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_password_keyboard,this);
        passwordView = (TextView) findViewById(R.id.password_view);
        keyBtn0 = (Button) findViewById(R.id.key_btn_0);
        keyBtn1 = (Button) findViewById(R.id.key_btn_1);
        keyBtn2 = (Button) findViewById(R.id.key_btn_2);
        keyBtn3 = (Button) findViewById(R.id.key_btn_3);
        keyBtn4 = (Button) findViewById(R.id.key_btn_4);
        keyBtn5 = (Button) findViewById(R.id.key_btn_5);
        keyBtn6 = (Button) findViewById(R.id.key_btn_6);
        keyBtn7 = (Button) findViewById(R.id.key_btn_7);
        keyBtn8 = (Button) findViewById(R.id.key_btn_8);
        keyBtn9 = (Button) findViewById(R.id.key_btn_9);
        keyBtnDelete = (Button) findViewById(R.id.key_btn_delete);
        keyBtnOk = (Button) findViewById(R.id.key_btn_ok);
        showPasswordSwitch = (Switch) findViewById(R.id.show_password_switch);
        keyBtn0.setOnClickListener(this);
        keyBtn1.setOnClickListener(this);
        keyBtn2.setOnClickListener(this);
        keyBtn3.setOnClickListener(this);
        keyBtn4.setOnClickListener(this);
        keyBtn5.setOnClickListener(this);
        keyBtn6.setOnClickListener(this);
        keyBtn7.setOnClickListener(this);
        keyBtn8.setOnClickListener(this);
        keyBtn9.setOnClickListener(this);
        keyBtnDelete.setOnClickListener(this);
        showPasswordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowPassword = isChecked;
                passwordView.setText(isShowPassword?passwordStr.toString():viewStr.toString());

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.key_btn_delete:
                deletePassword();
                break;
            case R.id.key_btn_0:
                addPassword(0);
                break;
            case R.id.key_btn_1:
                addPassword(1);
                break;
            case R.id.key_btn_2:
                addPassword(2);
                break;
            case R.id.key_btn_3:
                addPassword(3);
                break;
            case R.id.key_btn_4:
                addPassword(4);
                break;
            case R.id.key_btn_5:
                addPassword(5);
                break;
            case R.id.key_btn_6:
                addPassword(6);
                break;
            case R.id.key_btn_7:
                addPassword(7);
                break;
            case R.id.key_btn_8:
                addPassword(8);
                break;
            case R.id.key_btn_9:
                addPassword(9);
                break;
            default:
                break;
        }
    }

    public void setOnClickOkBtnListener(final OnClickOkBtnListener listener)
    {
        keyBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    listener.onClick(getPassword());
                }
            }
        });
    }

    public String getPassword()
    {
        return passwordStr.toString();
    }

    private void addPassword(int p)
    {
        if (passwordStr.length() < 20)
        {
            passwordStr.append(p);
            addNumToView();
        }
    }

    private void deletePassword()
    {
        if (passwordStr.length() > 0)
        {
            passwordStr.deleteCharAt(passwordStr.length() - 1);
            deleteNumToView();
        }
    }

    private boolean isShowPassword = false;

    public void setShowPassword(boolean isShow)
    {
        this.isShowPassword = isShow;
        passwordView.setText(isShowPassword?passwordStr.toString():viewStr.toString());
        showPasswordSwitch.setChecked(isShow);
    }

    public boolean isShowPassword()
    {
        return isShowPassword;
    }

    private StringBuffer viewStr = new StringBuffer();

    private void addNumToView()
    {
        if (viewStr.length() <20)
        {
            viewStr.append("*");
            passwordView.setText(isShowPassword?passwordStr.toString():viewStr.toString());
        }
    }

    private void deleteNumToView()
    {
        if (viewStr.length() > 0)
        {
            viewStr.deleteCharAt(viewStr.length()-1);
            passwordView.setText(isShowPassword?passwordStr.toString():viewStr.toString());
        }
    }

    public void setPasswordViewEmpty()
    {
        passwordView.setText("");
    }
}
