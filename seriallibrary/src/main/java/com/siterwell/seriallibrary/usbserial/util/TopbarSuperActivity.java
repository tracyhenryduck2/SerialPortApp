package com.siterwell.seriallibrary.usbserial.util;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import com.siterwell.seriallibrary.R;
import com.siterwell.seriallibrary.usbserial.dialog.ECProgressDialog;
import com.siterwell.seriallibrary.usbserial.view.TopBarView;

/**
 * Created by gc-0001 on 2016/12/6.
 */
public abstract class TopbarSuperActivity extends AppCompatActivity {

    /**
     * 标题
     */
    private TopBarView mTopBarView;
    private LayoutInflater mLayoutInflater;
    private View mContentView;
    private ECProgressDialog mProgressDialog;
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        init();
        onCreateInit();
    }


    public TopBarView getTopBarView() {
        if(mTopBarView instanceof TopBarView) {
            return (TopBarView) mTopBarView;
        }
        return null;
    }

    private void init()  {

        int layoutId = getLayoutId();
        ViewGroup mRootView = (ViewGroup)findViewById(R.id.root);
        mLayoutInflater = LayoutInflater.from(this);
        mTopBarView = (TopBarView)findViewById(R.id.top_bar);

        if (layoutId != -1) {
            mContentView = mLayoutInflater.inflate(getLayoutId(), null);
            mRootView.addView(mContentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }

        initSystemBar();
    }

    protected abstract void onCreateInit();

    protected abstract int getLayoutId();

    /**
     * hide inputMethod
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null ) {
            View localView = this.getCurrentFocus();
            if(localView != null && localView.getWindowToken() != null ) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }


    protected void initSystemBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getTopBarView().setFitsSystemWindows(true);//需要把根布局设置为这个属性 子布局则不会占用状态栏位置
            getTopBarView().setClipToPadding(true);//需要把根布局设置为这个属性 子布局则不会占用状态栏位置
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        SystemTintManager tintManager = new SystemTintManager(this);// 创建状态栏的管理实例
        tintManager.setStatusBarDarkMode(true, this);//false 状态栏字体颜色是白色 true 颜色是黑色
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initSystemBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftKeyboard();
    }

    protected void showProgressDialog(String title){
        mProgressDialog = new ECProgressDialog(this);
        mProgressDialog.setPressText(title);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mProgressDialog.show();

    }

    protected void hideProgressDialog(){
        if(mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    /**
     *  判断某个字符串是否存在于数组中
     *  用来判断该配置是否通道相关
     *  @param stringArray 原数组
     *  @param source 查找的字符串
     *  @return 是否找到
     */
    public static boolean contains(String[] stringArray, String source) {
        // 转换为list
        List<String> tempList = Arrays.asList(stringArray);

        // 利用list的包含方法,进行判断
        return tempList.contains(source);
    }



    public void showToast(String text){
        if ( null != text ) {
            if ( null != mToast ) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void showToast(int resid){
        if ( resid > 0 ) {
            if ( null != mToast ) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, resid, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
