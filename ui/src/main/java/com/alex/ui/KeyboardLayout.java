package com.alex.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 通过设置activity的windowSoftInputMode为adjustResize
 * 监听keyboard是否弹出高度
 */
public class KeyboardLayout extends LinearLayout {

    private KeyboardListener mListener;
    private Rect rect = new Rect();
    private int lastKeyboardHeight = -1;

    public void setKeyboardListener(KeyboardListener mListener) {
        this.mListener = mListener;
    }

    public interface KeyboardListener {
        void onSoftKeyboardShown(boolean isShowing, int keyboardHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        Activity activity = (Activity) getContext();
        rect.set(0, 0, 0, 0);
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        if (screenHeight == 0) {
            screenHeight = getResources().getDisplayMetrics().heightPixels;
        }
        int keyboardHeight = screenHeight - statusBarHeight - height;
        if (mListener != null && keyboardHeight != lastKeyboardHeight) {
            mListener.onSoftKeyboardShown(keyboardHeight > 0, keyboardHeight);
        }
        lastKeyboardHeight = keyboardHeight;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(screenHeight - statusBarHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLayout(Context context) {
        super(context);
    }
}
