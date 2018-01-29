package com.view.demo6;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Email 2185134304@qq.com
 * Created by JackChen on 2018/1/27.
 * Version 1.0
 * Description: 自定义View入门--评分控件RatingBar
 */
public class RatingBar extends View {

    private static final String TAG = "RatingBar";
    //设置默认的未选中五角星、选中五角星
    private Bitmap mStarNormalBitmap , mStarFocusBitmap ;
    //设置默认的评分
    private int mGradeNumber = 5 ;

    //设置当前的评分
    private int mCurrentGrade = 0 ;


    public RatingBar(Context context) {
        this(context,null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        //资源id默认传递0
        int starNormalId = typedArray.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (starNormalId == 0){
            throw new RuntimeException("请设置属性 starNormal") ;
        }

        //用BitmapFactory解析资源
        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), starNormalId);

        int starFocusId = typedArray.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (starFocusId == 0){
            throw new RuntimeException("请设置属性 starFocus") ;
        }
        //用BitmapFactory解析资源
        mStarFocusBitmap = BitmapFactory.decodeResource(getResources() , starFocusId) ;


        mGradeNumber = typedArray.getInt(R.styleable.RatingBar_gradeNumber , mGradeNumber) ;

        typedArray.recycle();
    }


    /**
     * 测量控件的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度  1个星星的高度  这里用mStarNormalBitmap和mStarFocusBitmap是一样的   自己去实现 padding + 加上间隔
        int height = mStarFocusBitmap.getHeight() ;
        //宽度  1个星星宽度*星星个数
        int width = mStarFocusBitmap.getWidth() * mGradeNumber ;
        setMeasuredDimension(width , height);

    }


    /**
     *  测量完控件宽和高后，然后开始画
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < mGradeNumber; i++) {
            // i * 星星的宽度
            int x = i * mStarFocusBitmap.getWidth() ;

            //触摸的时候mCurrentGrade的值是不断变化的  i从0开始,mCurrentGrade从1开始
            if (mCurrentGrade > i){
                //当前分数之前
                canvas.drawBitmap(mStarFocusBitmap , x , 0 , null);
            }else{
                canvas.drawBitmap(mStarNormalBitmap , x , 0 , null);
            }

        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //移动、按下、抬起的处理逻辑都是一样的，判断手指的位置，根据当前的位置计算出分数，再去刷新
        //在按下时候按下、移动、抬起事件都会调用，所以下边将按下、抬起注释，可以减少onDraw()的调用
        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN: //按下  尽量减少onDraw()的调用
            case MotionEvent.ACTION_MOVE: //移动
//            case MotionEvent.ACTION_UP: //抬起    尽量减少onDraw()的调用
            float moveX = event.getX();  //event.getX()表示相对于当前控件的距离  event.getRawX()表示相对于屏幕的距离
                Log.e(TAG , "moveX -> "+moveX) ;

                //当前显示的分数
                int currentGrade = (int) (moveX/mStarFocusBitmap.getWidth() + 1);

                //范围问题
                if (currentGrade < 0){
                    currentGrade = 0 ;
                }

                if (currentGrade > mGradeNumber){
                    currentGrade = mGradeNumber ;
                }

                //分数相同时就不要再去绘制了，尽量减少onDraw()的调用
                if (currentGrade == mCurrentGrade){
                    return true ; //return false表示不处理这个事件，那么待会事件也进不来，
                    //return true 表示不要再去调用下边方法了，即就是不去执行下边的代码了
                }

                mCurrentGrade = currentGrade ;
                //再去刷新显示
                invalidate();  //调用这个方法后就又会调用onDraw()方法


                break;
        }
        return true; //return false表示不消费事件，第一次ACTION_DOWN事件进来返回false，表示这个事件我以后都不要了，也就是说以后的事件就不会进来，
        // 所以此处必须返回true，表示消费这个事件

    }
}
