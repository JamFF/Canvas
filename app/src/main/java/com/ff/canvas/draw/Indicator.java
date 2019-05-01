package com.ff.canvas.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * description: 自定义LinearLayout进行绘制
 * author: FF
 * time: 2019-05-01 12:20
 */
public class Indicator extends LinearLayout {

    private static final String TAG = "Indicator";

    private Paint mPaint;

    private Path mPath;// 绘制三角的路径

    public Indicator(Context context) {
        this(context, null);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();// 三角形
        mPath.moveTo(0, -8);
        mPath.lineTo(40, -8);
        mPath.lineTo(20, -30);
        mPath.close();// 形成闭环
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        // 当不存在背景时不会执行
        // 当存在背景时，绘制会被子View遮挡，所以要在dispatchDraw中绘制
        /*canvas.drawRect(0, getHeight() - 8, getWidth() / 3f, getHeight(), mPaint);
        // 移动Canvas，绘制指针
        canvas.save();
        canvas.translate(getWidth() / 6f - 20, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();*/
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d(TAG, "dispatchDraw: ");
        canvas.drawRect(0, getHeight() - 8, getWidth() / 3f, getHeight(), mPaint);
        // 移动Canvas，绘制指针
        canvas.save();
        canvas.translate(getWidth() / 6f - 20, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }
}
