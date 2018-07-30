package com.andzj.passwordhelper.encrypt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.bean.EncryptionInfo;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zj on 2017/2/11.
 */

public class PasswordEncryption {



    public static String encryptInfo(String passwordStr, String dateStr, AccountPasswordInfo info)
    {
        byte[] passwordInt = getPasswordBytes(passwordStr);
        EncryptionInfo unEncryptedInfo = new EncryptionInfo(info);
        String unEncryptedStr = JSON.toJSONString(unEncryptedInfo);
        try
        {
            PublicKey exportDataPublicKey = RSAKey.getExportDataPublicKey();

            StringBuffer buffer = new StringBuffer(unEncryptedStr);
            StringBuilder builder = new StringBuilder();
            while (buffer.length() / 1024 > 1)
            {
                String b = buffer.substring(0,1024);
                if (!b.isEmpty())
                {
                    byte[] encryptBytes = RSAUtils.encryptData(b.getBytes(),exportDataPublicKey);

                    int sum = 0;
                    //for (int i=0;i<;i++)
                    builder.append(Base64Utils.encode(encryptBytes));
                    buffer.delete(0,1024);
                }
            }
            byte[] encryptBytes = RSAUtils.encryptData(buffer.toString().getBytes(),exportDataPublicKey);
            builder.append(Base64Utils.encode(encryptBytes));


            return builder.toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] dateEncrypt(byte[] dateBytes,byte[] encryptBytes)
    {
        for (int i =0;i<dateBytes.length;i++)
        {

        }
        return null;
    }


    private static String password;
    private static byte[] passwordBytes;

    private static byte[] getPasswordBytes(String password)
    {
        if (password != null && !"".equals(password) )
        {
            if (PasswordEncryption.password == null || !password.equals(PasswordEncryption.password))
            {
                PasswordEncryption.password = password;
                if (password.length() == 1)
                {
                    PasswordEncryption.passwordBytes = new byte[1];
                    passwordBytes[0] = (byte) Integer.parseInt(password);
                }
                else
                {
                    PasswordEncryption.passwordBytes = new byte[password.length()-1];
                    for (int i=0;i<password.length()-1;i++)
                    {
                        PasswordEncryption.passwordBytes[i] = (byte) Integer.parseInt(password.substring(i,i+2));
                    }
                }
            }
        }
        else
        {
            PasswordEncryption.passwordBytes = new byte[1];
            PasswordEncryption.passwordBytes[0] = 1;
        }

        return passwordBytes;
    }

    private static String dateStr;
    private static byte[] dateBytes;

    private static byte[] getDateBytes(String dateStr)
    {
        return null;
    }

    private static byte[] praseDateNumber(String dateStr)
    {
        byte[] dateByte = dateStr.getBytes();
        List<Byte> bytes = new ArrayList<>();
        for (byte b:dateByte)
        {
            if ( (int)b >= '0' || (int)b <= '9')
            {
                bytes.add(b);
            }
        }
        if (bytes.size()>0)
        {
            byte[] numBytes = new byte[bytes.size()];
            for (int i=0;i<bytes.size();i++)
            {
                numBytes[i] = bytes.get(i);
            }
            return numBytes;
        }
        byte[] oneBytes = new byte[1];
        oneBytes[0] = 1;
        return oneBytes;
    }


}
