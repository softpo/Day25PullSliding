package com.softpo.day25pullsliding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements PullToRefreshBase.OnRefreshListener,Handler.Callback{

    private PullToRefreshListView mPullToRefreshListView;
    private List<String> mData = new ArrayList<>();

    private Handler mHandler = new Handler(this);
    private ArrayAdapter<String> mAdapter;
    private ILoadingLayout mPullHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initPullLv();


    }

    private void initPullLv() {
        for (int i = 0; i < 100; i++) {
            mData.add(String.format("HelloWorld%s"," Android"+i));
        }

        mAdapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_list_item_1,
               mData);

        mPullToRefreshListView.setAdapter(mAdapter);

        //设置监听
        mPullToRefreshListView.setOnRefreshListener(this);

        //设置头布局
        initPullLvHeader();

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //参数position +1 （因为有头布局）
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "点击的条目是： "+position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initPullLvHeader() {
        mPullHeader = mPullToRefreshListView.getLoadingLayoutProxy(true, false);

        mPullHeader.setPullLabel("刷新更多");
        mPullHeader.setReleaseLabel("放开进行刷新");
        mPullHeader.setRefreshingLabel("拼命加载中……");

        String lastTime = DateUtils.formatDateTime(this,System.currentTimeMillis(),
                DateUtils.FORMAT_ABBREV_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME);

        mPullHeader.setLastUpdatedLabel(lastTime);

    }

    private void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullLv);
    }

    //刷新
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        //获取新的数据，联网请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    //更新数据--->线程跳转--->Handler
                    mHandler.sendEmptyMessage(200);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch(msg.what){
            case 200:
                mData.clear();
                for (int i = 0; i < 100; i++) {
                    //网址进行格式化
                    mData.add(String.format(Locale.CHINA,"第%d条数据%s",i,"香山红叶"));
                }

                //更新适配器
                mAdapter.notifyDataSetChanged();
                //刷新停止
                if(mPullToRefreshListView.isRefreshing())
                    mPullToRefreshListView.onRefreshComplete();

                String lastTime = DateUtils.formatDateTime(this,System.currentTimeMillis(),
                        DateUtils.FORMAT_ABBREV_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME);

                mPullHeader.setLastUpdatedLabel(lastTime);
                break;
            default:
                break;
        }

        return false;
    }
}
