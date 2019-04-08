package com.ff.canvas.save;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * description:Canvas的save和restore
 * <p>
 * 1.canvas内部对于状态的保存存放在栈中
 * 2.可以多次调用save保存canvas的状态，并且可以通过getSaveCount方法获取保存的状态个数
 * 3.可以通过restore方法返回最近一次save前的状态，也可以通过restoreToCount返回指定save状态。指定save状态之后的状态全部被清除
 * 4.saveLayer可以创建新的图层，之后的绘制都会在这个图层之上绘制，直到调用restore方法
 * 注意：绘制的坐标系不能超过图层的范围， saveLayerAlpha对图层增加了透明度信息
 * <p>
 * author: FF
 * time: 2019/4/6 15:46
 */
public class SaveRestoreView extends View {

    private static final String TAG = "SaveRestoreView";

    private Paint mPaint;
    private Matrix mMatrix;

    public SaveRestoreView(Context context) {
        this(context, null);
    }

    public SaveRestoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SaveRestoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /*Log.d(TAG, "onDraw: " + canvas.getSaveCount());
        canvas.drawRect(200, 200, 700, 700, mPaint);
        int state = canvas.save();
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());

        mPaint.setColor(Color.RED);
        canvas.translate(50, 50);
        canvas.drawRect(0, 0, 500, 500, mPaint);
        canvas.save();
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());

        mPaint.setColor(Color.BLUE);
        canvas.translate(50, 50);
        canvas.drawRect(0, 0, 500, 500, mPaint);

//        canvas.restore();
//        Log.d(TAG, "onDraw: " + canvas.getSaveCount());
//
//        canvas.restore();
//        Log.d(TAG, "onDraw: " + canvas.getSaveCount());

        canvas.restoreToCount(state);// 返回指定save状态，指定save状态之后的状态全部被清除，栈结构
        Log.d(TAG, "onDraw: " + canvas.getSaveCount());

        canvas.drawLine(0, 0, 400, 500, mPaint);*/

        canvas.drawRect(200, 200, 700, 700, mPaint);

        // 离屏绘制
        int layerId = canvas.saveLayer(0, 0, 700, 700, mPaint);
        mPaint.setColor(Color.RED);
        mMatrix.setTranslate(100, 100);
        canvas.setMatrix(mMatrix);
        // 由于平移操作，导致绘制的矩形超出了图层的大小，所以绘制不完全
        canvas.drawRect(0, 0, 700, 700, mPaint);
        // 回滚
        canvas.restoreToCount(layerId);

        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 100, 100, mPaint);
    }
}
