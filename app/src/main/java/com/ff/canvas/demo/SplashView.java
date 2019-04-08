package com.ff.canvas.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.ff.canvas.R;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * description: 旋转、扩散聚合、水波纹效果
 * author: FF
 * time: 2019/4/7 14:06
 */
public class SplashView extends View {

    private static final String TAG = "SplashView";

    // 旋转的6个小球的画笔
    private Paint mPaint;
    // 扩散圆的画笔
    private Paint mHolePaint;
    // 属性动画
    private ValueAnimator mValueAnimator;

    // 背景色
    private static final int BACKGROUND_COLOR = Color.WHITE;
    // 6个小球颜色数组
    private int[] mCircleColors;

    // 表示旋转圆的中心坐标
    private float mCenterX;
    private float mCenterY;
    // 表示斜对角线长度的一半，扩散圆最大半径
    private float mDistance;

    // 6个小球的半径
    private float mCircleRadius = 18;
    // 旋转圆的半径
    private static final float ROTATE_RADIUS = 90;

    // 当前大圆的旋转角度
    private float mCurrentRotateAngle = 0;
    // 当前大圆的半径
    private float mCurrentRotateRadius = ROTATE_RADIUS;
    // 扩散圆的半径
    private float mCurrentHoleRadius = 0;
    // 表示旋转动画的时长
    private static final int ROTATE_DURATION = 1200;

    private SplashState mState;

    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 设置抗锯齿的标记

        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(BACKGROUND_COLOR);

        mCircleColors = getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: ");
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        // 对角线长度的一半
        mDistance = (float) (Math.hypot(w, h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        if (mState == null) {
            mState = new RotateState(this);
        }
        mState.drawState(canvas);
    }

    /**
     * description: 旋转、扩散聚合、水波纹，三种动画效果的抽象类
     * author: FF
     * time: 2019/4/7 14:35
     */
    private static abstract class SplashState {

        // 使用静态内部类和弱引用避免内存泄漏
        final WeakReference<SplashView> mWeakReference;
        final SplashView mSplashView;

        SplashState(SplashView splashView) {
            mWeakReference = new WeakReference<>(splashView);
            mSplashView = mWeakReference.get();
        }

        abstract void drawState(Canvas canvas);

        /**
         * 绘制背景
         */
        void drawBackground(Canvas canvas) {
            if (mSplashView.mCurrentHoleRadius > 0) {
                // 绘制空心圆
                float strokeWidth = mSplashView.mDistance - mSplashView.mCurrentHoleRadius;
                float radius = strokeWidth / 2 + mSplashView.mCurrentHoleRadius;
                mSplashView.mHolePaint.setStrokeWidth(strokeWidth);
                canvas.drawCircle(mSplashView.mCenterX, mSplashView.mCenterY, radius, mSplashView.mHolePaint);
            } else {
                canvas.drawColor(BACKGROUND_COLOR);
            }
        }

        /**
         * 绘制6个小圆
         */
        void drawCircles(Canvas canvas) {
            if (mSplashView == null) {
                return;
            }
            // 两个小球之间的角度，Math.PI在角度值里相当于180°
            float rotateAngle = (float) (Math.PI * 2 / mSplashView.mCircleColors.length);
            for (int i = 0; i < mSplashView.mCircleColors.length; i++) {
                // 小球的角度当前角度加上大圆旋转的角度
                float angle = i * rotateAngle + mSplashView.mCurrentRotateAngle;
                // x = r * cos(a) + centX;
                // y = r * sin(a) + centY;
                float cx = (float) (Math.cos(angle) * mSplashView.mCurrentRotateRadius + mSplashView.mCenterX);
                float cy = (float) (Math.sin(angle) * mSplashView.mCurrentRotateRadius + mSplashView.mCenterY);
                mSplashView.mPaint.setColor(mSplashView.mCircleColors[i]);
                canvas.drawCircle(cx, cy, mSplashView.mCircleRadius, mSplashView.mPaint);
            }
        }
    }

    // 1.旋转
    private static class RotateState extends SplashState {

        private RotateState(SplashView view) {
            super(view);
            if (mSplashView == null) {
                return;
            }
            mSplashView.mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));// 旋转360°
            mSplashView.mValueAnimator.setRepeatCount(2);// 2代表重复2次，执行3次
            mSplashView.mValueAnimator.setDuration(ROTATE_DURATION);
            // android默认是先加速再减速的，所以这里设置线性插值器
            mSplashView.mValueAnimator.setInterpolator(new LinearInterpolator());
            mSplashView.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 设置大圆旋转的角度
                    mSplashView.mCurrentRotateAngle = (float) animation.getAnimatedValue();
                    mSplashView.invalidate();
                }
            });
            mSplashView.mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mSplashView.mState = new MergeState(mSplashView);
                }
            });
            mSplashView.mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);// 绘制背景
            drawCircles(canvas);// 绘制6个小球
        }
    }

    // 2.扩散聚合
    private static class MergeState extends SplashState {

        private MergeState(SplashView splashView) {
            super(splashView);
            if (mSplashView == null) {
                return;
            }
            // 从圆心到大圆的半径
            mSplashView.mValueAnimator = ValueAnimator.ofFloat(0, ROTATE_RADIUS);
            mSplashView.mValueAnimator.setRepeatCount(0);// 0代表不重复，执行1次
            mSplashView.mValueAnimator.setDuration(ROTATE_DURATION);
            // 从第一个参数0起始，向第二个参数ROTATE_RADIUS扩散，并超过一部分，最后回弹到ROTATE_RADIUS终止
            // 10f这个参数越大，扩散效果越明显
            mSplashView.mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mSplashView.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 获取当前大圆的半径
                    mSplashView.mCurrentRotateRadius = (float) animation.getAnimatedValue();
                    mSplashView.invalidate();
                }
            });
            mSplashView.mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mSplashView.mState = new ExpandState(mSplashView);
                }
            });
            mSplashView.mValueAnimator.reverse();// 反向执行
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    // 3.水波纹
    private static class ExpandState extends SplashState {

        private ExpandState(SplashView splashView) {
            super(splashView);
            if (mSplashView == null) {
                return;
            }
            mSplashView.mValueAnimator = ValueAnimator.ofFloat(0, mSplashView.mDistance);
            mSplashView.mValueAnimator.setDuration(ROTATE_DURATION);
            mSplashView.mValueAnimator.setInterpolator(new LinearInterpolator());
            mSplashView.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSplashView.mCurrentHoleRadius = (float) animation.getAnimatedValue();
                    mSplashView.invalidate();
                }
            });
            mSplashView.mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }

    @UiThread
    public void stopAnimator() {
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.removeAllListeners();
        if (mValueAnimator.isRunning()) {
            Log.d(TAG, "stopAnimator: ");
            mValueAnimator.cancel();
        }
    }
}
