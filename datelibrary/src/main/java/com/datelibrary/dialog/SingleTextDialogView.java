package com.datelibrary.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.qlwl.ts_mobileworking.R;
import com.sk.weichat.ui.dialog.base.BaseDialog;

/**
 * Created by Administrator on 2016/4/21.
 */
public class SingleTextDialogView extends BaseDialog {

    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mBtnSure;
    private TextView mBtnCancel;
    private View.OnClickListener mOnClickListener;

    {
        RID = R.layout.dialog_sigle_text;
    }

    public SingleTextDialogView(Activity activity) {
        this(activity, "", "", null);
    }

    public SingleTextDialogView(Activity activity, String title, String content, View.OnClickListener onClickListener) {
        this.mActivity = activity;
        this.mOnClickListener = onClickListener;
        initView();
        mTvTitle.setText(title);
        mTvContent.setText(content);
    }

    public SingleTextDialogView(Activity activity, String title, String content, View.OnClickListener onClickListener, boolean cancleable, boolean hasCancelBtn) {
        this(activity,title,content,onClickListener);
        this.hasCancelBtn=hasCancelBtn;
        mCancleable = cancleable;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvTitle = $(R.id.title);
        mTvContent = $(R.id.content);
        mBtnSure = $(R.id.sure_btn);
        mBtnCancel = $(R.id.cancel_btn);

        mBtnCancel.setVisibility(hasCancelBtn? View.VISIBLE: View.GONE);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mOnClickListener!=null)
                    mOnClickListener.onClick(v);
            }
        });
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }
    public void setButtonText(int rstring) {
        setButtonText(getString(rstring));
    }

    public void setTitle(int rstring) {
        setTitle(getString(rstring));
    }

    public void setContent(String content) {
        mTvContent.setText(content);
    }

    public void setContent(int rstring) {
        setContent(getString(rstring));
    }

    public void setBottonClick(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setButtonText(String text) {
        mBtnSure.setText(text);
    }


}
