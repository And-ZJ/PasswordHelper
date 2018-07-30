package com.andzj.passwordhelper.utils;

import android.content.Context;

import android.os.Environment;

import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by zj on 2016/8/25.
 */
public class MyFileOperateUtils
{
    private static String TAG = "MyFileOperateUtils";


    //检查外部存储可读写
    public static boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        Toast.makeText(MyApplication.getContext(),"存储器不可写",Toast.LENGTH_SHORT).show();
        return false;
    }

    //检查外部存储至少可读
    public static boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return true;
        }
        Toast.makeText(MyApplication.getContext(),"存储器不可读",Toast.LENGTH_SHORT).show();
        return false;
    }


    /**
     *
     * @param context = Context
     * @param fileName = "/fileDir1/fileDir2/.../fileName.fileFormat"
     * @return create success or fail
     */
    public static boolean createExternalFileDir(Context context,String fileName)
    {
        if (context == null || fileName == null || !"".equals(fileName))
        {
            return false;
        }
        int i = 0;
        i = fileName.lastIndexOf("/");
        if (i > 0)
        {
            String dir = fileName.substring(0,i+1);
            File fileDir = new File(context.getExternalFilesDir(null),dir);
            return fileDir.exists() || fileDir.mkdirs();
        }
        return true;
    }

    public static File getWritableEmptyExternalFile(Context context, String fileName)
    {
        if (createExternalFileDir(context,fileName))
        {
            try
            {
                File file = new File(context.getExternalFilesDir(null),fileName);
                if (file.exists())
                {
                    file.delete();
                }
                file.createNewFile();
                return file;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static File getWritableExternalFile(Context context,String fileName)
    {
        if (isExternalStorageWritable())
        {
            try
            {
                File file = new File(context.getExternalFilesDir(null),fileName);
                if (file.exists())
                {
                    return file;
                }
                else
                {
                    if (createExternalFileDir(context,fileName))
                    {
                        file.createNewFile();
                        return file;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static File getReadableExternalFile(Context context, String fileName)
    {
        if (fileName == null || "".equals(fileName))
        {
            return null;
        }
        if (isExternalStorageReadable())
        {
            try
            {
                File file = new File(context.getExternalFilesDir(null),fileName);
                if (file.exists())
                {
                    return file;
                }
                //MyLog.d("MyFileOperateUtils","找不到文件:" + fileName,false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //Toast.makeText(context,"找不到文件",Toast.LENGTH_SHORT).show();
        return null;
    }

    private static boolean isExternalFileExisted(Context context,String fileName)
    {
        return getReadableExternalFile(context,fileName) != null;
    }


    public static boolean saveStringDate(Context context, String saveContent, String fileName, boolean append)
    {
        if (context == null || saveContent == null || fileName == null)
        {
            return false;
        }
        File file = getWritableExternalFile(context,fileName);
        if (file == null)
        {
            return false;
        }
        FileOutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(file,append);
            outputStream.write(saveContent.getBytes());
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (outputStream !=null )
                {
                    outputStream.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String readStringData(Context context,String fileName)
    {
        if (context == null || fileName == null)
        {
            //MyLog.d("SearchBookActivity","context|fileName == null",false);
            return null;
        }
        File file = getReadableExternalFile(context,fileName);
        if (file == null)
        {
            //MyLog.d("SearchBookActivity","file == null",false);
            return null;
        }
        FileInputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            //MyLog.d("SearchBookActivity","bytes length =" + String.valueOf(bytes.length),false);
            if (inputStream.read(bytes) != -1)
            {
                //MyLog.d("MyFileOperateUtils","s = " + s,false);
                return new String(bytes);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param publicFileDir = "fileDir"
     * @param fileName = "/fileDir1/fileDir2/.../fileName.fileFormat"
     * @return create success or fail
     */
    public static boolean createExternalStoragePublicFileDir(String publicFileDir,String fileName)
    {
        if (fileName == null || "".equals(fileName))
        {
            return false;
        }
        int i = 0;
        i = fileName.lastIndexOf("/");
        if (i > 0)
        {
            String dir = fileName.substring(0,i+1);
            File fileDir = new File(Environment.getExternalStoragePublicDirectory(publicFileDir),dir);
            return fileDir.exists() || fileDir.mkdirs();
        }
        return true;
    }

    public static File getWritableEmptyExternalStoragePublicFile(String publicFileDir,String fileName)
    {
        if (!isExternalStorageWritable())
        {
            return null;
        }
        if (createExternalStoragePublicFileDir(publicFileDir,fileName))
        {
            File file = new File(Environment.getExternalStoragePublicDirectory(publicFileDir),fileName);
            if (file.exists())
            {
                file.delete();
            }
            try
            {
                file.createNewFile();
                return file;
            }
            catch (Exception e)
            {
                //Toast.makeText(MyApplication.getContext(),"无法创建文件,请赋予存储权限,或检查剩余空间是否足够",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return null;
    }

    private static File getReadableExternalStoragePublicFile(String publicFileDir,String fileName)
    {
        if (!isExternalStorageWritable())
        {
            return null;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(publicFileDir),fileName);
        if (file.exists())
        {
            return file;
        }
        return null;
    }

    public static boolean isExternalStoragePublicFileExisted(String publicFileDir,String fileName)
    {
        return getReadableExternalStoragePublicFile(publicFileDir,fileName) != null;
    }

}
