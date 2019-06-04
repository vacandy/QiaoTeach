package com.qiao_techcomponent.imge.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

/**
 * 巨图加载自定义控件
 */
public class QiaoBigImageView extends View implements GestureDetector.OnGestureListener,
        View.OnTouchListener{

    private BitmapFactory.Options mOptions;
    //图片的宽
    private int mImageWidth;
    //图片的高
    private int mImageHeight;
    //view的宽
    private int mViewWidth;
    //view的高
    private int mViewHeight;
    //区域解码器
    private BitmapRegionDecoder mDecoder;

    private Rect mRect;
    private float mScale;
    private Bitmap mBitmap;
    private GestureDetector mGestureDetector;
    private Scroller mScroller;

    public QiaoBigImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QiaoBigImageView(Context context) {
        this(context,null,0);
    }

    public QiaoBigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mGestureDetector = new GestureDetector(context,this);
        setOnTouchListener(this);
        //滑动类
        mScroller = new Scroller(context);
    }

    public void setBitmap (InputStream is) {

        //读取图片信息
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        //内存复用
        mOptions.inMutable = true;
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;
        try {
            //创建区域解码器
            mDecoder = BitmapRegionDecoder.newInstance(is,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        //计算要加载的图片区域
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;
        //获取一个缩放比例
        mScale = mViewWidth/(float)mImageWidth;
        //高度就根据缩放比进行获取
        mRect.bottom = (int)(mViewHeight/mScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mDecoder) {
            return;
        }
        //复用
        mOptions.inBitmap = mBitmap;
        //解码指定的区域
        mBitmap = mDecoder.decodeRegion(mRect,mOptions);
        //把得到的矩阵大小的内存进行缩放  得到view的大小
        Matrix matrix = new Matrix();
        matrix.setScale(mScale,mScale);
        canvas.drawBitmap(mBitmap,matrix,null);
    }

    public void setBitmap (Bitmap bitmap) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!mScroller.isFinished()) {
            //手指按下，如果扔在滑动，就停止滑动
            mScroller.forceFinished(true);
        }
        return true;
    }

    /*
    使用上一个接口的计算结果
     */
    @Override
    public void computeScroll() {
        if(mScroller.isFinished()){
            return;
        }
        //true 表示当前滑动还没有结束
        if(mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int)(mViewHeight/mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    /**
     *
     * @param e1   按下时坐标
     * @param e2   移动时坐标
     * @param distanceX    左右移动时的距离
     * @param distanceY   上下移动时的距离
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //上下滑动，Rect区域实时变化，要重新绘制当前需要显示的区域
        mRect.offset(0,(int)distanceY);
        //处理移动时已经移到了两个顶端的问题
        if(mRect.bottom > mImageHeight){
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int)(mViewHeight/mScale);
        }
        if(mRect.top < 0){
            mRect.top = 0;
            mRect.bottom = (int)(mViewHeight/mScale);
        }
        //重绘
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 处理惯性问题
     * @param e1
     * @param e2
     * @param velocityX   每秒移动的x点
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mScroller.fling(0,mRect.top,
                0,(int)-velocityY,
                0,0,
                0,mImageHeight - (int)(mViewHeight/mScale));
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {



        //手势处理
        return mGestureDetector.onTouchEvent(event);
    }




}
