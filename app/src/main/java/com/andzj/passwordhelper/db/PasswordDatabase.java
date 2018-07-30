package com.andzj.passwordhelper.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.utils.MyApplication;

/**
 * Created by ZJ on 2017/3/26.
 */

public class PasswordDatabase {

    PasswordDataBaseHelper helper = null;
    SQLiteDatabase db = null;
    public PasswordDatabase()
    {
        helper = new PasswordDataBaseHelper(MyApplication.getContext(),PasswordDataBaseHelper.DATABASE_ACCOUNT_PASSWORD,null,PasswordDataBaseHelper.DATABASE_VERSION);
        db = helper.getWritableDatabase();
    }

    public boolean insert(AccountPasswordInfo info)
    {
        if (info == null)
        {
            return false;
        }
        ContentValues values = new ContentValues();
        info.dislodgeEmptyString();
        AccountPasswordInfo encryptedInfo = MyEncryption.encryptAccountPasswordInfo(info, RSAKey.getSavePublicKey());
        values.put("accountCategory",encryptedInfo.getAccountCategory());
        values.put("accountName", encryptedInfo.getAccountName());
        values.put("nickname",encryptedInfo.getNickname());
        values.put("bindPhoneNumber",encryptedInfo.getBindPhoneNumber());
        values.put("bindMailbox",encryptedInfo.getBindMailbox());
        values.put("bindCooperationCategory",encryptedInfo.getBindCooperationCategory());
        values.put("bindCooperationAccountName",encryptedInfo.getBindCooperationAccountName());
        values.put("bindCooperationPassword",encryptedInfo.getBindCooperationPassword());
        values.put("bindCooperationNote",encryptedInfo.getBindCooperationNote());
        values.put("password",encryptedInfo.getPassword());
        values.put("note",encryptedInfo.getNote());
        values.put("updateTime",encryptedInfo.getUpdateTime());
        if (db.insert(PasswordDataBaseHelper.TABLE_PASSWORD_INFO,null,values) > 0)
        {
            values.clear();
            return true;
        }
        values.clear();
        return false;

    }

    public void close()
    {
        db.close();
        helper.close();
    }



}
