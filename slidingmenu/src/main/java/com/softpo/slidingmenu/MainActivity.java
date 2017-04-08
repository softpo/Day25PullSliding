package com.softpo.slidingmenu;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //将菜单加到Activity，通过滑动显示菜单
    private SlidingMenu mSlidingMenu;
    
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSlidingMenu();

        initView();
        getWindow().setBackgroundDrawableResource(R.mipmap.p17);

    }

    private void initView() {
        mButton = (Button) findViewById(R.id.clearCache);
        
        mButton.setOnClickListener(this);
    }

    private void initSlidingMenu() {
        mSlidingMenu = new SlidingMenu(this);

        //设置菜单模式，从左侧滑出
        mSlidingMenu.setMode(SlidingMenu.LEFT);

        //设置滑动的模式：从边界滑出
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        //设置菜单
        mSlidingMenu.setMenu(R.layout.menu);

        //关联Activity，显示到Activity
        mSlidingMenu.attachToActivity(this,SlidingMenu.SLIDING_WINDOW);

        //菜单添加到Activity（布局）
        //behind 对应----->菜单
        mSlidingMenu.setBehindOffset(200);

//        mSlidingMenu.setAboveOffset(200);

        //滑动时视差效果 范围0 到1
        mSlidingMenu.setBehindScrollScale(0.5f);

        //behind 对应菜单
        mSlidingMenu.setBehindCanvasTransformer(
                new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas,
                                        float percentOpen) {
                //参数一代表画布，canvas决定着菜单滑出来时，如何进行绘制
                //参数二打开的百分比 0 到1.0f
                float scale = 0.5f+percentOpen*0.5f;
                canvas.scale(scale,scale,
                        canvas.getWidth()/2,canvas.getHeight()/2);
            }
        });
        //Activity 的布局界面加动画
        mSlidingMenu.setAboveCanvasTransformer(
                new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas,
                                        float percentOpen) {
                float s = 1 - percentOpen*0.5f;

                canvas.scale(s,s,0,canvas.getHeight()/2);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "清除数据成功", Toast.LENGTH_SHORT).show();

        //将菜单进行关闭
        mSlidingMenu.toggle();
    }

    public void show(View view) {
        mSlidingMenu.toggle();
    }
}
