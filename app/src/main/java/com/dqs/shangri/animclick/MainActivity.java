package com.dqs.shangri.animclick;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int mDuration = 10000;
    private Activity activity = this;
    private ImageView mAnimView;
    private ImageView mBottomView;
    private DisplayMetrics mDm;
    private long mStartTime;
    private double mDurationInScreen;
    private double mViewWidth;
    private double widthPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnimView = (ImageView) findViewById(R.id.anim_view);
        mBottomView = (ImageView) findViewById(R.id.botttom_view);


        mDm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDm);

        mBottomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    double diffTime = System.currentTimeMillis() - mStartTime;
                    if (diffTime < mDurationInScreen) {
                        int x = (int) ((1 - diffTime / mDurationInScreen) * widthPixels);
                        if (event.getX() >= x && event.getX() <= (x + mViewWidth)) {
                            Log.e(getClass().getName(), "---------------success------");
                            Toast.makeText(activity, "success", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                return true; //此处必然为true, 否则接收不到up 事件
            }
        });

        animStart(mAnimView);
    }


    private void animStart(View view) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mViewWidth = view.getMeasuredWidth();
        widthPixels = mDm.widthPixels;

        mDurationInScreen = (long) (widthPixels / (widthPixels + mViewWidth) * mDuration); //计算 在屏幕内移动的时间

        //从屏幕的右侧，漂移到屏幕左侧
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, (int) widthPixels, -(int) mViewWidth);
        animator.setDuration(mDuration); //移动10s
        animator.setInterpolator(new LinearInterpolator()); //匀速移动 必须为允许，否则 计算位置将不准确
        mStartTime = System.currentTimeMillis();
        animator.start();
    }
}
