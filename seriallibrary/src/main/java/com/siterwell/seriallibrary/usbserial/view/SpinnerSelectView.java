package com.siterwell.seriallibrary.usbserial.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.util.*;
import com.siterwell.seriallibrary.R;
import com.siterwell.seriallibrary.usbserial.dialog.ECListDialog;

public class SpinnerSelectView extends Button implements View.OnClickListener{

    private static final String TAG = SpinnerSelectView.class.getName();

    /**选项资源菜单*/
    private String[] mItems ;
    /**下拉菜单标题显示文字*/
    private int mDropTitle;
    /**选中的模式位置*/
    private int mChoiceItemPosition;
    private int hint;

    public SpinnerSelectView(Context context) {
        super(context);
        initTypedArray(context, null, -1);
    }

    public SpinnerSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs, -1);
    }

    public SpinnerSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initTypedArray(context, attrs, defStyle);

    }

    private void initTypedArray(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpinnerView, defStyle, 0);
        int items = typedArray.getResourceId(R.styleable.SpinnerView_Spinner_item ,-1);
        mDropTitle = typedArray.getResourceId(R.styleable.SpinnerView_Spinner_DropDownTitle ,-1);
        hint =typedArray.getResourceId(R.styleable.SpinnerView_Spinner_hint,-1);
        typedArray.recycle();

        mChoiceItemPosition = -1;

        if(items > 0) {
            mItems = context.getResources().getStringArray(items);
            setOnClickListener(this);
        }
        if(mDropTitle <= 0) {
            mDropTitle = R.string.tips;
        }
        if(mChoiceItemPosition<0 && hint > 0){
            setText(hint);
        }
    }

    /**
     * 当前选择的类型模式位置
     * @return
     */
    public int getChoiceItemPosition() {
        return mChoiceItemPosition;
    }


    @Override
    public void onClick(View v) {
        showDropDownViewResource();
    }

    /**
     * 显示弹出选项菜单
     */
    private void showDropDownViewResource() {
        if(mItems != null) {
            ECListDialog dialog = new ECListDialog(getContext() ,mItems , mChoiceItemPosition);
            dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
                @Override
                public void onDialogItemClick(Dialog d, int position) {
                    mChoiceItemPosition = position;
                    setText(mItems[position]);
                }
            });
            dialog.setTitle(mDropTitle);
            dialog.show();
            return ;
        }
        Log.e(TAG , "show DropDownView error , items null");
    }

    public void setmChoiceItemPosition(int mChoiceItemPosition) {
        this.mChoiceItemPosition = mChoiceItemPosition;
    }
}
