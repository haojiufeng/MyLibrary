package com.commonlibrary;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
public class DateUtils {

    /** 格式：yyyy-MM-dd HH:mm:ss */
    public static SimpleDateFormat yMd_Hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	public static SimpleDateFormat yM = new SimpleDateFormat("yyyy-MM"); //格式化为只有年月的格式
    /**
     * yyyyMMdd 格式
     */
    public static SimpleDateFormat yMd = new SimpleDateFormat("yyyyMMdd");
    /**
     * yyyy-MM-dd  格式
     */
    public static SimpleDateFormat yMd_2 = new SimpleDateFormat("yyyy-MM-dd");
    // 工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2
//	public static String isHolidayUrl="http://api.k780.com/?app=life.workday&date="; // 网络接口
    public static String isHolidayUrl_1="http://oa.zqlwl.com/oapi.php?m=opai&c=index&a=check_holiday"; // IT中心内部接口
//	public static String appKeyAndSign="&appkey=32154&sign=39e55b7529108565a20e8bca35ed79e4&format=json";
    /**
     * 判断某一天日期类型 工作日，休息日，节假日
     * 工作日对应结果为 0, 休息日对应结果为 1,
     * @param date
     * @return
     */
    private static java.util.Map<String,Integer> daymap = new HashMap<String,Integer>();
    /**
     * 获取当前月份
     *
     * @return
     */
    public static int currentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取某个日期的月份的最后一天
     *
     * @param date
     * @return   yyyy-MM-dd  00:00:00 格式
     */
    public static String getLastDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.DATE, 28);// 从28 开始循环 2月份是天数最少的月份呢
            while (calendar.get(Calendar.MONTH) == month) { // 只要月份一样
                calendar.add(Calendar.DATE, 1);
                if (calendar.get(Calendar.MONTH) != month) { // 说明月末切换月份了
                    calendar.add(Calendar.DATE, -1);
                    // System.out.println(simpleDateFormat.format(calendar.getTime()));
                    return simpleDateFormat.format(calendar.getTime()) + " 23:59:59";
                }
            }
        } catch (ParseException e) {
//			EmailUtils.sendEmail("获取某月最后一天格式化异常", WagesUtils.getStackTrac(e));
        }
        return "";
    }

    /**
     * 获取一个日期的月份的第一天
     *
     * @param date
     * @return  yyyy-MM-dd  00:00:00 格式
     */
    public static String getFirstDayOfMonth(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempDate = simpleDateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempDate);
            calendar.set(Calendar.DATE, 1);
            return simpleDateFormat.format(calendar.getTime()) + " 00:00:00";

        } catch (ParseException e) {
//			EmailUtils.sendEmail("获取日期的第一天格式化错误", WagesUtils.getStackTrac(e));
        }
        return "";
    }

    /**
     * 将java.sql.Timestamp对象转化为String字符串
     *
     * @param time
     *            要格式的java.sql.Timestamp对象
     * @param strFormat
     *            输出的String字符串格式的限定（如："yyyy-MM-dd HH:mm:ss"）
     * @return 表示日期的字符串
     */
    public static String dateToStr(java.sql.Timestamp time, String strFormat) {
        DateFormat df = new SimpleDateFormat(strFormat);
        String str = df.format(time);
        return str;
    }

    /**
     * add by gongtao 计算指定日期时间之间的时间差  endStr-beginStr
     *
     * @param beginStr
     *            开始日期字符串  日期时间字符串格式:yyyy-MM-dd
     * @param endStr
     *            结束日期字符串
     * @param  unit  时间差的形式 0 -秒,1-分种,2-小时,3--天
     *
     */
    public static int getInterval(String beginStr, String endStr, int unit) {
        if(TextUtils.isEmpty(beginStr) || TextUtils.isEmpty(endStr)){ // 只要有一个考勤时间没有的，设置工作时间为0
            return 0;
        }
        int hours = 0;
        try {
            Date beginDate = yMd_Hms.parse(beginStr);
            Date endDate = yMd_Hms.parse(endStr);
            long millisecond = endDate.getTime() - beginDate.getTime(); // 日期相减获取日期差X(单位:毫秒)
            /**
             * Math.abs((int)(millisecond/1000)); 绝对值 1秒 = 1000毫秒
             * millisecond/1000 --> 秒 millisecond/1000*60 - > 分钟
             * millisecond/(1000*60*60) -- > 小时 millisecond/(1000*60*60*24) -->
             * 天
             */
            switch (unit) {
                case 0: // second
                    return (int) (millisecond / 1000);
                case 1: // minute
                    return (int) (millisecond / (1000 * 60));
                case 2: // hour
                    return (int) (millisecond / (1000 * 60 * 60));
                case 3: // day
                    return (int) (millisecond / (1000 * 60 * 60 * 24));
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hours;
    }
    /**
     * <p>比较两个日期的大小,精确到秒</p>
     *
     * @param d1
     * @param d2
     * @author lipp@icloud-edu.com
     * @date 2014-06-03
     * @return 返回一个long类型的整数，若大于0表示第一个日期晚于第二个日期，小于0表示第一个日期早于第二个日期，否则相等
     */
    public static long compareEachOther(Date d1, Date d2){
        if(d1 == null || d2 == null)
            return -1;
        String d1Str = d1.getTime() + "";
        String d2Str = d2.getTime() + "";
        int l1 = d1Str.length();
        int l2 = d2Str.length();
        d1Str = d1Str.substring(0, l1 - 3) + "000";
        d2Str = d2Str.substring(0, l2 - 3) + "000";
        //System.out.println(d1Str + "   " + d2Str);
        long long1 = Long.parseLong(d1Str);
        long long2 = Long.parseLong(d2Str);
        return long1 - long2;
    }
    /**
     * 2016-09-09 转换为2016-9-9
     *
     * @param date
     * @return
     */
    public static String dateFormatChanged(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
        try {
            return formatter.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2016-9-9 转换为2016-09-09
     *
     * @param date
     * @return
     */
    public static String dateFormatChanged_2(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**在指定日期的基础上增加指定单位指定数量后的日期，支持所有字段，所有数量，加，减
     * @param date  yyyy-MM-dd HH:mm:ss 格式，不是的话，请不要调用，会报错的
     * @param field
     * @param count
     * @return
     */
    public static String addDate(String date,int field,int count){
        Calendar calendar=Calendar.getInstance();
        try {
            Date tempDate=yMd_Hms.parse(date);
            calendar.setTime(tempDate);
            calendar.add(field, count);
            return yMd_Hms.format(calendar.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    /**在指定日期的基础上增加指定单位指定数量后的日期，支持所有字段，所有数量，加，减
     * @param date  yyyy-MM-dd格式，不是的话，请不要调用，会报错的
     * @param field
     * @param count
     * @return
     */
    public static String addDate_1(String date,int field,int count){
        Calendar calendar=Calendar.getInstance();
        try {
            Date tempDate=yMd_2.parse(date);
            calendar.setTime(tempDate);
            calendar.add(field, count);
            return yMd_2.format(calendar.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 距离指定日期是否超过一年
     * @param date
     * @return
     */
    public static boolean isOverOneYear(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        if(Calendar.getInstance().after(calendar)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * float 数值类型保留2位小数   分钟转为小时的计算方式
     * @param number
     * @return
     */
    public static String minuteToHours(int number){
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String num=df.format((float)number/60);
        return num;
    }

    /**返回当前时间  格式=yyyy-dd-mm hh:mm:ss
     * @return
     */
    public static String now(){
        return yMd_Hms.format(Calendar.getInstance().getTime());
    }
}
