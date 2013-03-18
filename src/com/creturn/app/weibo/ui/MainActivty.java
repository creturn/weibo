package com.creturn.app.weibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.creturn.app.weibo.R;
import com.creturn.app.weibo.data.adpter.MainListAdapter;
import com.creturn.app.weibo.data.entity.WeiboStatus;
import com.creturn.app.weibo.data.entity.WeiboStatusList;
import com.creturn.app.weibo.widget.PullToRefreshListView;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * User: return
 * Date: 13-2-27
 * Time: 上午1:47
 */
public class MainActivty extends Activity {
    private PullToRefreshListView mainListView;
    private Oauth2AccessToken weiboToken;
    private List<WeiboStatus> weiboStatusList;
    private MainListAdapter listAdapter;
    private Handler handler;
    private final int UPDATE_UI = 1; //更新ui
    private int page = 1; //页数
    private Button footBtn;
    public int flg_act = 0; //标记是刷新还是载入更多
    public int cnt_lst = 0; //统计list上次的总数
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        this.init();
    }
    //初始化操作
    public void init(){
        this.initToken();
        this.initHandler();
        //init foot view
        this.footBtn = new Button(getApplicationContext());
        this.footBtn.setText(R.string.loader_more);
        this.footBtn.setVisibility(View.GONE);
        this.footBtn.setOnClickListener(new View.OnClickListener() {
            //添加加载更多事件
            @Override
            public void onClick(View view) {
                     MainActivty.this.loadMore();
            }
        });
        //init main list view
        this.mainListView = (PullToRefreshListView) findViewById(R.id.main_list_view);
        this.mainListView.addFooterView(this.footBtn);
        this.mainListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivty.this.flg_act = 0;
                MainActivty.this.page = 1;
                MainActivty.this.getWeiboStatusList();
                Log.i("test","开始刷新了。。。");
            }
        });
        this.weiboStatusList = new ArrayList<WeiboStatus>();
        this.listAdapter = new MainListAdapter(getApplicationContext(),weiboStatusList);
        this.mainListView.setAdapter(listAdapter);
        this.initData();      //加载上次的缓存信息
    }
    //初始化Handler
    public void initHandler(){
        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                  if (msg.what == UPDATE_UI){
                      MainActivty.this.listAdapter.notifyDataSetChanged();//通知ui有新书据
                      MainActivty.this.mainListView.onRefreshComplete();
                      MainActivty.this.mainListView.setSelection(MainActivty.this.cnt_lst);
                      MainActivty.this.footBtn.setVisibility(View.VISIBLE);
                      MainActivty.this.showMsg("信息加载完毕!");
                  }
            }
        };
    }
    //提示消息
    public void showMsg(String msg){
        Toast toast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL,Gravity.LEFT,0);
        toast.show();
    }
    //初始化数据，加载缓存信息页在这里进行
    public void initData(){

    }
    //更新微博信息
    public void updateWeiboStatusList(List<WeiboStatus> StatusList) {
        //记录上次数量
        this.cnt_lst = this.weiboStatusList.size();
        if (this.flg_act == 0){
            //如果是刷新就重置数据，并清空list
           this.cnt_lst = 1;
           this.weiboStatusList.clear();
        }

        this.weiboStatusList.addAll(StatusList);
        this.handler.sendEmptyMessageDelayed(UPDATE_UI,0);
    }
    //加载更多
    public void loadMore(){
        this.flg_act = 1; //设置为载入更多
        this.page++;
        this.getWeiboStatusList();
    }
    //初始化token
    public void initToken(){
        this.weiboToken = new Oauth2AccessToken();
        SharedPreferences sp = this.getApplicationContext().getSharedPreferences("weiboAccount",MODE_PRIVATE);
        String accessToken = sp.getString("accessToken","");
        Long expiresTime = sp.getLong("expiresTime",0);
        if(accessToken.equals("")){
            //没有发现token就启用微博认证activity获取
            Intent intent = new Intent(this.getApplicationContext(),AuthorActivity.class);
            startActivity(intent);
            Log.i("test","没找到acessToken");
        } else {
            this.weiboToken.setToken(accessToken);
            this.weiboToken.setExpiresTime(expiresTime);
            Log.i("test","token:"+accessToken);
            Log.i("test","time:"+expiresTime.toString());
        }
    }
    public void update(View view){
        //通知List刷新
        this.mainListView.startRefresh();
    }
    public void getWeiboStatusList(){
        //获取主页微博信息
        StatusesAPI statusesAPI = new StatusesAPI(this.weiboToken);
        statusesAPI.homeTimeline(0,0,20,this.page,false, WeiboAPI.FEATURE.ALL,false,new RequestListener() {
            @Override
            public void onComplete(String s) {

                WeiboStatusList weiboStatusList1 = new WeiboStatusList();
                List<WeiboStatus> weiboStatuses = weiboStatusList1.parser(s);
                MainActivty.this.updateWeiboStatusList(weiboStatuses);
            }
            @Override
            public void onIOException(IOException e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onError(WeiboException e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

}