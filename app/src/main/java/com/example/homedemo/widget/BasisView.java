package com.example.homedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

//每一个ViewGroup的大小，都跟它的 childView /childViewGroup + parentViewGroup 有关
//onMeasure 度量，自上而下，层层遍历 (递归，计算子布局大小，反馈给上层布局，直到最顶层)

//https://blog.csdn.net/qq_40881680/article/details/82378452
//MeasureSpec  int 32位  高2位表示 mode  00 01 10 //位运算
//MODE_SHIFT = 30
//MODE_MASK  = 0x3 << MODE_SHIFT;

//<< MODE_SHIFT 左移30位
//UNSPECIFIED - 0 << MODE_SHIFT   不对view大小做限制  (多用于系统)
//EXACTLY     = 1 << MODE_SHIFT  确切的大小 如 100dp
//AT_MOST     = 2 << MODE_SHIFT  大小不可超过某个数值，譬如 matchParent 最大不能超过父布局




//自定义view生命周期
// 1 构造函数  - view 初始化
// 2 onMeasure - 测量view大小
// 3 onSizeChange - 确定view的大小
// 4 onLayout - 确定子View的布局（包含子View时用）
// 5 判断是否重绘
// 6 onDraw - 绘制实际内容
// 7 视图状态改变 - 用户操作/自身变化引起  -- （invalidate()）回到判断5
// 8 结束
//onMeasure 测量view的大小  onlayout 确定子View的布局  onDraw 绘制实际内容
//自定义View主要实现 onMeasure onDraw方法
//自定义ViewGroup 主要实现 onMeasure onLayout 方法  onDraw一般是绘制个背景色
public class BasisView extends View {

    private Paint mPaint;
    private Rect mRect;
    private Path mPathLine;
    private Path mPathAngle;
    private RectF mRectF;

    //代码中通过 new  - 调用的构造
    public BasisView(Context context) {
        super(context);
        init(context);
    }

    //xml布局中调用 - 绘制xml时通过反射调用
    public BasisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttrs(context, attrs);
    }

    //app存在 不同Style时候，调用此构造，譬如，白天模式，夜晚模式，
    public BasisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

    }

    private void init(Context context) {
        initPaint();
        initRect();
        initRectF();
        initPath();
        initRegion();
    }

    /**
     * 初始化区域
     */
    private void initRegion() {
        Region region = new Region();
        //构造
        //Region(Region region)  复制一个Region 相当于 自身的 set(Region region)
        //Region(Rect r)  绘制矩形区域
        //Region(int left, int top, int right, int bottom)  绘制指定区域

        //setEmpty()   清空区域
        //set(Region region)  set之后会覆盖之前的区域
        //set(int left, int top, int right, int bottom)

        //关键，构造不规则的区域
        //setPath(Path path, Region clip)
        //Path path 构造区域的路径
        //Region clip 区域
        //Path 跟 Region 取交集得出最终的区域(重合区域)


        //Region 区域操作
//        Rect rect1 = new Rect(100,100,400,200);
//        Rect rect2 = new Rect(200,0,300,300);
//
//        Region region1 = new Region(rect1);
//        Region region2 = new Region(rect2);

    }

    /**
     * 初始化path
     */
    private void initPath() {
        mPathLine = new Path();
        mPathAngle = new Path();
    }

    /**
     * 初始化椭圆矩形
     */
    private void initRectF() {
        mRectF = new RectF(100, 10, 200, 100);
    }

    /**
     * 初始化矩形
     */
    private void initRect() {
        mRect = new Rect(100, 550, 650, 650);
    }

    /**
     * 初始化 画笔
     */
    private void initPaint() {
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw
//        drawBackground(canvas);
//        drawCircle(canvas);
//        drawLine(canvas);
//        drawPoint(canvas);
//        drawRect(canvas);
//        drawPath(canvas);
//        drawAnglePath(canvas);
//        drawMineRegion(canvas);

        //canvas
//        translateCanvas(canvas);
        clipCanvas(canvas);


    }

    /**
     * 平移画布
     * 画布原始状态是以左上角为原点，向右是X轴正方向，向下是Y轴正方向。
     * 值为负数，即为负方向
     */
    private void translateCanvas(Canvas canvas) {
        canvas.translate(50,50);
        //注意：
        //0.canvas其实是一个很虚幻的概念，相当于一个透明的图层，每次在canvas上面画图（调用drawXXX），都会产一个透明层，然后在这个透明层上面画图，画完之后覆盖在屏幕上展示。
        //1.每次调用 drawXXX，都会产生一个全新的透明图层
        //2.如果在drawXXX之前，先调用平移，旋转等对canvas进行了操作，这个操作是不可逆的，每次产生的新画布，都是在操作之后的位置
        //3.当canvas跟屏幕合成的时候，超出屏幕的范围不会展示出来。




    }

    private void clipCanvas(Canvas canvas){
        //裁剪画布,过程不可逆，需要禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);//禁用硬件加速

        //1.将canvas全部绘制成红色,背景是全红的
        canvas.drawColor(Color.RED);
        //2.截取指定位置
        canvas.clipRect(100,100,200,200);
        //3.再次绘制成绿色，画布修改不可逆，绿色，仅仅绘制在了截取的指定位置上
        canvas.drawColor(Color.GREEN);

        canvas.clipRect(150,150,180,180);
        canvas.drawColor(Color.YELLOW);
    }

    private void drawMineRegion(Canvas canvas) {
        //Region 区域操作
        Rect rect1 = new Rect(100,100,400,200);
        Rect rect2 = new Rect(200,0,300,300);

        Region region1 = new Region(rect1);
        Region region2 = new Region(rect2);

        region1.op(region2, Region.Op.DIFFERENCE);
//        TODO drawRegion 这个方法没找到
//        Region.drawRegion(canvas,region1,mPaint);
    }

    public void getColorARGB() {
        //提取色值中的  A R G B
        int alpha = Color.alpha(Color.RED);
        int red = Color.red(Color.RED);
        int green = Color.green(Color.RED);
        int blue = Color.blue(Color.RED);
    }

    /**
     * 绘制弧度
     */
    private void drawAnglePath(Canvas canvas) {
        mPathAngle.moveTo(10, 10);
        //startAngle  弧开始的角度，以X轴正方向为 0度
        //sweepAngle  弧持续的角度
        //forceMoveTo 是否强制将弧的起点作为绘制的起始位置
        mPathAngle.arcTo(mRectF, 0, 90, true);
        //绘制曲线
        canvas.drawPath(mPathAngle, mPaint);
    }

    /**
     * 绘制路径
     */
    private void drawPath(Canvas canvas) {
//        Path path = new Path();
        //设置线条的起始点
        mPathLine.moveTo(10, 10);
        //由起始点，连接到目标点
        mPathLine.lineTo(10, 100);
        mPathLine.lineTo(300, 100);
        //闭合
        mPathLine.close();
        canvas.drawPath(mPathLine, mPaint);
    }

    /**
     * 绘制矩形 (new 构造的代码，建议提到onDraw 外面处理)
     */
    private void drawRect(Canvas canvas) {
        //方法一 直接构造
//        Rect rect = new Rect(100,550,650,650);
        //方法二 间接构造
        //Rect rect = new Rect();
        //rect.set(250,250,350,350);
        canvas.drawRect(mRect, mPaint);
    }

    /**
     * 绘制画布背景(【经代码测试】：画布也是有层级的，drawColor放在最先，这样背景色才会 显示在最底层)
     */
    public void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.GREEN);
//        canvas.drawARGB(255,255,255,255);
//        canvas.drawRGB(55,255,255);
    }


    /**
     * 绘制圆
     */
    public void drawCircle(Canvas canvas) {
        canvas.drawCircle(0, 0, 50, mPaint);
    }

    /**
     * 绘制线条
     */
    public void drawLine(Canvas canvas) {
        //绘制线条，左 上 右 下
        //绘制线条，线条粗细跟画笔宽度有关系，跟画笔样式无关
        canvas.drawLine(120, 120, 300, 300, mPaint);
    }

    /**
     * 绘制点
     */
    public void drawPoint(Canvas canvas) {
        //绘制点
        canvas.drawPoint(500, 500, mPaint);
    }



}
