package com.example.homedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BasisView extends View {

    private Paint mPaint;
    private Rect mRect;
    private Path mPath;

    public BasisView(Context context) {
        super(context);
        init(context);
    }

    public BasisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttrs(context,attrs);
    }

    public BasisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

    }

    private void init(Context context) {
        mPaint = new Paint();
        //设置颜色0xAARRGGBB  A 透明度  R 红  G 绿  B 蓝色
        //A 0~255 取值越小 透明度越高，图像越透明，取值0，完全看不见
        //R G B  0色值完全不可见，255色值完全显示
        mPaint.setColor(Color.RED);
        //设置填充样式
        //FILL 仅仅填充内部 （内圆）
        //STROKE 仅仅描边  （圆环）
        //FILL_AND_STROKE 填充内部 + 描边  （最大）
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置画笔宽度
        mPaint.setStrokeWidth(5);

        //抗锯齿
        mPaint.setAntiAlias(true);

        mRect = new Rect(100,550,650,650);
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制画布背景(【经代码测试】：画布也是有层级的，drawColor放在最先，这样背景色才会显示在最底层)
        canvas.drawColor(Color.GREEN);
//        canvas.drawARGB(255,255,255,255);
//        canvas.drawRGB(55,255,255);

        //绘制圆
        canvas.drawCircle(0,0,50,mPaint);

        //绘制线条，左 上 右 下
        //绘制线条，线条粗细跟画笔宽度有关系，跟画笔样式无关
        canvas.drawLine(120,120,300,300,mPaint);

        //绘制点
        canvas.drawPoint(500,500,mPaint);

        //绘制矩形 (new 构造的代码，建议提到onDraw 外面处理)
        //方法一 直接构造
//        Rect rect = new Rect(100,550,650,650);
        //方法二 间接构造
        //Rect rect = new Rect();
        //rect.set(250,250,350,350);
        canvas.drawRect(mRect,mPaint);

        //提取色值中的  A R G B
        int alpha = Color.alpha(Color.RED);
        int red = Color.red(Color.RED);
        int green = Color.green(Color.RED);
        int blue = Color.blue(Color.RED);

//        Path path = new Path();
        //设置线条的起始点
        mPath.moveTo(200,700);
        //由起始点，连接到目标点
        mPath.lineTo(300,800);
        mPath.lineTo(159,650);
        //闭合
        mPath.close();


    }
}
