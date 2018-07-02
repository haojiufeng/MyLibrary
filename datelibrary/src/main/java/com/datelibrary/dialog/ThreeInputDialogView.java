package com.datelibrary.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.datelibrary.dialog.base.BaseDialog;
import com.datelibrary.view.ClearEditText;
import com.qlwl.ts_mobileworking.R;

/**
 * Created by haojiufeng on 2017/9/21.
 * 其实是4种输入框
 */

public class ThreeInputDialogView extends BaseDialog {

    private TextView mTitleTv;
    private ClearEditText mContentEt;
    private ClearEditText mSecondEt;
    private ClearEditText mThreeEt;
    private Button mCommitBtn;
    private ThreeInputDialogView.onSureClickLinsenter mOnClickListener;
    private ClearEditText mFourEt;

    {
        RID = R.layout.dialog_single_input;
    }

    public ThreeInputDialogView(Activity activity) {
        this(activity, "", "", "","","", null);
    }

    public ThreeInputDialogView(Activity activity, String title, String hint, String hint2, String hint3, String hint4, ThreeInputDialogView.onSureClickLinsenter onClickListener) {
        mActivity = activity;
        initView();
        setView(title, hint, hint2,hint3,hint4);
        mOnClickListener = onClickListener;
    }

    protected void initView() {
        super.initView();
        mTitleTv = (TextView) mView.findViewById(R.id.title);
        mContentEt = (ClearEditText) mView.findViewById(R.id.content);
        mCommitBtn = (Button) mView.findViewById(R.id.sure_btn);
        mSecondEt = (ClearEditText) mView.findViewById(R.id.second_et);
        mThreeEt = (ClearEditText) mView.findViewById(R.id.three_et);
        mFourEt = (ClearEditText) mView.findViewById(R.id.four_et);
    }

    private void setView(String title, String hint, String hint2, String hint3, String hint4) {
        mTitleTv.setText(title);
        mContentEt.setHint(hint);
        mSecondEt.setVisibility(View.VISIBLE);
        mSecondEt.setHint(hint2);
        mThreeEt.setVisibility(View.VISIBLE);
        mThreeEt.setHint(hint3);
        mFourEt.setHint(hint4);
        mFourEt.setVisibility(View.VISIBLE);

        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mOnClickListener != null)
                    mOnClickListener.onClick(mContentEt, mSecondEt,mThreeEt,mFourEt);
            }
        });
    }


    public void setSureClick(ThreeInputDialogView.onSureClickLinsenter onClickListener) {
        mOnClickListener = onClickListener;
    }


    public String getContent() {
        return mContentEt.getText().toString();
    }


    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setHint(String hint) {
        mContentEt.setHint(hint);
    }

    public void setMaxLines(int maxLines) {
        mContentEt.setMaxLines(maxLines);
    }

    //这里有两个edittext，比较特殊，所以单击事件监听器也需要传两个edittext过去
    public interface onSureClickLinsenter {
        void onClick(EditText e1, EditText e2, EditText e3, EditText e4);
    }

    //外面需要对两个edittext做操作，给获取方法
    public EditText getE1() {
        return mContentEt;
    }

    public EditText getE2() {
        return mSecondEt;
    }
    public EditText getE3() {
        return mThreeEt;
    }
    public EditText getE4() {
        return mFourEt;
    }
}
