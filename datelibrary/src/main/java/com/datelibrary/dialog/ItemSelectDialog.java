package com.datelibrary.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.qlwl.ts_mobileworking.R;
import com.sk.weichat.ui.dialog.base.BaseDialog;
import com.sk.weichat.util.ViewPiexlUtil;


/**
 * @作者：陈晓威.
 * @时间：2016/4/30.
 * @说明：显示item的窗口
 */
public class ItemSelectDialog extends BaseDialog {
    {
        RID = R.layout.dialog_item_select;
    }

    private String[] mItems;
    private ItemSelectLisenter mLisenter;


    public ItemSelectDialog(Activity activity, String[] items, ItemSelectLisenter lisenter) {
        this.mActivity = activity;
        this.mItems = items;
        this.mLisenter = lisenter;
        initView();
        makeItemView();
    }

    public ItemSelectDialog(Activity activity, String[] items) {
        this(activity, items, null);
    }

    private void makeItemView() {
        int lenght = mItems.length;
        if (lenght <= 0)
            return;
        ViewGroup viewGroup = (ViewGroup) mView;

        for (int i = 0; i < lenght; i++) {
            viewGroup.addView(newButton(i));
            if (i != lenght - 1)
                viewGroup.addView(newLine());
        }
    }

    private Button newButton(final int position) {
        Button clickButton = new Button(mActivity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int padding = ViewPiexlUtil.px2dp(mActivity,20);
        clickButton.setPadding(padding, padding, padding, padding);
        clickButton.setBackgroundResource(R.drawable.list_selector_background);
        clickButton.setTextSize(ViewPiexlUtil.dp2px(mActivity, 12));
        clickButton.setTextColor(mActivity.getResources().getColor(R.color.dark_grey));
        clickButton.setLayoutParams(lp);
        clickButton.setText(mItems[position]);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(position, mItems[position]);
            }
        });
        return clickButton;
    }

    private View newLine() {
        View view = new View(mActivity);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        view.setBackgroundColor(mActivity.getResources().getColor(R.color.dark_grey));
        return view;
    }

    public void onSelect(int position, String text) {
        if (mLisenter != null)
            mLisenter.onSelect(position, text);
        mDialog.dismiss();
    }

    public void setItemSelectLisenter(ItemSelectLisenter lisenter) {
        this.mLisenter = lisenter;
    }

    public interface ItemSelectLisenter {
        void onSelect(int selectPosition, String selectText);
    }


}
