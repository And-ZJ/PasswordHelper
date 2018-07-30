package com.andzj.passwordhelper.encrypt;



import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by zj on 2017/2/15.
 * 用于加密导出文件中的帐号密码
 */

public class ExportEncryption {
    private byte[] passwordBytes;
    private byte[] emailBytes;
    public ExportEncryption(String passwordStr,String emailStr)
    {
        setEmailBytes(emailStr);
        setPasswordBytes(passwordStr);
    }

    private void setEmailBytes(String emailStr)
    {
        if (emailStr == null || "".equals(emailStr))
        {
            emailBytes = new byte[1];
            emailBytes[0] = (byte) 1;
            return;
        }
        emailBytes = emailStr.getBytes();
    }

    private void setPasswordBytes(String passwordStr)
    {
        if (passwordStr == null || "".equals(passwordStr))
        {
            passwordBytes = new byte[1];
            passwordBytes[0] = (byte) 1;
            return;
        }
        passwordBytes = passwordStr.getBytes();

    }

    private byte[] emailEncrypt(byte[] orgBytes)
    {
        int emailLength = emailBytes.length;
        for (int i=0;i <orgBytes.length;i++)
        {
            orgBytes[i] = (byte) (orgBytes[i] ^ emailBytes[i%emailLength]);
        }
        return orgBytes;
    }

    private byte[] emailDecrypt(byte[] orgBytes)
    {
        int emailLength = emailBytes.length;
        for (int i=0;i<orgBytes.length;i++)
        {
            orgBytes[i] = (byte) (orgBytes[i] ^ emailBytes[i%emailLength]);
        }
        return orgBytes;
    }

    private byte[] passwordEncrypt(byte[] orgBytes)
    {
        int passwordLength = passwordBytes.length;
        for (int i=0;i <orgBytes.length;i++)
        {
            orgBytes[i] = (byte) (orgBytes[i] ^ passwordBytes[i%passwordLength]);
        }
        return orgBytes;
    }

    private byte[] passwordDecrypt(byte[] orgBytes)
    {
        //System.out.println("len:"+ orgBytes.length);
        int passwordLength = passwordBytes.length;
        for (int i=0;i<orgBytes.length;i++)
        {
            orgBytes[i] = (byte) (orgBytes[i] ^ passwordBytes[i%passwordLength]);
        }
        return orgBytes;
    }

    private byte[] RSAEncrypt(byte[] orgBytes, PublicKey publicKey)
    {
        return RSAUtils.encryptData(orgBytes,publicKey);
    }

    private byte[] RSADecrypt(byte[] orgBytes, PrivateKey privateKey)
    {
        return RSAUtils.decryptData(orgBytes,privateKey);
    }

    private String Base64Encode(byte[] orgBytes)
    {
        return Base64Utils.encode(orgBytes);
    }

    private byte[] Base64Decode(String orgStr)
    {
        return Base64Utils.decode(orgStr);
    }

    public String encrypt(String data,PublicKey publicKey)
    {
        int cutLength = 50;
        //////////////////////////////////////////////////////////////
        //System.out.println("length:"+String.valueOf(data.getBytes().length) + " " +data);
        StringBuffer sb = new StringBuffer(data);
        String sb2;// = new String();
        StringBuffer encrypt = new StringBuffer();
        while(sb.length() > cutLength)
        {
            sb2 = sb.substring(0,cutLength);
            sb.delete(0,cutLength);sb.trimToSize();
            //System.out.println("length:"+String.valueOf(sb2.getBytes().length));
            encrypt.append(encryptStr(sb2,publicKey)).append("~");
        }
        //System.out.println("length:"+String.valueOf(sb.toString().getBytes().length));
        encrypt.append(encryptStr(sb.toString(),publicKey));
        return encrypt.toString();
    }

    private String encryptStr(String smallData,PublicKey publicKey)
    {
        return Base64Encode(passwordEncrypt(emailEncrypt(RSAEncrypt(smallData.getBytes(),publicKey))));
    }

    public String decrypt(String data,PrivateKey privateKey)
    {
        StringBuffer decrypt = new StringBuffer();
        String[] split = data.split("~");
        for (String s : split)
        {
            decrypt.append(decryptStr(s, privateKey));
        }
        return decrypt.toString();
    }

    private String decryptStr(String smallData,PrivateKey privateKey)
    {
        //System.out.println("len:"+ smallData.length() + " D:"+smallData);
        return new String(RSADecrypt(emailDecrypt(passwordDecrypt(Base64Decode(smallData))),privateKey));
    }

}
