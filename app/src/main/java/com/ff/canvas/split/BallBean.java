package com.ff.canvas.split;

/**
 * description: 粒子封装对象
 * author: FF
 * time: 2019/4/6 22:23
 */
public class BallBean {
    public int color;// 图片像素点颜色值
    public float x;// 粒子圆心坐标x
    public float y;// 粒子圆心坐标y
    public float r;// 粒子半径

    public float vX;// 粒子运动水平方向速度
    public float vY;// 粒子运动垂直方向速度
    public float aX;// 粒子运动水平方向加速度
    public float aY;// 粒子运动垂直方向加速度

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getvX() {
        return vX;
    }

    public void setvX(float vX) {
        this.vX = vX;
    }

    public float getvY() {
        return vY;
    }

    public void setvY(float vY) {
        this.vY = vY;
    }

    public float getaX() {
        return aX;
    }

    public void setaX(float aX) {
        this.aX = aX;
    }

    public float getaY() {
        return aY;
    }

    public void setaY(float aY) {
        this.aY = aY;
    }
}
