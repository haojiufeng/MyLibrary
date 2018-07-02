package com.datelibrary.dialog.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

/**
 * Created by Administrator on 2016/5/3.
 */
public abstract class BaseDialog {
    protected View mView;
    protected Activity mActivity;
    protected int RID;
    protected AlertDialog mDialog;
    //点击外部可以取消
    protected boolean mCancleable = true;
    //是否有取消按钮
    protected boolean hasCancelBtn = true;

    protected void initView() {
        if (RID != 0)
            mView = mActivity.getLayoutInflater().inflate(RID, null);
    }

    public BaseDialog show() {
        mDialog = new AlertDialog.Builder(mActivity).setView(mView).create();
        mDialog.setCancelable(mCancleable);

        if (null != mActivity && !mActivity.isFinishing())
            mDialog.show();
        return this;
    }

    public <T> T $(int rid) {
        return (T) mView.findViewById(rid);
    }

    public String getString(int rstring) {
        return mActivity.getString(rstring);
    }

}
