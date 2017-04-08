package com.softpo.swiperefresh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //layout ：包裹具体控件
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;
    private List<String> mData = new ArrayList<>();
    private BaseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initData();

        initLv();

        initSwipe();
    }

    private void initSwipe() {
        //设置背景颜色
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.YELLOW);

        //设置加载圈的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE);

        //设置加载圈的大小
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        int distance = getResources().getDisplayMetrics().heightPixels/8;
        //设置向下拉动多少，触发下拉刷新
        mSwipeRefreshLayout.setDistanceToTriggerSync(distance);

        //加载的视图，在哪里显示
        mSwipeRefreshLayout.setProgressViewEndTarget(true,500);

        //设置下拉监听
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initLv() {
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                mData);

        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mData.add("人生自古谁无屎，每天一坨保健康");
        }
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        mListView = (ListView) findViewById(R.id.lv);
    }

    @Override
    public void onRefresh() {
        //刷新
        //联网获取新的数据
        //模拟
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    //切换到主线程
                    mListView.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++) {
                                mData.add(0,"明月照纱窗格格孔明诸葛亮");
                            }
                            mAdapter.notifyDataSetChanged();

                            //停止刷新操作
                            if(mSwipeRefreshLayout.isRefreshing()){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
