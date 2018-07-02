package com.datelibrary.dialog;

import android.app.Activity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qlwl.ts_mobileworking.R;
import com.sk.weichat.ui.dialog.base.BaseDialog;
import com.sk.weichat.view.ClearEditText;

/**
 * Created by Administrator on 2016/4/21.
 */
public class SingleInputDialogView extends BaseDialog {

    private TextView mTitleTv;
    private ClearEditText mContentET;
    private Button mCommitBtn;
    private View.OnClickListener mOnClickListener;

    {
        RID = R.layout.dialog_single_input;
    }


    public SingleInputDialogView(Activity activity) {
        mActivity = activity;
        initView();
    }

    public SingleInputDialogView(Activity activity, View.OnClickListener onClickListener) {
        mActivity = activity;
        initView();
        mOnClickListener = onClickListener;
    }

    public SingleInputDialogView(Activity activity, String title, String hint, int maxLines, int lines, InputFilter[] i, boolean isPwdType, View.OnClickListener onClickListener) {
        mActivity = activity;
        initView();
        setView(title, hint, maxLines, lines, i,isPwdType);
        this.mOnClickListener = onClickListener;
    }

    protected void initView() {
        super.initView();
        mTitleTv = (TextView) mView.findViewById(R.id.title);
        mContentET = (ClearEditText) mView.findViewById(R.id.content);
        mCommitBtn = (Button) mView.findViewById(R.id.sure_btn);
    }

    /**
     * @param title     对话框标题
     * @param hint      输入hint 文字
     * @param maxLines  lines
     * @param lines
     * @param i
     * @param isPwdType 输入文本的内容是否是密码类型
     */
    private void setView(String title, String hint, int maxLines, int lines, InputFilter[] i, boolean isPwdType) {
        mTitleTv.setText(title);
        mContentET.setHint(hint);
        mContentET.setMaxLines(maxLines);
        mContentET.setLines(lines);
        if (isPwdType) {
            mContentET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        mContentET.setFilters(i);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mOnClickListener != null)
                    mOnClickListener.onClick(mContentET);
            }
        });
    }

    public View getmView() {
        return mView;
    }


    public void setSureClick(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    public String getContent() {
        return mContentET.getText().toString();
    }


    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setHint(String hint) {
        mContentET.setHint(hint);
    }

    public void setMaxLines(int maxLines) {
        mContentET.setMaxLines(maxLines);
    }

    public void setLines(int lines) {
        mContentET.setLines(lines);
    }

    public void setFilters(InputFilter[] i) {
        mContentET.setFilters(i);
    }

}
