package com.alex.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 有输入时显示右侧图标的EditText
 *
 */
public class EditTextWithIcon extends EditText {

    private Drawable right;
    private Rect rightDrawableRect = new Rect();

    /**
     * 获得xml中配置的右侧图标
     */
    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        if (right != null) {
            this.right = right;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            super.onTouchEvent(event);
        }
        if (right != null) {
            int eventX = (int) event.getX();
            int eventY = (int) event.getY();

            rightDrawableRect.top = getPaddingTop();
            rightDrawableRect.right = getWidth() - getPaddingRight();
            rightDrawableRect.bottom = getHeight() - getPaddingBottom();
            rightDrawableRect.left = rightDrawableRect.right - right.getIntrinsicHeight();

            // check to make sure the touch event was within the rect of the right drawable
            if (rightDrawableRect.contains(eventX, eventY) && iconClickListener != null) {
                iconClickListener.clickIcon();
                return super.onTouchEvent(event);
            }
        }
        if (iconClickListener != null) {
            iconClickListener.clickEdit();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 监听EditView中文本的变化
     * @param text 变化后的文本内容
     * @param start 变化的起始位置
     * @param before 变化前需要变化文本的长度
     * @param after 变化后变化文本的长度
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private IconClickListener iconClickListener;

    public void setIconClickListener(IconClickListener listener) {
        this.iconClickListener = listener;
    }

    public interface IconClickListener {
        void clickIcon();

        void clickEdit();
    }

    public EditTextWithIcon(Context context) {
        this(context, null);
    }

    public EditTextWithIcon(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextWithIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}