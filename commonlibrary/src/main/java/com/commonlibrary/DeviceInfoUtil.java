package com.commonlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.UUID;

/**
 * 获取系统设备信息的工具类
 * 
 * @author dty
 * 
 */
public class DeviceInfoUtil {
	//判断是华为，小米，还是魅族
	public static final String SYS_EMUI = "HW";
	public static final String SYS_MIUI = "MI";
	public static final String SYS_FLYME = "MZ";
	private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
	private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
	private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
	private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
	private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
	private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";
	private static String[] PERMISSIONS_PHONE_STATE = {
			Manifest.permission.READ_PHONE_STATE};
	/* 获取手机唯一序列号  IMEI*/
	public static String getDeviceId(Activity context) {
		String deviceId="";
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int permission = ContextCompat.checkSelfPermission(context,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(context, PERMISSIONS_PHONE_STATE,
					0x11111);
		}else{
			deviceId= tm.getDeviceId();// 手机设备ID，这个ID会被用为用户访问统计
			if (deviceId == null) {
				deviceId = UUID.randomUUID().toString().replaceAll("-", "");
			}
		}

		LogUtils.d("imei ===",deviceId);
		return deviceId;
	}

	/* 获取操作系统版本号 */
	public static String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/* 获取手机型号 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/* 获取app的版本信息 */
	public static int getVersionCode(Context context) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;// 系统版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getVersionName(Context context) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;// 系统版本名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getSystem(){
		String SYS="BD";
		FileInputStream inputStream=null;
		try {
			Properties prop= new Properties();
			inputStream=new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
			prop.load(inputStream);
			if(prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
					|| prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
					|| prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null){
				SYS = SYS_MIUI;//小米
			}else if(prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
					||prop.getProperty(KEY_EMUI_VERSION, null) != null
					||prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null){
				SYS = SYS_EMUI;//华为
			}else if(getMeizuFlymeOSFlag().toLowerCase().contains("flyme")){
				SYS = SYS_FLYME;//魅族
			};
		} catch (IOException e){
			e.printStackTrace();
			if(null!=inputStream){
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return SYS;
		}
		return SYS;
	}
	public static String getMeizuFlymeOSFlag() {
		return getSystemProperty("ro.build.display.id", "");
	}
	private static String getSystemProperty(String key, String defaultValue) {
		try {
			Class<?> clz = Class.forName("android.os.SystemProperties");
			Method get = clz.getMethod("get", String.class, String.class);
			return (String)get.invoke(clz, key, defaultValue);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 设备信息
	 */
	public static String getDeviceInfo(Context context) {
		return "设备型号: " + android.os.Build.MODEL + "\n"
				+ "Android版本: " + android.os.Build.VERSION.RELEASE + "\n"
				+ "Android API: " + android.os.Build.VERSION.SDK_INT + "\n"
				+ "程序版本名: " +getVersionName(context) + "\n"
				+ "程序版本号: " +getVersionCode(context) + "\n"
				+ "当前登录者:" + SPUtils.getStringPreference(context, "TS_MobileWorking", "name", null) + "\n";
	}
}
