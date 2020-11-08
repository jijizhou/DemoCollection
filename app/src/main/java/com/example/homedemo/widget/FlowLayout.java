package com.example.homedemo.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private int mHorizontalSpacing = 16;//统一的水平间距
    private int mVerticalSpacing = 16;//统一的垂直间距
    private List<List<View>> allLines;//记录所有行，一行一行的存储，用于onLayout
    private List<Integer> lineHeights; //记录所有的行高，用于onLayout

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initMeasureParams(){
        allLines = new ArrayList<>();
        lineHeights = new ArrayList<>();
    }

    /**
     * 测量
     * 测量需要递归测量。父的大小需要所有子的大小一起决定
     * @param widthMeasureSpec 父的父给的测量参数
     * @param heightMeasureSpec 父的父给的测量参数
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //初始化布局需要的参数，构造数据仅仅只会初始化一次，onMeasure onLayout onDraw 根据需求会调用多次，所以需要在onMeasure中初始化
        initMeasureParams();

        //获取最外层ViewGroup解析的宽高
        int selWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selHeight = MeasureSpec.getSize(heightMeasureSpec);

        int parentNeedWidth = 0;//measure过程中，子view需要父viewGroup的宽/高
        int parentNeedHeight = 0;

        List<View> lineViews = new ArrayList<View>();
        int lineWithUsed = 0;//记录单行已经使用的width
        int lineHeight = 0;//记录一行的高


        //获取内边距
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();

        //获取所有 子View + 子ViewGroup 的个数
        int childCount = getChildCount();

        //遍历子项，测量
        for (int i = 0; i < childCount; i++) {
            //获取到子项
            View childAt = getChildAt(i);
            //获取到子项的布局参数
            LayoutParams childAtLayoutParams = childAt.getLayoutParams();
            //调用系统测量算法（父的Spec参数，内边距，子项的宽/子项的高）
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingStart + paddingEnd, childAtLayoutParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childAtLayoutParams.height);
            //将测算出的测算系数，传递个子项的测量方法
            childAt.measure(childWidthMeasureSpec,childHeightMeasureSpec);
            //获取到子项的宽高
            int childMeasuredWidth = childAt.getMeasuredWidth();
            int childMeasuredHeight = childAt.getMeasuredHeight();

            //判断折行
            //子view的宽 + 已使用的宽 + 间距 > 总共的宽
            if(childMeasuredWidth + lineWithUsed + mHorizontalSpacing > selWidth){
                //折行
                //记录每行高度，onLayout使用
                allLines.add(lineViews);
                lineHeights.add(lineHeight);
                //归零数据
                lineViews = new ArrayList<>();
                lineWithUsed = 0;
                lineHeight = 0;

                //累加高
                parentNeedHeight = parentNeedHeight + lineHeight + mVerticalSpacing;
                //找出最长宽（父类最大宽 / 累计使用宽 + 间距）
                parentNeedWidth = Math.max(parentNeedWidth,lineWithUsed + mHorizontalSpacing);

            }

            //将所有的子View添加到集合中
            lineViews.add(childAt);
            lineWithUsed = lineWithUsed + childMeasuredWidth + mHorizontalSpacing;
            lineHeight = Math.max(lineHeight,childMeasuredHeight);

        }

        //根据子view的测量结果，重新测量自己的ViewGroup
        //获取父布局给自己的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //根据不同的测量模式，获取最后的宽高
        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selWidth : parentNeedWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selHeight : parentNeedHeight;
        //测量自己
        setMeasuredDimension(realWidth,realHeight);

    }
    /**
     * 屏幕坐标系
     * 屏幕左上角为 （0,0）
     * 横向为 X轴
     * 竖向为 Y轴
     * 虚拟-高度 为 Z轴
     *
     *
     * 视图坐标系
     * 以ViewGroup的左上角为原点（0,0）
     * view.getTop      view最上边，到父上边的距离
     * view.getBottom   view最下边，到父上边的距离
     * view.getLeft     view最左边，到父左边的距离
     * view.getRight    view最右边，到父左边的距离
     */

    /**
     * onMeasure 之后的 调用 onLayout
     * 这里使用的是 视图坐标系
     *
     * getMeasureWidth
     * 在measure() 过程结束就可以获取到对应的值，通过setMeasuredDimension()设置的。
     *
     * getWidth
     * 在layout()过程结束后才能获取到
     * 通过视图右边的坐标减去左边的坐标计算出来的
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局，不需要考虑父亲
        //反推，所有的子类都布局完了，父也相当于布局完了

        //获取父的内边距，初始需要留出父的内边距，子项，布局是在父的内边距之后的
        int curL = getPaddingLeft();
        int curT = getPaddingTop();
        //获取 onMeasure() 测量后的数据，所有数据一共布局了多少行
        int lineCount = allLines.size();
        //遍历行数，按行数依次取出保存的view，布局每行view的位置
        for (int i = 0; i < lineCount; i++) {
            //获取每行的view数据
            List<View> lineViews = allLines.get(i);
            //开始单行内view的for循环，获取每行，不同的行高
            int lineHeight = lineHeights.get(i);
            for (View lineView : lineViews) {
                //起始的左边距 = 父的内边距
                int left = curL;
                //top 距离，是每行固定的
                int top = curT;
                //右边距，左边距 + view的宽度
                // 【注意】这边需要使用 【View.getMeasuredWidth/getMeasuredHeight】 获取宽高，在onMeasure之后可以获取到对应数据
                // 【注意】view.getWidth/getHeight 获取的宽高，是【在onLayout()之后计算出来的】，当前不适用，当前我们依旧在onLayout方法中
                int right = left + lineView.getMeasuredWidth();
                int bottom = top + lineView.getMeasuredHeight();
                lineView.layout(left,top,right,bottom);

                //成功布局一个元素之后，下一个元素的左边距，需要从上一个元素的最左位置开始 【right】 并且要添加上我们固定的水平边距 【mHorizontalSpacing】
                curL = right + mHorizontalSpacing;
            }
            //在单行的for循环外面，开始清零记录数据，为下一行的循环初始化数据
            //重置左边距 = 父的内边距
            curL = getPaddingLeft();
            //累加TOP高度，累计高度【curT】 + 上一行的高度【lineHeight】 + 行的垂直间距【mVerticalSpacing】
            curT = curT + lineHeight + mVerticalSpacing;
        }

    }
}
