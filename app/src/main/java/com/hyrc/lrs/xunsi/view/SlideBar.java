package com.hyrc.lrs.xunsi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.hyrc.lrs.xunsi.R;


/**
 * @description 作用:
 * @date: 2020/3/30
 * @author: 卢融霜
 */
public class SlideBar extends View {
    private static final String[] BAR_LETTERS = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"};
    private Paint paint = new Paint();
    /*
    选项的数组下标
     */
    private int selectIndex = -1;
    private onTouchLetterListener onTouchLetterListener;
    //背景是否变化
    private boolean isShowBkg = false;

    public SlideBar(Context context) {
        super(context);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 触摸监听
     */
    public interface onTouchLetterListener {
        /**
         * 监听
         *
         * @param letter
         * @param touch  touch
         */
        void onTouchLetterChange(String letter, boolean touch);
    }

    public void setOnTouchLetterListener(onTouchLetterListener onTouchLetterListener) {
        this.onTouchLetterListener = onTouchLetterListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowBkg == true) {
            //显示背景
            canvas.drawColor(getResources().getColor(R.color.transparent));
        }
        int height = getHeight();
        int width = getWidth();
        //每个字母占得高度
        int singleHeight = (height - 20) / BAR_LETTERS.length;
        //遍历字母
        for (int i = 0; i < BAR_LETTERS.length; i++) {
            paint.setColor(getResources().getColor(R.color.black));
            paint.setTextSize(getResources().getDimension(R.dimen.dp_10));
            //设置抗锯齿样式
            paint.setAntiAlias(true);
            //如果被选中
            if (i == selectIndex) {
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                //加粗
                paint.setFakeBoldText(true);
            }
            //从左下角开始绘制
            //x轴
            float xpos = (width - paint.measureText(BAR_LETTERS[i])) / 2;
            //y轴 （i从0开始算）
            float ypos = singleHeight * i + singleHeight;
            //开始绘制
            canvas.drawText(BAR_LETTERS[i], xpos, ypos, paint);
            paint.reset();//重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float ypos = event.getY();
        //上一次触发的坐标
        int oldSelectIndex = selectIndex;
        //根据触摸的位置确定点的哪个字母
        int newSelectIndex = (int) (ypos / getHeight() * BAR_LETTERS.length);


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isShowBkg = true;
                if (onTouchLetterListener != null && oldSelectIndex != newSelectIndex
                        && newSelectIndex >= 0 && newSelectIndex <= BAR_LETTERS.length) {
                    //注册点击事件并传入字母
                    onTouchLetterListener.onTouchLetterChange(BAR_LETTERS[newSelectIndex], true);
                    oldSelectIndex = newSelectIndex;
                    selectIndex = newSelectIndex;
                    invalidate();//重新绘制

                }
                break;
            case MotionEvent.ACTION_UP:

                isShowBkg = false;
                selectIndex = -1;
                invalidate();
                //注册点击事件并传入字母
                if (onTouchLetterListener != null) {
                    onTouchLetterListener.onTouchLetterChange(BAR_LETTERS[newSelectIndex], false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (onTouchLetterListener != null && oldSelectIndex != newSelectIndex
                        && newSelectIndex >= 0 && newSelectIndex <= BAR_LETTERS.length) {
                    //注册点击事件并传入字母
                    onTouchLetterListener.onTouchLetterChange(BAR_LETTERS[newSelectIndex], true);
                    oldSelectIndex = newSelectIndex;
                    selectIndex = newSelectIndex;
                    //重新绘制
                    invalidate();

                }
                break;
        }


        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}