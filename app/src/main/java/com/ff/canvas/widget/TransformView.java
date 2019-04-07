package com.ff.canvas.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * description:
 * author: FF
 * time: 2019/4/6 14:13
 */
public class TransformView extends View {

    private static final String TAG = "TransformView";

    private Paint mPaint;
    private Matrix mMatrix;

    public TransformView(Context context) {
        this(context, null);
    }

    public TransformView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");

        // 平移Canvas操作
//        translateCanvas(canvas);

        // 缩放Canvas操作
//        scaleCanvas(canvas);

        // 旋转Canvas操作
//        rotateCanvas(canvas);

        // 倾斜Canvas操作
//        skewCanvas(canvas);

        // 裁剪Canvas操作
//        clipRectCanvas(canvas);

        // 反向裁剪Canvas操作
//        clipOutRectCanvas(canvas);

        // 通过矩阵实现平移，缩放，旋转Canvas操作
        matrixCanvas(canvas);
    }

    // 平移Canvas
    private void translateCanvas(Canvas canvas) {
        canvas.drawRect(0, 0, 400, 400, mPaint);
        mPaint.setColor(Color.RED);
        canvas.translate(50, 50);
        canvas.drawRect(0, 0, 400, 400, mPaint);
    }

    // 缩放Canvas
    private void scaleCanvas(Canvas canvas) {
        canvas.drawRect(200, 200, 600, 600, mPaint);
        mPaint.setColor(Color.RED);

//        canvas.scale(0.5f, 0.5f);// 缩小50%

        // 重载方法，先translate(px, py),再scale(sx, sy),再反向translate
        canvas.scale(0.5f, 0.5f, 500, 500);
        // 等同于下面三行代码
        // canvas.translate(200, 200);
        // canvas.scale(0.5f, 0.5f);
        // canvas.translate(-200, -200);

        canvas.drawRect(200, 200, 600, 600, mPaint);
    }

    // 旋转Canvas
    private void rotateCanvas(Canvas canvas) {
        // 先平移，再旋转，也是在平移后的原点，进行旋转
//        canvas.translate(100, 100);
//        canvas.drawRect(0, 0, 400, 400, mPaint);
//        mPaint.setColor(Color.RED);
//        canvas.rotate(45);// 旋转45度，默认顺时针
//        canvas.drawRect(0, 0, 400, 400, mPaint);

        canvas.drawRect(400, 400, 800, 800, mPaint);
        mPaint.setColor(Color.RED);
        // 重载方法
        canvas.rotate(45, 600, 600);// px, py表示旋转中心的坐标
        canvas.drawRect(400, 400, 800, 800, mPaint);
    }

    // 倾斜Canvas
    private void skewCanvas(Canvas canvas) {
        canvas.drawRect(0, 0, 400, 400, mPaint);
        mPaint.setColor(Color.RED);
//        canvas.skew(1, 0);// 在X方向倾斜45度，也就是Y轴逆时针旋转45
        canvas.skew(0, 1);// 在Y方向倾斜45度，也就是X轴顺时针旋转45
        canvas.drawRect(0, 0, 400, 400, mPaint);
    }

    // 裁剪Canvas
    private void clipRectCanvas(Canvas canvas) {
        canvas.clipRect(200, 200, 400, 400); // 画布被裁剪
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(100, 100, 100, mPaint);// 超出裁剪区域，无法绘制
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(200, 200, 200, mPaint);// 部分在裁剪范围内，绘制部分
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(300, 300, 100, mPaint);// 在裁剪范围内，绘制成功
    }

    // 反向裁剪Canvas
    private void clipOutRectCanvas(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 画布裁剪外的区域，最低API 26
            canvas.clipOutRect(200, 200, 400, 400);
        }
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(100, 100, 100, mPaint);// 在裁剪范围内，绘制成功
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(200, 200, 200, mPaint);// 部分在裁剪范围内，绘制部分
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(300, 300, 100, mPaint);// 超出裁剪区域，无法绘制
    }

    // 矩阵操作Canvas
    private void matrixCanvas(Canvas canvas) {
        canvas.drawRect(0, 0, 400, 400, mPaint);
        mPaint.setColor(Color.RED);
//        mMatrix.setTranslate(50, 50);
//        mMatrix.setRotate(45);
        mMatrix.setScale(0.5f, 0.5f);// 同时设置平移，缩放，旋转，只有最后一个生效
        canvas.setMatrix(mMatrix);
        canvas.drawRect(0, 0, 400, 400, mPaint);
    }
}