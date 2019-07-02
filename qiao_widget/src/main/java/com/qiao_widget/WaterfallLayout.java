package com.qiao_widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/***
 * 流式布局自定义控件
 */
public class WaterfallLayout extends ViewGroup {
    private static final String TAG = "WaterfallLayout";
    private int mColumns = 3;
    private int mHorizontalSpace = 20;
    private int mVerticalSpace = 20;
    private int mChildWidth = 0;
    private int mTop[];

    public WaterfallLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTop = new int[mColumns];
    }

    public WaterfallLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterfallLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //得到总宽度
        int measureWidth = 0;
        int measureHeight = 0;

        if(widthMode == MeasureSpec.EXACTLY){
            measureWidth = widthSize;
            measureHeight = heightSize;
        }else{
            measureChildren(widthMeasureSpec, heightMeasureSpec);

            //得到单个Item的宽度
            mChildWidth = (widthSize - (mColumns - 1) * mHorizontalSpace) / mColumns;

            int childCount = getChildCount();
            if (childCount < mColumns) {
                measureWidth = childCount * mChildWidth + (childCount - 1) * mHorizontalSpace;
            } else {
                measureWidth = widthSize;
            }

            clearTop();
            for (int i = 0; i < childCount; i++) {
                View child = this.getChildAt(i);
                int childHeight = child.getMeasuredHeight() * mChildWidth / child.getMeasuredWidth();
                //Log.e(TAG,"onMeasure MeasureHeight = " + child.getMeasuredHeight() + "; MeasureWidth = " + child.getMeasuredWidth());
                //Log.e(TAG,"onMeasure mChildWidth = " + mChildWidth + "; childHeight = " + childHeight);
                int minColum = getMinHeightColum();

            WaterfallLayoutParams lParams = (WaterfallLayoutParams)child.getLayoutParams();
            lParams.left = minColum * (mChildWidth + mHorizontalSpace);
            lParams.top = mTop[minColum];
            lParams.right = lParams.left + mChildWidth;
            lParams.bottom = lParams.top + childHeight;

                mTop[minColum] += mVerticalSpace + childHeight;
            }

            measureHeight = getMaxHeight();
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        clearTop();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            /*int childHeight = child.getMeasuredHeight() * mChildWidth / child.getMeasuredWidth();
            //Log.e(TAG,"onLayout MeasureHeight = " + child.getMeasuredHeight() + "; MeasureWidth = " + child.getMeasuredWidth());
            int minColum = getMinHeightColum();
            int tleft = minColum * (mChildWidth + mHorizontalSpace);
            int ttop = mTop[minColum];
            int tright = tleft + mChildWidth;
            int tbottom = ttop + childHeight;
            mTop[minColum] += mVerticalSpace + childHeight;
            child.layout(tleft, ttop, tright, tbottom);*/

            WaterfallLayoutParams lParams = (WaterfallLayoutParams)child.getLayoutParams();
            child.layout(lParams.left, lParams.top, lParams.right, lParams.bottom);
        }
    }

    private int getMinHeightColum() {
        int minColum = 0;
        for (int i = 0; i < mColumns; i++) {
            if (mTop[i] < mTop[minColum]) {
                minColum = i;
            }
        }
        return minColum;
    }

    private int getMaxHeight() {
        int maxHeight = 0;
        for (int i = 0; i < mColumns; i++) {
            if (mTop[i] > maxHeight) {
                maxHeight = mTop[i];
            }
        }
        return maxHeight;
    }

    private void clearTop() {
        for (int i = 0; i < mColumns; i++) {
            mTop[i] = 0;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        for (int i = 0; i < getChildCount(); i++) {
            final int index = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, index);
                }
            });
        }
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WaterfallLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new WaterfallLayoutParams(WaterfallLayoutParams.WRAP_CONTENT, WaterfallLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new WaterfallLayoutParams(p);
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof WaterfallLayoutParams;
    }

    public static class WaterfallLayoutParams extends ViewGroup.LayoutParams {
        public int left = 0;
        public int top = 0;
        public int right = 0;
        public int bottom = 0;

        public WaterfallLayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public WaterfallLayoutParams(int width, int height) {
            super(width, height);
        }

        public WaterfallLayoutParams(android.view.ViewGroup.LayoutParams params) {
            super(params);
        }

    }
}
