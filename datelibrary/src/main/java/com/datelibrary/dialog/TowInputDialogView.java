package com.datelibrary.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qlwl.ts_mobileworking.R;
import com.sk.weichat.ui.dialog.base.BaseDialog;
import com.sk.weichat.view.ClearEditText;

/**
 * Created by Administrator on 2016/4/21.
 */
public class TowInputDialogView extends BaseDialog{

    private TextView mTitleTv;
    private ClearEditText mContentEt;
    private ClearEditText mSecondEt;
    private Button mCommitBtn;
    private onSureClickLinsenter mOnClickListener;

    {
        RID = R.layout.dialog_single_input;
    }

    public TowInputDialogView(Activity activity){
        this(activity,"", "", "", null);
    }
    public TowInputDialogView(Activity activity, String title, String hint, String hint2, onSureClickLinsenter onClickListener){
        mActivity = activity;
        initView();
        setView(title,hint,hint2);
        mOnClickListener = onClickListener;
    }

    protected void initView(){
        super.initView();
        mTitleTv = (TextView) mView.findViewById(R.id.title);
        mContentEt = (ClearEditText) mView.findViewById(R.id.content);
        mCommitBtn = (Button) mView.findViewById(R.id.sure_btn);
        mSecondEt = (ClearEditText) mView.findViewById(R.id.second_et);
    }

    private void setView(String title, String hint, String hint2){
        mTitleTv.setText(title);
        mContentEt.setHint(hint);
        if(hint.contains("密码")){
            mContentEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mSecondEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        mSecondEt.setVisibility(View.VISIBLE);
        mSecondEt.setHint(hint2);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mOnClickListener!=null)
                    mOnClickListener.onClick(mDialog,mContentEt,mSecondEt);
            }
        });
    }


    public void setSureClick(onSureClickLinsenter onClickListener){
        mOnClickListener = onClickListener;
    }


    public String getContent(){
        return mContentEt.getText().toString();
    }


    public void setTitle(String title){
        mTitleTv.setText(title);
    }

    public void setHint(String hint){
        mContentEt.setHint(hint);
    }

    public void setMaxLines(int maxLines){
        mContentEt.setMaxLines(maxLines);
    }

    //这里有两个edittext，比较特殊，所以单击事件监听器也需要传两个edittext过去
    public interface onSureClickLinsenter{
        void onClick(AlertDialog mDialog, EditText e1, EditText e2);
    }
    //外面需要对两个edittext做操作，给获取方法
    public EditText getE1(){
        return mContentEt;
    }
    public EditText getE2(){
        return mSecondEt;
    }
}
