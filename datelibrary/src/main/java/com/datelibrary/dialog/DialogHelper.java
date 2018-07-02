package com.datelibrary.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.View;

import com.datelibrary.R;
import com.sk.weichat.ui.dialog.ItemSelectDialog;
import com.sk.weichat.ui.dialog.OnlyTextDialogView;
import com.sk.weichat.ui.dialog.SingleInputDialogView;
import com.sk.weichat.ui.dialog.SingleTextDialogView;
import com.sk.weichat.ui.dialog.ThreeInputDialogView;
import com.sk.weichat.ui.dialog.TowInputDialogView;

/**
 * @编写人： TanX
 * @时间： 2016/5/3 12:30
 * @说明：
 * @功能： 统一管理dialog
 **/
public class DialogHelper {

    /**
     * 显示一个提示信息的dialog
     * @param activity
     * @param title 标题
     * @param content 显示内容
     * @param onClickListener 确定按钮的点击事件
     */
    public static void showSingleTextDialog(Activity activity, String title, String content, View.OnClickListener onClickListener) {
        new SingleTextDialogView(activity, title, content, onClickListener).show();
    }
    //不可点击其他地方撤销
    public static void showSingleTextDialog(Activity activity, String title, String content, View.OnClickListener onClickListener, boolean cancleable, boolean hasCancelBtn) {
        new SingleTextDialogView(activity, title, content, onClickListener,cancleable,hasCancelBtn).show();
    }

    /**
     * 显示一个输入框提示的dialog
     * @param activity
     * @param title 标题
     * @param hint edittext的hint
     * @param maxLines edittext的最多行数
     * @param lines edittext默认显示行数
     * @param i edittext的字符过滤器
     * @param onClickListener 确定按钮的点击事件
     */
    public static void showSingleInputDialog(Activity activity, String title, String hint, int maxLines, int lines, InputFilter[] i, boolean isPwdType, View.OnClickListener onClickListener) {
        new SingleInputDialogView(activity, title, hint, maxLines, lines, i,isPwdType, onClickListener).show();
    }


    /**
     * 显示一个输入框提示的dialog,没填的属性为空属性<不做设置>
     * @param activity
     */
    public static void showSingleInputDialog(Activity activity) {
        new SingleInputDialogView(activity).show();
    }

    /**
     * 显示一个输入框提示的dialog,没填的属性为空属性<不做设置>
     * @param activity
     * @param onClickListener 确定按钮的点击事件
     */
    public static void showSingleInputDialog(Activity activity, View.OnClickListener onClickListener) {
        new SingleInputDialogView(activity, onClickListener).show();
    }


    /**
     * 显示一个输入框提示的dialog,其中一些属性设置为默认
     * @param activity
     * @param title 标题
     * @param hint edittext的hint
     * @param  isPwdType 文本类型是否是密码类型
     * @param onClickListener 确定按钮的点击事件
     */
    public static void showLimitSingleInputDialog(Activity activity, String title, String hint, boolean isPwdType, View.OnClickListener onClickListener) {

        new SingleInputDialogView(activity, title, hint, 2, 2, new InputFilter[]{new InputFilter.LengthFilter(20)},isPwdType, onClickListener).show();
    }

    /**
     * 显示两个输入框的dialog,并返回该dialog
     * @param activity
     * @param title 标题
     * @param hint 第一个edittext的hint
     * @param hint2 第二个edittext的hint
     * @param onClickListener 确定按钮的点击事件
     * @return 显示的dialog
     */
    public static TowInputDialogView showTowInputDialogAndReturnDialog(Activity activity, String title, String hint, String hint2, TowInputDialogView.onSureClickLinsenter onClickListener) {
       return (TowInputDialogView) new TowInputDialogView(activity,title, hint, hint2, onClickListener).show();
    }
    /**
     * 显示两个输入框的dialog,并返回该dialog
     * @param activity
     * @param title 标题
     * @param hint 第一个edittext的hint
     * @param hint2 第二个edittext的hint
     * @param onClickListener 确定按钮的点击事件
     * @return 显示的dialog
     */
    public static ThreeInputDialogView showThreeInputDialogAndReturnDialog(Activity activity, String title, String hint, String hint2, String hint3, String hint4, ThreeInputDialogView.onSureClickLinsenter onClickListener) {
        return (ThreeInputDialogView) new ThreeInputDialogView(activity,title, hint, hint2,hint3,hint4, onClickListener).show();
    }
    private static ProgressDialog dialog;

    /**
     * 显示提示message的dialog
     */
    public static void showMessageProgressDialog(Activity activity, String message){
        dialog = new ProgressDialog(activity);
        dialog.setMessage(message);
        dialog.show();
    }

    /**
     * 显示提示等待的dialog
     */
    public static void showDefaulteMessageProgressDialog(Activity activity){
        showMessageProgressDialog(activity, activity.getString(R.string.please_wait));
    }

    /**
     * 显示提示message的dialog
     */
    public static void showMessageProgressDialogAddCancel(Activity activity, String message, DialogInterface.OnCancelListener listener){
        dialog = new ProgressDialog(activity);
        dialog.setMessage(message);
        dialog.setOnCancelListener(listener);
        dialog.show();
    }

    /**
     * 显示提示等待的dialog
     */
    public static void showDefaulteMessageProgressDialogAddCancel(Activity activity, DialogInterface.OnCancelListener listener){
        showMessageProgressDialogAddCancel(activity, activity.getString(R.string.please_wait), listener);
    }

    /**
     * 移除显示信息的dialog
     */
    public static void dismissProgressDialog(){
        if (dialog==null)
            return;
        dialog.dismiss();
        dialog = null;
    }

    /**
     * 显示一个只有textview的窗口
     * @param activity
     * @param text
     */
    public static void showOnlyTextDialog(Activity activity, String text){
        new OnlyTextDialogView(activity,text).show();
    }

    /**
     * @作者： 陈晓威。
     * @时间： 2016/4/30 21:03。
     * @说明： 显示一个文字列表的dialog
     **/
    public static void showItemDialog(Activity activity, String[] items, ItemSelectDialog.ItemSelectLisenter lisenter) {
        new ItemSelectDialog(activity, items, lisenter).show();
    }

}
