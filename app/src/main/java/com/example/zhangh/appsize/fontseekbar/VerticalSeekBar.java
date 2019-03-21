package com.example.zhangh.appsize.fontseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.zhangh.appsize.R;


public class VerticalSeekBar extends View {
    private Context context;
    private int height;
    private int width;
    private Paint paint;
    private int maxProgress = 10;
    private int progress = 4;

    protected Bitmap mThumb;
    private int intrinsicHeight;
    private int intrinsicWidth;
    private boolean isInnerClick;
    private float downX;
    private float downY;

    private int locationX;
    private int locationY = -1;

    private int mInnerProgressWidth = 3;
    private int mInnerProgressWidthPx;

    private int unSelectColor = 0xcc888888;
    private RectF mDestRect;
    /**
     * 滑动方向，
     * 0代表从下向上滑
     * 1代表从下向上滑
     */
    private int orientation;

    /**
     * 设置未选中的颜色
     *
     * @param uNSelectColor
     */
    public void setUnSelectColor(int uNSelectColor) {
        this.unSelectColor = uNSelectColor;
    }

    /**
     * 设置滑动方向，
     * 0代表从下向上滑
     * 1代表从下向上滑
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
        invalidate();
    }

    private int selectColor = 0xaa0980ED;

    /**
     * 设置选中线条的颜色
     *
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    /**
     * 设置进度条的宽度 单位是px
     *
     * @param mInnerProgressWidthPx
     */
    public void setmInnerProgressWidthPx(int mInnerProgressWidthPx) {
        this.mInnerProgressWidthPx = mInnerProgressWidthPx;
    }

    /**
     * 设置进度条的宽度 ，单位是dp;默认是4db
     *
     * @param mInnerProgressWidth
     */
    public void setmInnerProgressWidth(int mInnerProgressWidth) {
        this.mInnerProgressWidth = mInnerProgressWidth;
        mInnerProgressWidthPx = Utils.dip2px(context, mInnerProgressWidth);
    }


    /**
     * 设置图片
     *
     * @param id
     */
    public void setThumb(int id) {

        mThumb = BitmapFactory.decodeResource(getResources(), id);
        intrinsicHeight = mThumb.getHeight();
        intrinsicWidth = mThumb.getWidth();
        mDestRect.set(0, 0, intrinsicWidth, intrinsicHeight);
        invalidate();
    }

    /**
     * 设置滑动图片的大小 单位是dp
     *
     * @param width
     * @param height
     */
    public void setThumbSize(int width, int height) {
        setThumbSizePx(Utils.dip2px(context, width), Utils.dip2px(context, height));
    }

    /**
     * 设置滑动图片的大小 单位是px
     *
     * @param width
     * @param height
     */
    public void setThumbSizePx(int width, int height) {
        intrinsicHeight = width;
        intrinsicWidth = height;
        mDestRect.set(0, 0, width, height);
//        locationY = (int) (intrinsicHeight * 0.5f + (100 - progress) * 0.01 * (height - intrinsicHeight));
        invalidate();
    }

    public VerticalSeekBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        paint = new Paint();
        mThumb = BitmapFactory.decodeResource(getResources(), R.mipmap.color_seekbar_thum);
        intrinsicHeight = mThumb.getHeight();
        intrinsicWidth = mThumb.getWidth();
        mDestRect = new RectF(0, 0, intrinsicWidth, intrinsicHeight);
        mInnerProgressWidthPx = Utils.dip2px(context, mInnerProgressWidth);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
//        intrinsicHeight = mThumb.getHeight();
//        intrinsicWidth = mThumb.getWidth();
//
//        mDestRect.set(0, 0, intrinsicWidth, intrinsicHeight);
        if (locationY == -1) {
            locationX = width / 2;
            locationY = height / 2;

            Log.i("xiaozhu", locationY + ":" + height);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //判断点击点是否在圈圈上
                isInnerClick = isInnerMthum(event);
                if (isInnerClick) {
                    if (listener != null) {
                        listener.onStart(this, progress);
                    }
                }
                downX = event.getX();
                downY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (isInnerClick) {
                    locationY = (int) event.getY();//int) (locationY + event.getY() - downY);
                    fixLocationY();

                    progress = (int) (maxProgress - (locationY - intrinsicHeight * 0.5) / (height - intrinsicHeight) * maxProgress);
                    if (orientation == 1) {
                        progress = maxProgress - progress;
                    }
                    downY = event.getY();
                    downX = event.getX();
                    if (listener != null) {
                        listener.onProgress(this, progress);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInnerClick) {
                    if (listener != null) {
                        listener.onStop(this, progress);
                    }
                }
                break;
        }
        return true;
    }

    private void fixLocationY() {
        if (locationY <= intrinsicHeight / 2) {
            locationY = intrinsicHeight / 2;
        } else if (locationY >= height - intrinsicHeight / 2) {
            locationY = height - intrinsicHeight / 2;
        }
    }

    /**
     * 是否点击了图片
     *
     * @param event
     * @return
     */
    private boolean isInnerMthum(MotionEvent event) {
        return event.getX() >= width / 2 - intrinsicWidth / 2 && event.getX() <= width / 2 + intrinsicWidth / 2 && event.getY() >= locationY - intrinsicHeight / 2 && event.getY() <= locationY + intrinsicHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (orientation == 0) {
            locationY = (int) (intrinsicHeight * 0.5f + (maxProgress - progress) * (height - intrinsicHeight) / maxProgress);
        } else {
            locationY = (int) (intrinsicHeight * 0.5f + (progress) * (height - intrinsicHeight) / maxProgress);
        }
        paint.setColor(orientation == 0 ? unSelectColor : selectColor);
        canvas.drawRect(width / 2 - mInnerProgressWidthPx / 2, mDestRect.height() / 2, width / 2 + mInnerProgressWidthPx / 2, locationY, paint);
        paint.setColor(orientation == 0 ? selectColor : unSelectColor);
        canvas.drawRect(width / 2 - mInnerProgressWidthPx / 2, locationY, width / 2 + mInnerProgressWidthPx / 2, height - mDestRect.height() / 2, paint);
        canvas.save();
        canvas.translate(width / 2 - mDestRect.width() / 2, locationY - mDestRect.height() / 2);
//        canvas.drawBitmap(mThumb, width / 2 - intrinsicWidth / 2, locationY - intrinsicHeight / 2, new Paint());
        if (mThumb.isRecycled()){
            mThumb = BitmapFactory.decodeResource(getResources(), R.mipmap.color_seekbar_thum);
            intrinsicHeight = mThumb.getHeight();
            intrinsicWidth = mThumb.getWidth();
            mDestRect = new RectF(0, 0, intrinsicWidth, intrinsicHeight);
        }
        canvas.drawBitmap(mThumb, null, mDestRect, paint);
        canvas.restore();
        super.onDraw(canvas);
    }

    public void setProgress(int progress) {
        if (height == 0) {
            height = getMeasuredHeight();
        }

        this.progress = progress;

        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mThumb != null) {
            mThumb.recycle();
        }
        super.onDetachedFromWindow();
    }


    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    private SlideChangeListener listener;

    public void setOnSlideChangeListener(SlideChangeListener l) {
        this.listener = l;
    }

    //添加监听接口
    public interface SlideChangeListener {
        /**
         * 开始滑动
         *
         * @param slideView
         * @param progress
         */
        void onStart(VerticalSeekBar slideView, int progress);

        /**
         * 滑动过程中
         *
         * @param slideView
         * @param progress
         */
        void onProgress(VerticalSeekBar slideView, int progress);

        /**
         * 停止滑动
         *
         * @param slideView
         * @param progress
         */
        void onStop(VerticalSeekBar slideView, int progress);
    }

}

