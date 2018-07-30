package com.andzj.passwordhelper.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.andzj.passwordhelper.utils.MyApplication;
import com.andzj.passwordhelper.utils.MyFileOperateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ZJ on 2017/4/9.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler{

    public static String TAG="CrashHandler";

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());

    //系统默认的UncaughtException处理器
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler INSTANCE = new CrashHandler();

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    private Context mContext;

    private Map<String, String> infos = new HashMap<String, String>();

    private CrashHandler(){}

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                MyLog.e(TAG, "error when sleep thread!");
            }
            //退出程序
            //mDefaultHandler.uncaughtException(thread, ex);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }



    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //收集设备参数信息
                collectDeviceInfo(mContext);
                //保存日志文件
                String filepath = saveCrashInfo2File(ex);
                if (filepath == null)
                {
                    Toast.makeText(MyApplication.getContext(),"很抱歉,程序出现异常,即将退出.\n无法生成日志，缺少存储权限。",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.\n烦请您将日志文件发送给作者分析，谢谢！\n" + filepath, Toast.LENGTH_LONG).show();
                }
                Looper.loop();
            }
        }.start();

        return true;
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            MyLog.e(TAG, "an error occured when collect package info");
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                MyLog.d(TAG, field.getName() + " : " + field.get(null));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                MyLog.e(TAG, "an error occured when collect crash info");
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        String fileName = "闪退日志-" + time + "-" + timestamp + ".log";
        File logFile = MyFileOperateUtils.getWritableEmptyExternalStoragePublicFile(MyApplication.DIR_APPLICATION_NAME,MyApplication.DIR_LOG_NAME + fileName);
        if (logFile == null)
        {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();//cause 干啥用的？
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try
        {
            FileOutputStream fos = new FileOutputStream(logFile);
            fos.write(sb.toString().getBytes());
            fos.close();
            return logFile.getAbsolutePath();
        }
        catch (Exception e) {
            e.printStackTrace();
            MyLog.e(TAG, "an error occured while writing file...");
        }
        return null;
    }

    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}
