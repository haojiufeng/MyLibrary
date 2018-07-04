package com.commonlibrary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * 客户端运行异常崩溃数据扑捉异常保存SD卡或者实时投递服务器工具类,
 * 在Application中调用CrashHandler.getInstance().setCustomCrashInfo(this);
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final int TYPE_SAVE_SDCARD = 1; //崩溃日志保存本地SDCard  --建议开发模式使用
    private static final int TYPE_SAVE_REMOTE = 2; //崩溃日志保存远端服务器 --建议生产模式使用
    private static final int TYPE_SAVE_EMAIL = 3; //崩溃日志发送邮件 --建议生产模式使用

    private int type_save = 1;  //崩溃保存日志模式 默认为2，采用保存Web服务器
    private static final String CRASH_SAVE_SDPATH = "sdcard/qlwl_cache/"; //崩溃日志SD卡保存路径
    private static final String CARSH_LOG_DELIVER = "http://www.baidu.com";
    private static CrashHandler instance = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    /**
     * @return
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc) 进行重写捕捉异常
     * @see
     * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
     * .Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (type_save == TYPE_SAVE_SDCARD) {
            // 1,保存信息到sdcard中
            saveToSdcard(mContext, ex);
        } else if (type_save == TYPE_SAVE_REMOTE) {
            // 2,异常崩溃信息投递到服务器
            saveToServer(mContext, ex);
        } else if (type_save == TYPE_SAVE_EMAIL) {
            sendErrorInfoEmail(mContext, ex);
        }
        // 3,应用准备退出
        showToast(mContext, "很抱歉,程序发生异常,即将退出.");
        SystemClock.sleep(3500);
//        for (int i = ActivityStack.getInstance().size() - 1; i > 0; i--) {
//            ActivityStack.getInstance().getActivity(i).finish();
//        }
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    /**
     * 设置自定异常处理类
     *
     * @param context
     */
    public void setCustomCrashInfo(Context context) {
        this.mContext = context;
        type_save = 3;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 保存异常信息到sdcard中
     *
     * @param context
     * @param ex      异常信息对象
     */
    private void saveToSdcard(Context context, Throwable ex) {
        String fileName = null;
        StringBuffer sBuffer = new StringBuffer();
        // 添加异常信息
        sBuffer.append(getExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(CRASH_SAVE_SDPATH);
            if (!file.exists()) {
                file.mkdir();
            }
            fileName = file.toString() + File.separator + paserTime(System.currentTimeMillis()) + ".html";
            File file1 = new File(fileName);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file1);
                fos.write(sBuffer.toString().getBytes());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进行把数据投递至服务器
     *
     * @param context
     * @param ex      崩溃异常
     */
    private void saveToServer(Context context, Throwable ex) {
        final String carsh_log = getExceptionInfo(ex);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("crash_log", carsh_log);
                //TODO 上传服务器

            }
        }).start();
    }

    /**
     * 错误信息发送邮件
     *
     * @param context
     * @param ex      崩溃异常
     */
    private void sendErrorInfoEmail(Context context, Throwable ex) {
        final String carsh_log = getExceptionInfo(ex);
        LogUtils.e("应用崩溃异常信息：",carsh_log);
        if (TextUtils.isEmpty(carsh_log)) {
            return;
        }
        EmailUtils.sendEmail(context, "应用发生异常", carsh_log);
    }

    /**
     * 获取并且转化异常信息
     * 同时可以进行投递相关的设备，用户信息
     *
     * @param ex
     * @return 异常信息的字符串形式
     */
    public String getExceptionInfo(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("---------Crash Log Begin---------\n");
        //在这边可以进行相关设备信息投递--这边就稍微设置几个吧
        //其他设备和用户信息大家可以自己去扩展收集上传投递
        stringBuffer.append("系统版本名:" + DeviceInfoUtil.getVersionName(mContext) + "\n")
                .append(("设备类型:" + DeviceInfoUtil.getModel()) + "\n")
                .append(("OSVersion:" + DeviceInfoUtil.getOsVersion() + "\n"))
                .append(("程序版本名:" + DeviceInfoUtil.getVersionName(mContext) + "\n"))
                .append(("程序版本号:" + DeviceInfoUtil.getVersionCode(mContext) + "\n"))
                .append(("类名称:" + mContext.getClass().getName() + "\n"));
//                .append("登陆者:" + MyApplication.getInstance().mLoginUser.getNickName() + "\n");
        stringBuffer.append(sw.toString() + "\n");
        stringBuffer.append("---------Crash Log End---------\n");
        Log.e("hjf", stringBuffer.toString());


        String error = "";
        try {

            PackageInfo pi = getVersionInfo();

            error = DeviceInfoUtil.getDeviceInfo(mContext) + "手机硬件信息：" + getMobileInfo() + ";versionName:" + pi.versionName + ";versionCode:" + String.valueOf(pi.versionCode) + ";时间：" + new Date() + "；报错信息：" + getErrorInfo(ex);

//            EmailUtils.sendEmail(mContext, "天使移动办公未捕捉异常"+mContext.getPackageName(), error);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return stringBuffer.toString();
        return error;
    }

    /**
     * 进行弹出框提示
     *
     * @param context
     * @param msg
     */
    private void showToast(final Context context, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * x}
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     *
     * @param milliseconds
     * @return
     */
    private String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));

        return times;
    }

    /**
     * 获取手机的版本信息
     *
     * @return
     */
    private PackageInfo getVersionInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

    /**
     * 获取手机的硬件信息
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        //通过反射获取系统的硬件信息
        try {

            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
