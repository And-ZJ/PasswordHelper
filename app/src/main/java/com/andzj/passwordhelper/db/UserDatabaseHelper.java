package com.andzj.passwordhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.utils.MyApplication;

/**
 * Created by zj on 2017/1/28.
 */

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public static String TAG = "UserDatabaseHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_USER = "User.db";
    public static final String TABLE_USER_KEY = "UserKey";

    private static final String CREATE_USER_KEY = "create table " + TABLE_USER_KEY + " ("
            + "id integer primary key autoincrement, "
            + "savePublicKey        text, "
            + "savePrivateKey       text, "
            + "loginPassword        text, "
            + "passwordHint         text, "
            + "emailAddress         text, "
            + "updateTime           text) ";

    public UserDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static UserInfo getUserInfo()
    {
        UserDatabaseHelper helper = new UserDatabaseHelper(MyApplication.getContext(),DATABASE_USER,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_USER_KEY + " order by id desc",null);
        if (cursor.moveToFirst())
        {
            UserInfo info = readInfoFromCursor(cursor);
            //MyLog.d(TAG,String.valueOf(info.getId()),false);
            cursor.close();
            db.close();
            helper.close();
            return info;
        }
        cursor.close();
        db.close();
        helper.close();
        return null;
    }

    public static UserInfo insert(UserInfo userInfo)
    {
        if (userInfo == null)
        {
            return null;
        }
        UserDatabaseHelper helper = new UserDatabaseHelper(MyApplication.getContext(),DATABASE_USER,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        userInfo.dislodgeEmptyString();
        ContentValues values = new ContentValues();
        values.put("savePublicKey",userInfo.getSavePublicKey());
        values.put("savePrivateKey",userInfo.getSavePrivateKey());
        values.put("loginPassword",userInfo.getLoginPassword());
        values.put("passwordHint",userInfo.getPasswordHint());
        values.put("emailAddress",userInfo.getEmailAddress());
        values.put("updateTime",userInfo.getUpdateTime());
        if (db.insert(TABLE_USER_KEY,null,values) >0)
        {
            values.clear();
            Cursor cursor = db.rawQuery("select * from " + TABLE_USER_KEY + " order by id desc",null);
            if (cursor.moveToFirst())
            {
                UserInfo info = readInfoFromCursor(cursor);
                cursor.close();
                db.close();
                helper.close();
                return info;
            }
            db.close();
            helper.close();
            return userInfo;
        }
        values.clear();
        db.close();
        helper.close();
        return null;
    }

    public static UserInfo update(UserInfo userInfo)
    {
        if (userInfo == null || userInfo.getId() < 0)
        {
            return null;
        }
        UserDatabaseHelper helper = new UserDatabaseHelper(MyApplication.getContext(),DATABASE_USER,null,DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        userInfo.dislodgeEmptyString();
        ContentValues values = new ContentValues();
        values.put("savePublicKey",userInfo.getSavePublicKey());
        values.put("savePrivateKey",userInfo.getSavePrivateKey());
        values.put("loginPassword",userInfo.getLoginPassword());
        values.put("passwordHint",userInfo.getPasswordHint());
        values.put("emailAddress",userInfo.getEmailAddress());
        values.put("updateTime",userInfo.getUpdateTime());
        if (db.update(TABLE_USER_KEY,values,"id = ?",new String[]{String.valueOf(userInfo.getId())}) >0)
        {
            values.clear();
            db.close();
            helper.close();
            return userInfo;
        }
        values.clear();
        db.close();
        helper.close();
        return null;
    }

    private static UserInfo readInfoFromCursor(Cursor cursor)
    {
        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
        String publicKey = cursor.getString(cursor.getColumnIndex("savePublicKey"));
        String privateKey = cursor.getString(cursor.getColumnIndex("savePrivateKey"));
        String loginPassword = cursor.getString(cursor.getColumnIndex("loginPassword"));
        String passwordHint = cursor.getString(cursor.getColumnIndex("passwordHint"));
        String emailAddress = cursor.getString(cursor.getColumnIndex("emailAddress"));
        String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
        return new UserInfo(id,publicKey,privateKey,loginPassword,passwordHint,emailAddress,updateTime);
    }

}
