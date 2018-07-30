package com.andzj.passwordhelper.bean;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.StringBuilderPrinter;

/**
 * Created by zj on 2017/1/28.
 */

public class UserInfo implements Parcelable {
    private Integer id;
    private String savePublicKey;
    private String savePrivateKey;
    private String loginPassword;//encrypt
    private String passwordHint;
    private String emailAddress;
    private String updateTime;

    public UserInfo(Integer id, String savePublicKey, String savePrivateKey, String loginPassword,String passwordHint, String emailAddress,String updateTime) {
        this.id = id;
        this.savePublicKey = savePublicKey;
        this.savePrivateKey = savePrivateKey;
        this.loginPassword = loginPassword;
        this.passwordHint = passwordHint;
        this.emailAddress = emailAddress;
        this.updateTime = updateTime;
    }

    public UserInfo(){}

    public void update(UserInfo userInfo)
    {
        this.id = userInfo.getId();
        this.savePublicKey = userInfo.getSavePublicKey();
        this.savePrivateKey = userInfo.getSavePrivateKey();
        this.loginPassword = userInfo.getLoginPassword();
        this.passwordHint = userInfo.getPasswordHint();
        this.emailAddress = userInfo.getEmailAddress();
        this.updateTime = userInfo.getUpdateTime();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append("   ");
        sb.append("savePublicKey:").append(savePublicKey).append("   ");
        sb.append("savePrivateKey:").append(savePrivateKey).append("   ");
        sb.append("loginPassword:").append(loginPassword).append("   ");
        sb.append("passwordHint:").append(passwordHint).append("   ");
        sb.append("emailAddress:").append(emailAddress).append("   ");
        sb.append("updateTime:").append(updateTime).append("\n");
        return sb.toString();
    }

    public void dislodgeEmptyString()
    {
        savePublicKey = dislodgeEmptyString(savePublicKey);
        savePrivateKey = dislodgeEmptyString(savePrivateKey);
        loginPassword = dislodgeEmptyString(loginPassword);
        passwordHint = dislodgeEmptyString(passwordHint);
        emailAddress = dislodgeEmptyString(emailAddress);
        updateTime = dislodgeEmptyString(updateTime);
    }

    private String dislodgeEmptyString(String s)
    {
        return (s == null || "".equals(s)) ? null : s;
    }

    public static String calEmailAddressShort(String emailAddress)
    {
        if (emailAddress == null || emailAddress.isEmpty())
        {
            return null;
        }
        StringBuffer buffer = new StringBuffer(emailAddress);
        int position = emailAddress.lastIndexOf("@");
        if (position >= 0)
        {
            if (position >3)
            {
                buffer.replace(3,position,"***");

            }
            return buffer.toString();
        }
        return emailAddress;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSavePrivateKey() {
        return savePrivateKey;
    }

    public void setSavePrivateKey(String savePrivateKey) {
        this.savePrivateKey = savePrivateKey;
    }

    public String getSavePublicKey() {
        return savePublicKey;
    }

    public void setSavePublicKey(String savePublicKey) {
        this.savePublicKey = savePublicKey;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
        dest.writeString(savePublicKey);
        dest.writeString(savePrivateKey);
        dest.writeString(loginPassword);
        dest.writeString(passwordHint);
        dest.writeString(emailAddress);
        dest.writeString(updateTime);
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            Integer id = source.readInt();
            String savePublicKey = source.readString();
            String savePrivateKey = source.readString();
            String loginPassword = source.readString();
            String passwordHint = source.readString();
            String emailAddress = source.readString();
            String updateTime = source.readString();
            return new UserInfo(id,savePublicKey,savePrivateKey,loginPassword,passwordHint,emailAddress,updateTime);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}


