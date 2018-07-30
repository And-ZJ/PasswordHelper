package com.andzj.passwordhelper.encrypt;

import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.utils.MyApplication;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by zj on 2017/2/11.
 */

public class RSAKey {
    private static PublicKey savePublicKey;
    private static PrivateKey savePrivateKey;

    /**
     * 用于数据库存储的公钥
     * @return 公钥
     */
    public static PublicKey getSavePublicKey()
    {
        if (savePublicKey == null)
        {
            try
            {
                savePublicKey = RSAUtils.getPublicKey(MainActivity.getUserInfo().getSavePublicKey().getBytes());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return savePublicKey;
    }

    /**
     * 用于数据库存储的私钥
     * @return 私钥
     */
    public static PrivateKey getSavePrivateKey()
    {
        if (savePrivateKey == null)
        {
            try
            {
                savePrivateKey = RSAUtils.getPrivateKey(MainActivity.getUserInfo().getSavePrivateKey().getBytes());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return savePrivateKey;
    }


    private static PublicKey userInfoPublicKey;

    /**
     * 加密登录密码等个人信息的公钥，应用内不提供解密
     * @return 公钥
     * @throws Exception 加载出错
     */
    public static PublicKey getUserInfoPublicKey() throws Exception
    {
        if (userInfoPublicKey == null)
        {
            InputStream inPublic = MyApplication.getContext().getResources().openRawResource(R.raw.user_info_public_key);
            userInfoPublicKey =  RSAUtils.loadPublicKey(inPublic);
        }
        return userInfoPublicKey;
    }

    private static PublicKey importPasswordPublicKey;
    private static PrivateKey importPasswordPrivateKey;

    /**
     *  加密导出备份数据中密码的公钥，应用内不提供解密
     * @return 公钥
     * @throws Exception 加载出错
     */
    public static PublicKey getImportPasswordPublicKey() throws Exception
    {
        if (importPasswordPublicKey == null)
        {
            InputStream inPublic = MyApplication.getContext().getResources().openRawResource(R.raw.import_password_public_key);
            importPasswordPublicKey =  RSAUtils.loadPublicKey(inPublic);
        }
        return importPasswordPublicKey;
    }

    private static PublicKey exportDataPublicKey;
    private static PrivateKey exportDataPrivateKey;

    /**
     * 加密导出备份数据帐号密码的公钥
     * @return 公钥
     * @throws Exception 加载出错
     */
    public static PublicKey getExportDataPublicKey() throws Exception
    {
        if (exportDataPublicKey == null)
        {
            InputStream inPublic = MyApplication.getContext().getResources().openRawResource(R.raw.export_data_public_key);
            exportDataPublicKey = RSAUtils.loadPublicKey(inPublic);
        }
        return exportDataPublicKey;
    }

    /**
     * 解密导出备份数据帐号密码的私钥
     * @return 私钥
     * @throws Exception 加载出错
     */
    public static PrivateKey getExportDataPrivateKey() throws Exception
    {
        if (exportDataPrivateKey == null)
        {
            InputStream inPrivate = MyApplication.getContext().getResources().openRawResource(R.raw.export_data_private_key);
            exportDataPrivateKey =  RSAUtils.loadPrivateKey(inPrivate);
        }
        return exportDataPrivateKey;
    }

}
