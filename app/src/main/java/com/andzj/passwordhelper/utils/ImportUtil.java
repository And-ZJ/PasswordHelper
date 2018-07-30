package com.andzj.passwordhelper.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.bean.EncryptionInfo;
import com.andzj.passwordhelper.db.PasswordDatabase;
import com.andzj.passwordhelper.encrypt.ExportEncryption;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.encrypt.RSAKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zj on 2017/2/2.
 */

public class ImportUtil {

    /**
     * 识别备份文件，如果能识别，读取密码，邮箱，密码提示等信息
     * @param filename 文件名
     * @return map对象
     *            未被识别，出错信息填写在 error 键中
     *            被识别，写入 encryptPassword,emailAddress,passwordHint 等键
     */
    public static Map<String,String> distinguishImportFile(String filename)
    {
        Map<String,String> map = new HashMap<>();
        if (filename == null)
        {
            map.put("error","文件名为空");
            //Toast.makeText(context,"文件名为空",Toast.LENGTH_SHORT).show();
            return map;
        }
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String encryptPassword = null;
        String emailAddress = null;
        String passwordHint = null;
        try
        {
            fileReader = new FileReader(filename);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null)
            {
                if (line.charAt(0) == '!')
                {
                    char num = line.charAt(1);
                    if (num == '1')
                    {
                        if (!line.substring(7).equals("PasswordHelper"))
                        {
                            break;
                        }
                    }
                    else if (num == '2')
                    {
                        if (!line.substring(8,18).equals("BackupFile"))
                        {
                            break;
                        }
                    }
                    else if (num == '3')
                    {
                        encryptPassword = line.substring(3);
                    }
                    else if (num == '4')
                    {
                        passwordHint = MyEncryption.decryptDislodgeEmptyString(line.substring(3), RSAKey.getExportDataPrivateKey());
                    }
                    else if (num == '5')
                    {
                        emailAddress = MyEncryption.decryptDislodgeEmptyString(line.substring(3),RSAKey.getExportDataPrivateKey());
                        break;
                    }
                }
                line =  bufferedReader.readLine();
            }
            if (encryptPassword == null || passwordHint == null || emailAddress == null)
            {
                map.put("error","此文件非本应用导出的文件，请重新选择");
            }
            else
            {
                map.put("success","文件已被识别，请输入密码");
                map.put("encryptPassword",encryptPassword);
                map.put("passwordHint",passwordHint);
                map.put("emailAddress",emailAddress);
            }
        }
        catch (Exception e)
        {
            //Toast.makeText(ImportBackupFileActivity.this,"文件打开失败",Toast.LENGTH_SHORT).show();
            map.put("error","文件打开失败，请赋予存储权限！");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fileReader != null)
                {
                    fileReader.close();
                }
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            }
            catch (Exception e)
            {
                //Toast.makeText(ImportBackupFileActivity.this,"关闭文件时出现异常",Toast.LENGTH_SHORT).show();
                map.put("error","关闭文件时出现异常");
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 读取备份文件信息，存储到数据库中，前提是已识别此文件
     * @param context   必要context对象，用于Toast和ProgressDialog
     * @param filename  备份文件名
     * @param password  解密用的密码
     * @param emailAddress  解密用的邮件地址
     * @return  true： 成功导入到数据库
     *          false：导入失败
     */
    public static boolean importBackupFileOperation( Context context,String filename, String password, String emailAddress)
    {
        boolean fileReadCompleted = false;
        if (password == null || emailAddress == null)
        {
            Toast.makeText(context,"数据出错，无法解析",Toast.LENGTH_SHORT).show();
            return false;
        }
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        PasswordDatabase passwordDatabase = null;
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在导入，请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.show();
        try
        {
            fileReader = new FileReader(filename);
            bufferedReader = new BufferedReader(fileReader);
            PrivateKey importDataPrivateKey = RSAKey.getExportDataPrivateKey();
            ExportEncryption exportEncryption = new ExportEncryption(password,emailAddress);
            String line = bufferedReader.readLine();
            passwordDatabase = new PasswordDatabase();
            int count =0;
            int wrongCount = 0;
            while (line != null)
            {
                char first = line.charAt(0);
                if (first !='!' && first != '-')
                {
                    String jsonInfo = exportEncryption.decrypt(line,importDataPrivateKey);
                    EncryptionInfo info = JSON.parseObject(jsonInfo,EncryptionInfo.class);
                    ++count;
                    progressDialog.setTitle("正在导入第"+ String.valueOf(count) + "条数据");
                    if (!passwordDatabase.insert(new AccountPasswordInfo(info)))
                    {
                        wrongCount++;
                    }
                }
                line =  bufferedReader.readLine();
            }
            if (count == 0)
            {
                Toast.makeText(context, "未读取到任何有效数据", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(context, "导入"+ count + "条数据，其中失败" + wrongCount + "条", Toast.LENGTH_SHORT).show();
                fileReadCompleted = true;
            }
            passwordDatabase.close();
            progressDialog.dismiss();
        }
        catch (Exception e)
        {
            Toast.makeText(context,"读取时出现异常",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (passwordDatabase != null)
                {
                    passwordDatabase.close();
                }
                if (fileReader != null)
                {
                    fileReader.close();
                }
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(context,"关闭文件时出现异常",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        progressDialog.dismiss();
        return fileReadCompleted;
    }
}
