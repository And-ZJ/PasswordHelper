package com.andzj.passwordhelper.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.andzj.passwordhelper.activity.ActivityCollector;
import com.andzj.passwordhelper.encrypt.Base64Utils;
import com.andzj.passwordhelper.encrypt.RSAUtils;
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.db.PasswordDataBaseHelper;
import com.andzj.passwordhelper.db.UserDatabaseHelper;
import com.andzj.passwordhelper.log.CrashHandler;

import java.security.KeyPair;
import java.util.Date;


/**
 * Created by zj on 2016/7/17.
 */
public class MyApplication extends Application
{
    private static Context context;

    public static String DIR_APPLICATION_NAME = "PasswordHelper";
    public static String DIR_EXPORT_BACKUP_NAME = "/BackupFile/";
    public static String DIR_EXPORT_PASSWORD_RECOVERY_NAME = "/PasswordRecoveryFile/";
    public static String DIR_LOG_NAME = "/Log/";

    @Override
    public void onCreate()
    {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        context = getApplicationContext();
        crashHandler.init(context);
    }

    public static Context  getContext()
    {
        return context;
    }

    public static AccountPasswordInfo developerInfo = new AccountPasswordInfo(0,"邮箱","ZJ.Cosmos@gmail.com",null,null,null,null,null,null,null,null,"本软件开发者的联系方式");

    public static boolean firstLaunchCheck()
    {
        SharedPreferences firstPref = MyApplication.getContext().getSharedPreferences("firstPref", Context.MODE_PRIVATE);
        Boolean firstLaunch = firstPref.getBoolean("first",true);
        if (firstLaunch)
        {
            Toast.makeText(MyApplication.getContext(),"应用首次启动初始化中...,请等候一会",Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor firstEditor = firstPref.edit();
            firstEditor.putBoolean("first",false);
            firstEditor.apply();
            return true;
        }
        return false;
    }

    public static UserInfo systemInit()
    {
        UserInfo userInfo = UserDatabaseHelper.getUserInfo();
        if (userInfo == null)
        {

            UserInfo newUserInfo = new UserInfo();
            UserInfo idInfo = null;
            int wrongCount = 0;
            do
            {
                KeyPair pair = RSAUtils.generateRSAKeyPair();
                newUserInfo.setSavePublicKey(Base64Utils.encode(pair.getPublic().getEncoded()));
                newUserInfo.setSavePrivateKey(Base64Utils.encode(pair.getPrivate().getEncoded()));
                newUserInfo.setUpdateTime(MyTimeUtils.getMyTimeStr(new Date()));
                ++wrongCount;
                if (wrongCount > 20)
                {
                    Toast.makeText(MyApplication.getContext(),"应用错误，无法存储数据库。",Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                }
                idInfo = UserDatabaseHelper.insert(newUserInfo);
            }while (idInfo == null);
            MainActivity.getUserInfo().update(idInfo);
            PasswordDataBaseHelper.insert(developerInfo);
            return newUserInfo;
        }
        else
        {
            MainActivity.getUserInfo().update(userInfo);
            return userInfo;
        }

    }

}
