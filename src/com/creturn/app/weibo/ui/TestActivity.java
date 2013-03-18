package com.creturn.app.weibo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.creturn.app.weibo.R;
import com.creturn.app.weibo.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: return
 * Date: 13-3-5
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public class TestActivity extends Activity {
    private PullToRefreshListView pullToRefreshListView;
    private ArrayAdapter<String> pullViewAdapter;
    private List<String> pullStrings;
    private ArrayAdapter<String> listViewAdapter;
    private List<String> listStrings;
    private Handler handler;
    private final int UPDATE_COMPLITE = 1;
    private final int UPDATE_NOW = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.test);
        init();
    }
    public void test(View view){
        this.pullToRefreshListView.onRefreshComplete();
    }
    public void sleep(){
        try {
            Thread.sleep(5000);
            Toast.makeText(this,"加载完成喽！",Toast.LENGTH_LONG).show();
            this.pullToRefreshListView.onRefreshComplete();

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    private void init() {
        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    TestActivity.this.pullToRefreshListView.onRefreshComplete();
                }
                if (msg.what == 2){
                    TestActivity.this.sleep();
                }
            }
        };
        this.pullStrings = new ArrayList<String>();
        this.addData();

        this.pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_list_view);
        this.pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("test","即将刷新数据");
                TestActivity.this.handler.sendEmptyMessage(UPDATE_NOW);

            }
        });

        this.pullViewAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.pullStrings);
        this.pullToRefreshListView.setAdapter(this.pullViewAdapter);
    }

    private void addData() {
        for (int i = 0; i < 20; i++){
            this.pullStrings.add("test"+i);
        }
    }


}
