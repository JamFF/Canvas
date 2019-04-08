package com.ff.canvas.split;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ff.canvas.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * description:
 * author: FF
 * time: 2019/4/6 21:30
 */
public class SplitView extends View {

    private static final String TAG = "SplitView";

    private static final float DIAMETER = 3;// 粒子直径

    private Paint mPaint;
    private Bitmap mBitmap;
    private int measuredHeight;
    private float translate;
    private List<BallBean> mBallBeans = new ArrayList<>();
    private ValueAnimator mAnimator;

    private volatile boolean completeInit;

    private Handler mHandler = new Handler();

    public SplitView(Context context) {
        this(context, null);
    }

    public SplitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        initBall();
        initAnimator();
    }

    // 初始化粒子对象
    private void initBall() {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "initBall: start");
                for (int i = 0; i < mBitmap.getWidth(); i++) {
                    for (int j = 0; j < mBitmap.getHeight(); j++) {
                        BallBean ball = new BallBean();
                        ball.setColor(mBitmap.getPixel(i, j));// 获取某个像素点的颜色
                        ball.setX(i * DIAMETER + DIAMETER / 2);// 颗粒圆的X坐标
                        ball.setY(j * DIAMETER + DIAMETER / 2);// 颗粒圆的Y坐标
                        ball.setR(DIAMETER / 2);// 颗粒圆的半径

                        // X轴速度(-20,20)之间的随机值
                        ball.setvX((float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random()));
                        // Y轴速度通过计算后的随机值
                        ball.setvY(rangInt(-15, 35));
                        // 加速度
                        ball.setaX(0);
                        ball.setaY(0.98f);

                        mBallBeans.add(ball);
                    }
                }
                completeInit = true;
                postInvalidate();
                Log.d(TAG, "initBall: end");
            }
        });
    }

    private void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(-1);// 不断重复
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());// 线性插值器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateBall();
            }
        });
    }

    private int rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        // 在0到(max - min)范围内变化，取大于x的最小整数 再随机
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }

    // 更新粒子的位置，更新速度和加速度
    private void updateBall() {

        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                boolean isInvalidate = false;
                for (BallBean ballBean : mBallBeans) {
                    if (ballBean.getY() > measuredHeight) {
                        continue;// 对已经移除屏幕外的不作处理
                    }
                    isInvalidate = true;
                    ballBean.setX(ballBean.getX() + ballBean.getvX());
                    ballBean.setY(ballBean.getY() + ballBean.getvY());

                    ballBean.setvX(ballBean.getvX() + ballBean.getaX());
                    ballBean.setvY(ballBean.getvY() + ballBean.getaY());
                }
                if (isInvalidate) {
                    postInvalidate();
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            stopAnimator();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        measuredHeight = getMeasuredHeight();
        // 计算居中需要的平移值
        translate = (getMeasuredWidth() - mBitmap.getWidth() * DIAMETER) / 2.0f;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        Log.d(TAG, "onDraw: ");

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        // 移动画布到水平居中
        canvas.translate(translate, translate);

        if (completeInit) {
            for (final BallBean ballBean : mBallBeans) {
                mPaint.setColor(ballBean.getColor());
                canvas.drawCircle(ballBean.getX(), ballBean.getY(), ballBean.getR(), mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 执行动画
            mAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    @UiThread
    public void stopAnimator() {
        mAnimator.removeAllUpdateListeners();
        if (mAnimator.isRunning()) {
            Log.d(TAG, "stopAnimator: ");
            mAnimator.cancel();
        }
    }
}
