package com.andzj.passwordhelper.bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.andzj.passwordhelper.utils.MyTimeUtils;

import java.util.Date;


/**
 * Created by zj on 2017/1/18.
 */

public class AccountPasswordInfo implements Parcelable {

    public static String NOT_SET = "未设置";

    private Integer id;
    private String accountCategory;
    private String accountName;
    private String nickname;
    private String bindPhoneNumber;
    private String bindMailbox;
    //private String bindCooperationAccount;
    private String bindCooperationCategory;
    private String bindCooperationAccountName;
    private String bindCooperationPassword;
    private String bindCooperationNote;

    private String password;
    private String note;
    private String updateTime;

    public AccountPasswordInfo(Integer id, String accountCategory,
                               String accountName, String nickname, String bindPhoneNumber, String bindMailbox,
                               String bindCooperationCategory, String bindCooperationAccountName, String bindCooperationPassword, String bindCooperationNote,
                               String password,String note, String updateTime) {
        this.id = id;
        this.accountCategory = accountCategory;
        this.accountName = accountName;
        this.nickname = nickname;
        this.bindPhoneNumber = bindPhoneNumber;
        this.bindMailbox = bindMailbox;
        this.bindCooperationCategory = bindCooperationCategory;
        this.bindCooperationAccountName = bindCooperationAccountName;
        this.bindCooperationPassword = bindCooperationPassword;
        this.bindCooperationNote = bindCooperationNote;
        this.note = note;
        this.password = password;
        this.updateTime = updateTime;
    }

    public AccountPasswordInfo(Integer id, String accountCategory,
                               String accountName, String nickname, String bindPhoneNumber, String bindMailbox,
                               String bindCooperationCategory, String bindCooperationAccountName, String bindCooperationPassword, String bindCooperationNote,
                               String password,String note) {
        this(id,accountCategory,accountName,nickname,bindPhoneNumber,bindMailbox,
                bindCooperationCategory,bindCooperationAccountName,bindCooperationPassword,bindCooperationNote,
                password,note, MyTimeUtils.getMyTimeStr(new Date()));
    }

    public AccountPasswordInfo(String accountCategory,
                               String accountName, String nickname, String bindPhoneNumber, String bindMailbox,
                               String bindCooperationCategory, String bindCooperationAccountName, String bindCooperationPassword, String bindCooperationNote,
                               String password,String note)
    {
        this(0,accountCategory,accountName,nickname,bindPhoneNumber,bindMailbox,
                bindCooperationCategory,bindCooperationAccountName,bindCooperationPassword,bindCooperationNote,
                password,note, MyTimeUtils.getMyTimeStr(new Date()));
    }

    public AccountPasswordInfo(AccountPasswordInfo info)
    {
        this(info.getId(),info.getAccountCategory(),info.getAccountName(),info.getNickname(),
                info.getBindPhoneNumber(),info.getBindMailbox(),
                info.getBindCooperationCategory(),info.getBindCooperationAccountName(),info.getBindCooperationPassword(),info.getBindCooperationNote(),
                info.getPassword(),info.getNote(),info.getUpdateTime());
    }

    public AccountPasswordInfo()
    {

    }

    public AccountPasswordInfo(EncryptionInfo info)
    {
        this(info.getId(),info.getAc(),info.getAn(),info.getNn(),info.getBpn(),info.getBm(),
                info.getBcc(),info.getBca(),info.getBcp(),info.getBcn(),info.getP(),info.getN(),
                info.getUt());
    }

    @Deprecated
    public AccountPasswordInfo(AccountPasswordHistoryInfo historyInfo)
    {
        this(historyInfo.getIndexId(),historyInfo.getAccountCategory(),historyInfo.getAccountName(),historyInfo.getNickname(),
                historyInfo.getBindPhoneNumber(),historyInfo.getBindMailbox(),
                historyInfo.getBindCooperationCategory(),historyInfo.getBindCooperationAccountName(),historyInfo.getBindCooperationPassword(),historyInfo.getBindCooperationNote(),
                historyInfo.getPassword(),historyInfo.getNote(),historyInfo.getUpdateTime());
    }

    public void update(AccountPasswordInfo info)
    {
        setId(info.getId());
        setAccountCategory(info.getAccountCategory());
        setAccountName(info.getAccountName());
        setNickname(info.getNickname());
        setBindPhoneNumber(info.getBindPhoneNumber());
        setBindMailbox(info.getBindMailbox());
        setBindCooperationCategory(info.getBindCooperationCategory());
        setBindCooperationAccountName(info.getBindCooperationAccountName());
        setBindCooperationPassword(info.getBindCooperationPassword());
        setBindCooperationNote(info.getBindCooperationNote());
        setPassword(info.getPassword());
        setNote(info.getNote());
        setUpdateTime(info.getUpdateTime());
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id:").append(id).append("   ");
        sb.append("accountCategory:").append(accountCategory).append("   ");
        sb.append("accountName:").append(accountName).append("   ");
        sb.append("nickname:").append(nickname).append("   ");
        sb.append("bindPhoneNumber:").append(bindPhoneNumber).append("   ");
        sb.append("bindMailbox:").append(bindMailbox).append("   ");
        sb.append("bindCooperationCategory:").append(bindCooperationCategory).append("   ");
        sb.append("bindCooperationAccountName:").append(bindCooperationAccountName).append("   ");
        sb.append("bindCooperationPassword:").append(bindCooperationPassword).append("   ");
        sb.append("bindCooperationNote:").append(bindCooperationNote).append("   ");
        sb.append("password:").append(password).append("   ");
        sb.append("note:").append(note).append("   ");
        sb.append("updateTime:").append(updateTime).append("\n");
        return sb.toString();
    }

    private boolean isEmpty(String s)
    {
        return s == null || "".equals(s);
    }

    public void dislodgeEmptyString()
    {
        accountCategory = dislodgeEmptyString(accountCategory);
        accountName = dislodgeEmptyString(accountName);
        nickname = dislodgeEmptyString(nickname);
        bindPhoneNumber = dislodgeEmptyString(bindPhoneNumber);
        bindMailbox = dislodgeEmptyString(bindMailbox);
        bindCooperationCategory = dislodgeEmptyString(bindCooperationCategory);
        bindCooperationAccountName = dislodgeEmptyString(bindCooperationAccountName);
        bindCooperationPassword = dislodgeEmptyString(bindCooperationPassword);
        bindCooperationNote = dislodgeEmptyString(bindCooperationNote);
        password = dislodgeEmptyString(password);
        note = dislodgeEmptyString(note);
        updateTime = dislodgeEmptyString(updateTime);
    }

    private String dislodgeEmptyString(String s)
    {
        return (s == null || "".equals(s)) ? null : s;
    }

    public String getAccountDescribe()
    {
        if (!isEmpty(accountName))
        {
            return accountName;
        }
        else if (!isEmpty(nickname))
        {
            return nickname;
        }
        else if (!isEmpty(bindPhoneNumber))
        {
            return bindPhoneNumber;
        }
        else if (!isEmpty(bindMailbox))
        {
            return bindMailbox;
        }
        else if (!isEmpty(bindCooperationCategory))
        {
            if (!isEmpty(bindCooperationAccountName))
            {
                return bindCooperationCategory + ":" + bindCooperationAccountName;
            }
            return bindCooperationCategory;
        }
        return NOT_SET;
    }

    public String getPasswordDescribe()
    {
        if (!isEmpty(password))
        {
            return password;
        }
        else if (!isEmpty(bindCooperationCategory) && !isEmpty(bindCooperationPassword))
        {
            return bindCooperationCategory + ":" + bindCooperationPassword;
        }
        return NOT_SET;

    }

    public String getNoteDescribe()
    {
        if (!isEmpty(note))
        {
            return note;
        }
        else if (!isEmpty(bindCooperationCategory) && !isEmpty(bindCooperationNote))
        {
            return bindCooperationNote;
        }
        return NOT_SET;
    }

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBindCooperationAccountName() {
        return bindCooperationAccountName;
    }

    public void setBindCooperationAccountName(String bindCooperationAccountName) {
        this.bindCooperationAccountName = bindCooperationAccountName;
    }

    public String getBindCooperationCategory() {
        return bindCooperationCategory;
    }

    public void setBindCooperationCategory(String bindCooperationCategory) {
        this.bindCooperationCategory = bindCooperationCategory;
    }

    public String getBindCooperationNote() {
        return bindCooperationNote;
    }

    public void setBindCooperationNote(String bindCooperationNote) {
        this.bindCooperationNote = bindCooperationNote;
    }

    public String getBindCooperationPassword() {
        return bindCooperationPassword;
    }

    public void setBindCooperationPassword(String bindCooperationPassword) {
        this.bindCooperationPassword = bindCooperationPassword;
    }

    public String getBindMailbox() {
        return bindMailbox;
    }

    public void setBindMailbox(String bindMailbox) {
        this.bindMailbox = bindMailbox;
    }

    public String getBindPhoneNumber() {
        return bindPhoneNumber;
    }

    public void setBindPhoneNumber(String bindPhoneNumber) {
        this.bindPhoneNumber = bindPhoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(accountCategory);
        dest.writeString(accountName);
        dest.writeString(nickname);
        dest.writeString(bindPhoneNumber);
        dest.writeString(bindMailbox);
        dest.writeString(bindCooperationCategory);
        dest.writeString(bindCooperationAccountName);
        dest.writeString(bindCooperationPassword);
        dest.writeString(bindCooperationNote);
        dest.writeString(password);
        dest.writeString(note);
        dest.writeString(updateTime);
    }

    public static final Creator<AccountPasswordInfo> CREATOR = new Creator<AccountPasswordInfo>()
    {
        @Override
        public AccountPasswordInfo createFromParcel(Parcel source) {
            Integer id = source.readInt();
            String accountCategory = source.readString();
            String accountName = source.readString();
            String nickname = source.readString();
            String bindPhoneNumber = source.readString();
            String bindMailbox = source.readString();
            String bindCooperationCategory = source.readString();
            String bindCooperationAccountName = source.readString();
            String bindCooperationPassword = source.readString();
            String bindCooperationNote = source.readString();
            String password = source.readString();
            String note = source.readString();
            String updateTime = source.readString();
            return new AccountPasswordInfo(id,accountCategory,accountName,nickname,bindPhoneNumber,bindMailbox,
                    bindCooperationCategory,bindCooperationAccountName,bindCooperationPassword,bindCooperationNote,password,note,updateTime);
        }

        @Override
        public AccountPasswordInfo[] newArray(int size) {
            return new AccountPasswordInfo[size];
        }
    };
}
