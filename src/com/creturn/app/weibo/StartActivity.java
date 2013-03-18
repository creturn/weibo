package com.creturn.app.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import com.creturn.app.weibo.ui.AuthorActivity;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

import java.io.IOException;

public class StartActivity extends Activity {
	public static int GET_ACCESS_TOKEN_ID = 1;
	private Oauth2AccessToken accessToken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public	void init(){
		this.accessToken = new Oauth2AccessToken();
	}
	public void getAcessToken(View view){
		 Intent intent = new Intent(getApplicationContext(), AuthorActivity.class);
		 startActivityForResult(intent, GET_ACCESS_TOKEN_ID);
	}
	public void getUserInfo(View view){
        StatusesAPI statusesAPI = new StatusesAPI(this.accessToken);
        statusesAPI.homeTimeline(0,0,20,1,false, WeiboAPI.FEATURE.ALL,false,new RequestListener() {
            @Override
            public void onComplete(String s) {
               Log.i("test","Weibo:"+s);
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == GET_ACCESS_TOKEN_ID) {
			Bundle dataBundle = data.getExtras();
			this.accessToken.setExpiresTime(dataBundle.getLong("expiresTime"));
			this.accessToken.setToken(dataBundle.getString("accessToken"));
			this.accessToken.setRefreshToken(dataBundle.getString("refreshToken"));
			Log.i("test", "callback acesstoken:" + this.accessToken.getToken());
		}
	}
	
	 

}
