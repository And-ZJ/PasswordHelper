package com.andzj.passwordhelper.encrypt;

import com.andzj.passwordhelper.encrypt.Base64Utils;
import com.andzj.passwordhelper.encrypt.RSAUtils;
import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.activity.MainActivity;
import com.andzj.passwordhelper.bean.AccountPasswordHistoryInfo;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.utils.MyApplication;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by zj on 2017/1/26.
 * 一些加密操作
 */

public class MyEncryption {

    public static String TAG = "MyEncryption";

    /**
     * 加密帐号密码信息，用于存储到数据库中
     * @param unEncryptInfo 未被加密的信息
     * @param publicKey 加密公钥
     * @return 返回被各字段加密的对象
     */
    public static AccountPasswordInfo encryptAccountPasswordInfo(AccountPasswordInfo unEncryptInfo, PublicKey publicKey)
    {
        unEncryptInfo.dislodgeEmptyString();
        //System.out.print("加密前：\n" + unEncryptInfo.toString());
        AccountPasswordInfo encryptedInfo = new AccountPasswordInfo();
        encryptedInfo.setId(unEncryptInfo.getId());
        encryptedInfo.setAccountCategory(encryptDislodgeEmptyString(unEncryptInfo.getAccountCategory(),publicKey));
        encryptedInfo.setAccountName(encryptDislodgeEmptyString(unEncryptInfo.getAccountName(),publicKey));
        encryptedInfo.setNickname(encryptDislodgeEmptyString(unEncryptInfo.getNickname(),publicKey));
        encryptedInfo.setBindPhoneNumber(encryptDislodgeEmptyString(unEncryptInfo.getBindPhoneNumber(),publicKey));
        encryptedInfo.setBindMailbox(encryptDislodgeEmptyString(unEncryptInfo.getBindMailbox(),publicKey));
        encryptedInfo.setBindCooperationCategory(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationCategory(),publicKey));
        encryptedInfo.setBindCooperationAccountName(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationAccountName(),publicKey));
        encryptedInfo.setBindCooperationPassword(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationPassword(),publicKey));
        encryptedInfo.setBindCooperationNote(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationNote(),publicKey));
        encryptedInfo.setPassword(encryptDislodgeEmptyString(unEncryptInfo.getPassword(),publicKey));
        encryptedInfo.setNote(encryptDislodgeEmptyString(unEncryptInfo.getNote(),publicKey));
        encryptedInfo.setUpdateTime(unEncryptInfo.getUpdateTime());
        //System.out.print("加密后：\n" + encryptedInfo.toString());
        return encryptedInfo;
    }

    /**
     * 从数据库中读取后，用此解密每个加密字段
     * @param encryptedInfo 被加密的对象
     * @param privateKey 解密私钥
     * @return 已解密的对象
     */
    public static AccountPasswordInfo decryptAccountPasswordInfo(AccountPasswordInfo encryptedInfo, PrivateKey privateKey)
    {
        encryptedInfo.dislodgeEmptyString();
        //System.out.print("解密前：\n" + encryptedInfo.toString());
        AccountPasswordInfo decryptedInfo = new AccountPasswordInfo();
        decryptedInfo.setId(encryptedInfo.getId());
        decryptedInfo.setAccountCategory(decryptDislodgeEmptyString(encryptedInfo.getAccountCategory(),privateKey));
        decryptedInfo.setAccountName(decryptDislodgeEmptyString(encryptedInfo.getAccountName(),privateKey));
        decryptedInfo.setNickname(decryptDislodgeEmptyString(encryptedInfo.getNickname(),privateKey));
        decryptedInfo.setBindPhoneNumber(decryptDislodgeEmptyString(encryptedInfo.getBindPhoneNumber(),privateKey));
        decryptedInfo.setBindMailbox(decryptDislodgeEmptyString(encryptedInfo.getBindMailbox(),privateKey));
        decryptedInfo.setBindCooperationCategory(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationCategory(),privateKey));
        decryptedInfo.setBindCooperationAccountName(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationAccountName(),privateKey));
        decryptedInfo.setBindCooperationPassword(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationPassword(),privateKey));
        decryptedInfo.setBindCooperationNote(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationNote(),privateKey));
        decryptedInfo.setPassword(decryptDislodgeEmptyString(encryptedInfo.getPassword(),privateKey));
        decryptedInfo.setNote(decryptDislodgeEmptyString(encryptedInfo.getNote(),privateKey));
        decryptedInfo.setUpdateTime(encryptedInfo.getUpdateTime());
        //System.out.print("解密后：\n" + decryptedInfo.toString());
        return decryptedInfo;
    }

    /**
     * 加密帐号密码历史对象
     * @param unEncryptInfo 未加密的帐号密码历史对象
     * @param publicKey 加密公钥
     * @return 已加密各个字段的对象
     */
    public static AccountPasswordHistoryInfo encryptAccountPasswordHistoryInfo(AccountPasswordHistoryInfo unEncryptInfo, PublicKey publicKey)
    {
        unEncryptInfo.dislodgeEmptyString();
        AccountPasswordHistoryInfo encryptedInfo = new AccountPasswordHistoryInfo();
        encryptedInfo.setId(unEncryptInfo.getId());
        encryptedInfo.setIndexId(unEncryptInfo.getIndexId());
        encryptedInfo.setAccountCategory(encryptDislodgeEmptyString(unEncryptInfo.getAccountCategory(),publicKey));
        encryptedInfo.setAccountName(encryptDislodgeEmptyString(unEncryptInfo.getAccountName(),publicKey));
        encryptedInfo.setNickname(encryptDislodgeEmptyString(unEncryptInfo.getNickname(),publicKey));
        encryptedInfo.setBindPhoneNumber(encryptDislodgeEmptyString(unEncryptInfo.getBindPhoneNumber(),publicKey));
        encryptedInfo.setBindMailbox(encryptDislodgeEmptyString(unEncryptInfo.getBindMailbox(),publicKey));
        encryptedInfo.setBindCooperationCategory(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationCategory(),publicKey));
        encryptedInfo.setBindCooperationAccountName(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationAccountName(),publicKey));
        encryptedInfo.setBindCooperationPassword(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationPassword(),publicKey));
        encryptedInfo.setBindCooperationNote(encryptDislodgeEmptyString(unEncryptInfo.getBindCooperationNote(),publicKey));
        encryptedInfo.setPassword(encryptDislodgeEmptyString(unEncryptInfo.getPassword(),publicKey));
        encryptedInfo.setNote(encryptDislodgeEmptyString(unEncryptInfo.getNote(),publicKey));
        encryptedInfo.setUpdateTime(unEncryptInfo.getUpdateTime());
        return encryptedInfo;
    }

    /**
     * 解密帐号密码历史对象
     * @param encryptedInfo 被加密的对象
     * @param privateKey 解密私钥
     * @return 各个字段已解密的对象
     */
    public static AccountPasswordHistoryInfo decryptAccountPasswordHistoryInfo(AccountPasswordHistoryInfo encryptedInfo, PrivateKey privateKey)
    {
        encryptedInfo.dislodgeEmptyString();
        AccountPasswordHistoryInfo decryptedInfo = new AccountPasswordHistoryInfo();
        decryptedInfo.setId(encryptedInfo.getId());
        decryptedInfo.setIndexId(encryptedInfo.getIndexId());
        decryptedInfo.setAccountCategory(decryptDislodgeEmptyString(encryptedInfo.getAccountCategory(),privateKey));
        decryptedInfo.setAccountName(decryptDislodgeEmptyString(encryptedInfo.getAccountName(),privateKey));
        decryptedInfo.setNickname(decryptDislodgeEmptyString(encryptedInfo.getNickname(),privateKey));
        decryptedInfo.setBindPhoneNumber(decryptDislodgeEmptyString(encryptedInfo.getBindPhoneNumber(),privateKey));
        decryptedInfo.setBindMailbox(decryptDislodgeEmptyString(encryptedInfo.getBindMailbox(),privateKey));
        decryptedInfo.setBindCooperationCategory(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationCategory(),privateKey));
        decryptedInfo.setBindCooperationAccountName(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationAccountName(),privateKey));
        decryptedInfo.setBindCooperationPassword(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationPassword(),privateKey));
        decryptedInfo.setBindCooperationNote(decryptDislodgeEmptyString(encryptedInfo.getBindCooperationNote(),privateKey));
        decryptedInfo.setPassword(decryptDislodgeEmptyString(encryptedInfo.getPassword(),privateKey));
        decryptedInfo.setNote(decryptDislodgeEmptyString(encryptedInfo.getNote(),privateKey));
        decryptedInfo.setUpdateTime(encryptedInfo.getUpdateTime());
        return decryptedInfo;
    }

    /**
     * 导出备份文件的密码加密，使用importPassword公钥加密，应用内不提供私钥解密
     * @param password 用户输入的密码
     * @param emailAddress 校验身份使用的邮箱
     * @return 已加密的密码字符串
     */
    public static String encryptExportPassword(String password,String emailAddress)
    {
        byte[] passwordBytes = password.getBytes();
        int passwordLength = passwordBytes.length;
        byte[] emailBytes = emailAddress.getBytes();
        int emailLength = emailBytes.length;
        for (int i=0;i<passwordLength;i++)
        {
            passwordBytes[i] = (byte)( passwordBytes[i] ^ emailBytes[i%emailLength]);
        }
        try
        {
            byte[] encryptPassword = RSAUtils.encryptData(passwordBytes,RSAKey.getImportPasswordPublicKey());
            if (encryptPassword != null && encryptPassword.length >0)
            {
                return Base64Utils.encode(encryptPassword);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 加密字符串操作
     * @param unEncryptStr 未加密的字符串，长度不限制
     * @param publicKey 加密公钥
     * @return 已加密的字符串
     */
    public static String encryptDislodgeEmptyString(String unEncryptStr,PublicKey publicKey)
    {
        if (!isEmpty(unEncryptStr))
        {
            int cutLength = 50;
            StringBuffer sb = new StringBuffer(unEncryptStr);
            String sb2;
            StringBuffer encrypt = new StringBuffer();
            while(sb.length() > cutLength)
            {
                sb2 = sb.substring(0,cutLength);
                sb.delete(0,cutLength);
                sb.trimToSize();
                byte[] encryptByte =  RSAUtils.encryptData(sb2.getBytes(),publicKey);
                if (encryptByte != null && encryptByte.length > 0)
                {
                    encrypt.append(Base64Utils.encode(encryptByte)).append("~");
                }
                else
                {
                    return null;
                }
            }
            byte[] encryptByte =  RSAUtils.encryptData(sb.toString().getBytes(),publicKey);
            if (encryptByte != null && encryptByte.length > 0)
            {
                encrypt.append(Base64Utils.encode(encryptByte));
                return encrypt.toString();
            }
//            byte[] encryptByte =  RSAUtils.encryptData(unEncryptStr.getBytes(),publicKey);
//            if (encryptByte !=null && encryptByte.length > 0)
//            {
//                return Base64Utils.encode(encryptByte);
//            }
        }
        return null;
    }


    /**
     * 解密字符串操作
     * @param encryptedStr 已加密的字符串
     * @param privateKey 解密私钥
     * @return 解密的字符串
     */
    public static String decryptDislodgeEmptyString(String encryptedStr,PrivateKey privateKey)
    {
        if (!isEmpty(encryptedStr))
        {
            StringBuffer decrypt = new StringBuffer();
            String[] split = encryptedStr.split("~");
            for (String s : split)
            {
                byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(s),privateKey);
                if (decryptByte != null && decryptByte.length >0)
                {
                    decrypt.append(new String(decryptByte));
                    //return new String(decryptByte);
                }
                else
                {
                    return null;
                }
            }
            return decrypt.toString();
        }
        return null;
    }

    /**
     * 判断某个字符串是否为空，判断标准，null 或 length == 0
     * @param s 待判断的字符串s
     * @return true 空
     *         false 不空
     */
    private static  boolean isEmpty(String s)
    {
        return s == null || s.length() == 0;
    }
}
