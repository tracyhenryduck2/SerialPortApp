package com.siterwell.serialportapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);

    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    private boolean allowChildViewScroll = true;

    public void setAllowChildViewScroll(boolean allowChildViewScroll) {
        this.allowChildViewScroll = allowChildViewScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!allowChildViewScroll){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}