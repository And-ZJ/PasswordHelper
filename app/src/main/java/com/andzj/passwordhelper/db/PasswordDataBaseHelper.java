package com.andzj.passwordhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.bean.AccountPasswordHistoryInfo;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.utils.MyApplication;
import com.andzj.passwordhelper.encrypt.MyEncryption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2017/1/19.
 */

public class PasswordDataBaseHelper extends SQLiteOpenHelper{
    public static String TAG = "PasswordDataBaseHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_ACCOUNT_PASSWORD = "AccountPasswords.db";
    public static final String TABLE_VERSION_INFO = "VersionInfo";
    public static final String TABLE_PASSWORD_INFO = "AccountPassword";
    public static final String TABLE_HISTORY_INFO = "PasswordHistory";


    private static final String CREATE_VERSION_INFO = "create table " + TABLE_VERSION_INFO + " ("
            + "id integer primary key autoincrement, "
            + "appVersion               real, "
            + "accountPasswordVersion   integer, "
            + "userVersion              integer) ";

    private static final String CREATE_PASSWORD_INFO = "create table " + TABLE_PASSWORD_INFO + " ("
            + "id integer primary key autoincrement, "
            + "accountCategory              text, "
            + "accountName                  text, "
            + "nickname                     text, "
            + "bindPhoneNumber              text, "
            + "bindMailbox                  text, "
            + "bindCooperationCategory      text, "
            + "bindCooperationAccountName   text, "
            + "bindCooperationPassword      text, "
            + "bindCooperationNote          text, "
            + "password                     text, "
            + "note                         text, "
            + "updateTime                   text) ";

    private static final String CREATE_HISTORY_INFO = "create table " + TABLE_HISTORY_INFO + " ("
            + "id integer primary key autoincrement, "
            + "indexId                      integer, "
            + "accountCategory              text, "
            + "accountName                  text, "
            + "nickname                     text, "
            + "bindPhoneNumber              text, "
            + "bindMailbox                  text, "
            + "bindCooperationCategory      text, "
            + "bindCooperationAccountName   text, "
            + "bindCooperationPassword      text, "
            + "bindCooperationNote          text, "
            + "password                     text, "
            + "note                         text, "
            + "updateTime                   text) ";



    private Context mContext;

    public PasswordDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,name,factory,version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PASSWORD_INFO);
        db.execSQL(CREATE_VERSION_INFO);
        db.execSQL(CREATE_HISTORY_INFO);
        //db.execSQL("insert into " + TABLE_PASSWORD_INFO + " ('accountCategory','accountName','bindMailbox','note','updateTime') values('微信号','And_ZJ','ZJ.Cosmos@gmail.com','本软件开发者的联系方式','" + MyTimeUtils.getMyTimeStr(new Date()) + "')");
        db.execSQL("insert into " + TABLE_VERSION_INFO + " ('accountPasswordVersion') values('" + String.valueOf(DATABASE_VERSION) + "')");
        db.execSQL("insert into " + TABLE_VERSION_INFO + " ('userVersion') values('" + String.valueOf(UserDatabaseHelper.DATABASE_VERSION) + "')");
        db.execSQL("insert into " + TABLE_VERSION_INFO + " ('appVersion') values('1.0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion)
        {
            case 0:
                db.execSQL(CREATE_PASSWORD_INFO);
                db.execSQL(CREATE_VERSION_INFO);
                db.execSQL(CREATE_HISTORY_INFO);
            case 1:

            default:
                db.execSQL("insert into " + TABLE_VERSION_INFO + " ('accountPasswordVersion') values('" + String.valueOf(DATABASE_VERSION) + "')");
                db.execSQL("insert into " + TABLE_VERSION_INFO + " ('userVersion') values('" + String.valueOf(UserDatabaseHelper.DATABASE_VERSION) + "')");
                db.execSQL("insert into " + TABLE_VERSION_INFO + " ('appVersion') values('1.0')");
                break;
        }
    }


    public static AccountPasswordInfo insert(AccountPasswordInfo info)
    {
        if (info == null)
        {
            return null;
        }
        ///////////////////////////
        //MyLog.d(TAG,"insert");
        PasswordDataBaseHelper helper = new PasswordDataBaseHelper(MyApplication.getContext(),DATABASE_ACCOUNT_PASSWORD,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
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
        if (db.insert(TABLE_PASSWORD_INFO,null,values) > 0)
        {
            values.clear();
            Cursor cursor = db.rawQuery("select * from " + TABLE_PASSWORD_INFO + " order by id desc limit 0,1",null);
            AccountPasswordInfo savedDecryptInfo = null;
            if (cursor.moveToFirst())
            {
                savedDecryptInfo = MyEncryption.decryptAccountPasswordInfo(readInfoFromCursor(cursor),RSAKey.getSavePrivateKey());
                //MyLog.d(TAG,"decryptInfo");
            }

            cursor.close();
            db.close();
            helper.close();
            return savedDecryptInfo;
        }
        values.clear();
        db.close();
        helper.close();
        return null;
    }




    public static AccountPasswordInfo update(AccountPasswordInfo newInfo)
    {
        if (newInfo == null || newInfo.getId() < 0)
        {
            return null;
        }
        PasswordDataBaseHelper helper = new PasswordDataBaseHelper(MyApplication.getContext(),DATABASE_ACCOUNT_PASSWORD,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        AccountPasswordInfo oldInfo = null;
        Cursor oldCursor = db.rawQuery("select * from " + TABLE_PASSWORD_INFO + " where id = " + String.valueOf(newInfo.getId()),null);
        if (oldCursor.moveToFirst())
        {
            oldInfo = readInfoFromCursor(oldCursor);
        }
        oldCursor.close();

        ContentValues newValues = new ContentValues();
        newInfo.dislodgeEmptyString();
        AccountPasswordInfo encryptedInfo = MyEncryption.encryptAccountPasswordInfo(newInfo,RSAKey.getSavePublicKey());
        newValues.put("accountCategory",encryptedInfo.getAccountCategory());
        newValues.put("accountName", encryptedInfo.getAccountName());
        newValues.put("nickname",encryptedInfo.getNickname());
        newValues.put("bindPhoneNumber",encryptedInfo.getBindPhoneNumber());
        newValues.put("bindMailbox",encryptedInfo.getBindMailbox());
        newValues.put("bindCooperationCategory",encryptedInfo.getBindCooperationCategory());
        newValues.put("bindCooperationAccountName",encryptedInfo.getBindCooperationAccountName());
        newValues.put("bindCooperationPassword",encryptedInfo.getBindCooperationPassword());
        newValues.put("bindCooperationNote",encryptedInfo.getBindCooperationNote());
        newValues.put("password",encryptedInfo.getPassword());
        newValues.put("note",encryptedInfo.getNote());
        newValues.put("updateTime",encryptedInfo.getUpdateTime());
        if (db.update(TABLE_PASSWORD_INFO,newValues,"id = ?",new String[] {String.valueOf(newInfo.getId())}) > 0)
        {
            newValues.clear();
            if (oldInfo != null)
            {
                ContentValues oldValues = new ContentValues();
                oldValues.put("indexId",encryptedInfo.getId());
                oldValues.put("accountCategory",encryptedInfo.getAccountCategory());
                oldValues.put("accountName", encryptedInfo.getAccountName());
                oldValues.put("nickname",encryptedInfo.getNickname());
                oldValues.put("bindPhoneNumber",encryptedInfo.getBindPhoneNumber());
                oldValues.put("bindMailbox",encryptedInfo.getBindMailbox());
                oldValues.put("bindCooperationCategory",encryptedInfo.getBindCooperationCategory());
                oldValues.put("bindCooperationAccountName",encryptedInfo.getBindCooperationAccountName());
                oldValues.put("bindCooperationPassword",encryptedInfo.getBindCooperationPassword());
                oldValues.put("bindCooperationNote",encryptedInfo.getBindCooperationNote());
                oldValues.put("password",encryptedInfo.getPassword());
                oldValues.put("note",encryptedInfo.getNote());
                oldValues.put("updateTime",oldInfo.getUpdateTime());
                if (db.insert(TABLE_HISTORY_INFO,null,oldValues) > 0)
                {
                    oldValues.clear();
                    db.close();
                    helper.close();
                    return newInfo;
                }
                oldValues.clear();
            }
            db.close();
            helper.close();
            return newInfo;

        }
        newValues.clear();
        db.close();
        helper.close();
        return null;
    }

    public static AccountPasswordInfo delete(AccountPasswordInfo info)
    {
        if (info == null || info.getId() < 0)
        {
            return null;
        }
        PasswordDataBaseHelper helper = new PasswordDataBaseHelper(MyApplication.getContext(),DATABASE_ACCOUNT_PASSWORD,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.delete(TABLE_PASSWORD_INFO,"id = ?",new String[]{String.valueOf(info.getId())}) > 0)
        {
            if (db.delete(TABLE_HISTORY_INFO,"indexId = ?",new String[]{String.valueOf(info.getId())}) >= 0)
            {
                db.close();
                helper.close();
                return info;
            }
        }
        db.close();
        helper.close();
        return null;
    }

    @Deprecated
    public static List<AccountPasswordInfo> searchInfo(String accountCategory)
    {
        List<AccountPasswordInfo> infos = new ArrayList<>();
        PasswordDataBaseHelper helper = new PasswordDataBaseHelper(MyApplication.getContext(),DATABASE_ACCOUNT_PASSWORD,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PASSWORD_INFO + " where accountCategory LIKE %" + accountCategory + "%",null);
        if (cursor.moveToFirst())
        {
            do
            {
                AccountPasswordInfo info = readInfoFromCursor(cursor);
                infos.add(info);
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
        return infos;
    }


    public static List<AccountPasswordInfo> readAllEncryptedInfo()
    {
        List<AccountPasswordInfo> infos = new ArrayList<>();
        PasswordDataBaseHelper helper = new PasswordDataBaseHelper(MyApplication.getContext(),DATABASE_ACCOUNT_PASSWORD,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PASSWORD_INFO + " order by id desc",null);
        if (cursor.moveToFirst())
        {
            do
            {
                AccountPasswordInfo info = readInfoFromCursor(cursor);
                infos.add(info);
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
        return infos;
    }


    private static AccountPasswordInfo readInfoFromCursor(Cursor cursor)
    {
        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
        String accountCategory = cursor.getString(cursor.getColumnIndex("accountCategory"));
        String accountName = cursor.getString(cursor.getColumnIndex("accountName"));
        String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
        String bindPhoneNumber = cursor.getString(cursor.getColumnIndex("bindPhoneNumber"));
        String bindMailbox = cursor.getString(cursor.getColumnIndex("bindMailbox"));
        String bindCooperationCategory = cursor.getString(cursor.getColumnIndex("bindCooperationCategory"));
        String bindCooperationAccountName = cursor.getString(cursor.getColumnIndex("bindCooperationAccountName"));
        String bindCooperationPassword = cursor.getString(cursor.getColumnIndex("bindCooperationPassword"));
        String bindCooperationNote = cursor.getString(cursor.getColumnIndex("bindCooperationNote"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String note = cursor.getString(cursor.getColumnIndex("note"));
        String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
        return new AccountPasswordInfo(id,accountCategory,accountName,nickname,
                bindPhoneNumber,bindMailbox,
                bindCooperationCategory,bindCooperationAccountName,bindCooperationPassword,bindCooperationNote,
                password,note,updateTime);
    }

    public static List<AccountPasswordHistoryInfo> searchHistory(int indexId)
    {
        PasswordDataBaseHelper helper = new PasswordDataBaseHelper(MyApplication.getContext(),DATABASE_ACCOUNT_PASSWORD,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        List<AccountPasswordHistoryInfo> historyInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HISTORY_INFO + " where indexId = " + String.valueOf(indexId),null);
        if (cursor.moveToFirst())
        {
            do
            {
                AccountPasswordHistoryInfo decryptHistoryInfo = MyEncryption.decryptAccountPasswordHistoryInfo(readHistoryInfoFromCursor(cursor),RSAKey.getSavePrivateKey());
                historyInfos.add(decryptHistoryInfo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        helper.close();
        return historyInfos;
    }

    private static AccountPasswordHistoryInfo readHistoryInfoFromCursor(Cursor cursor)
    {
        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
        Integer indexId = cursor.getInt(cursor.getColumnIndex("indexId"));
        String accountCategory = cursor.getString(cursor.getColumnIndex("accountCategory"));
        String accountName = cursor.getString(cursor.getColumnIndex("accountName"));
        String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
        String bindPhoneNumber = cursor.getString(cursor.getColumnIndex("bindPhoneNumber"));
        String bindMailbox = cursor.getString(cursor.getColumnIndex("bindMailbox"));
        String bindCooperationCategory = cursor.getString(cursor.getColumnIndex("bindCooperationCategory"));
        String bindCooperationAccountName = cursor.getString(cursor.getColumnIndex("bindCooperationAccountName"));
        String bindCooperationPassword = cursor.getString(cursor.getColumnIndex("bindCooperationPassword"));
        String bindCooperationNote = cursor.getString(cursor.getColumnIndex("bindCooperationNote"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String note = cursor.getString(cursor.getColumnIndex("note"));
        String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
        return new AccountPasswordHistoryInfo(id,indexId,accountCategory,accountName,nickname,
                bindPhoneNumber,bindMailbox,
                bindCooperationCategory,bindCooperationAccountName,bindCooperationPassword,bindCooperationNote,
                password,note,updateTime);
    }

//
//    private static String dislodgeEmptyString(String s)
//    {
//        return (s == null || "".equals(s)) ? null : s;
//    }
}
