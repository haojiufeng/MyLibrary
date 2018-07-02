package com.datelibrary.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.weichat.ui.dialog.base.BaseDialog;
import com.sk.weichat.util.ViewPiexlUtil;

/**
 * Created by Administrator on 2016/5/4.
 */
public class OnlyTextDialogView extends BaseDialog {
    private TextView mTextView;

    public OnlyTextDialogView(Activity activity, String text) {
        mActivity = activity;
        initView(text);
    }

    private void initView(String text) {
        mTextView = new TextView(mActivity);
        mTextView.setText(text);
        mTextView.setTextSize(ViewPiexlUtil.px2dp(mActivity, 50));
        mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public BaseDialog show() {
        mDialog = new AlertDialog.Builder(mActivity).setView(mTextView).create();
        mDialog.show();
        return this;
    }





}
