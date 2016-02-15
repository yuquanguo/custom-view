package com.alex.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class SimpleRatingBar extends View {

    private Bitmap missImage;
    private Bitmap getImage;
    private float rating = 0f;
    private int num = 5; // 等级数量
    private int interval = 0; // 等级图标之间的间隔 px
    private Rect src;
    private Rect dst;

    public SimpleRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleRatingBar,
                defStyleAttr, 0);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SimpleRatingBar_missImage) {
                BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                missImage = drawable == null ? null : drawable.getBitmap();

            } else if (attr == R.styleable.SimpleRatingBar_interval) {
                interval = a.getDimensionPixelSize(attr, interval);

            } else if (attr == R.styleable.SimpleRatingBar_num) {
                num = a.getInt(attr, num);

            } else if (attr == R.styleable.SimpleRatingBar_getImage) {
                BitmapDrawable starDrawable = (BitmapDrawable) a.getDrawable(attr);
                getImage = starDrawable == null ? null : starDrawable.getBitmap();

            } else if (attr == R.styleable.SimpleRatingBar_rating) {
                rating = a.getFloat(attr, rating);

            }
        }
        a.recycle();
    }

    public SimpleRatingBar(Context context) {
        this(context, null);
    }

    public SimpleRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public final void setRating(float rating) {
        this.rating = rating;
        requestLayout();
        invalidate();
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public float getRating() {
        return rating;
    }

    public int getNum() {
        return num;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getImage == null) {
            super.onDraw(canvas);
            return;
        }
        int width = getImage.getWidth();
        int left = 0;
        for (int i = 0; i < num; i++) {
            Bitmap src = i < rating ? getImage : missImage;
            if (src != null) {
                canvas.drawBitmap(i < rating ? getImage : missImage, left, 0, null);
            }
            left += width + interval;
        }
        float scale = rating % 1.0f;
        if (scale > 0 && missImage != null) {
            int n = (int) Math.floor(rating);
            int l = (width + interval) * n;
            if (src == null) {
                src = new Rect();
            }
            src.left = (int) (width * scale);
            src.right = width;
            src.bottom = getImage.getHeight();
            if (dst == null) {
                dst = new Rect();
            }
            dst.left = src.left + l;
            dst.right = src.right + l;
            dst.bottom = src.bottom;
            if (missImage != null) {
                canvas.drawBitmap(missImage, src, dst, null);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getImage != null) {
            int width = getImage.getWidth();
            if (missImage == null) {
                float scale = rating % 1.0f;
                if (scale > 0) {
                    int n = (int) Math.floor(rating);
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) ((width + interval) * n + scale * width),
                            MeasureSpec.EXACTLY);
                } else {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) ((width + interval) * rating - interval),
                            MeasureSpec.EXACTLY);
                }

            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((width + interval) * num - interval,
                        MeasureSpec.EXACTLY);
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(getImage.getHeight(), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}