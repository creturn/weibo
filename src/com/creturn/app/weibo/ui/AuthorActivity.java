package com.creturn.app.weibo.ui;
import android.content.SharedPreferences;
import com.creturn.app.weibo.StartActivity;
import com.creturn.app.weibo.R;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.HttpManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
@SuppressLint("SetJavaScriptEnabled")
public class AuthorActivity extends Activity {
	private WebView webView;
	final String key = "705788728";
	final String ser = "d39439b32bb5dab61d3b66a76a7a0085";
	final String cal = "http://weibo.creturn.com/callback.php";
	private String strUrl = "";
	private String strCodeUrl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_view);
		this.init();
		this.getAuthorize();
	}
	/**
	 * 初始化操作
	 */
	public void init() {
		// 初始化webview
		webView = (WebView) findViewById(R.id.auth_view);
		webView.getSettings().setJavaScriptEnabled(true); // 启用javascript
		webView.setWebViewClient(new WebViewC());
	}
	public void getAuthorize() {
		this.strUrl = "https://open.weibo.cn/oauth2/authorize?client_id=" + this.key + "&display=mobile&redirect_uri=" + this.cal;
		this.webView.loadUrl(this.strUrl);
	}
	public void getAccessToken() {
		String code = this.strCodeUrl.split("=")[1];
		String apiUrl = "https://api.weibo.com/oauth2/access_token";
		WeiboParameters params = new WeiboParameters();
		params.add("client_id", this.key);
		params.add("client_secret", this.ser);
		params.add("code", code);
		params.add("redirect_uri", this.cal);
		try {
			String result = HttpManager.openUrl(apiUrl, "POST", params,null);
			Oauth2AccessToken otoken = new Oauth2AccessToken(result);
			Log.i("test","access token:"+otoken.getToken());
			Intent intent = new Intent();
			intent.putExtra("accessToken", otoken.getToken());
			intent.putExtra("expiresTime", otoken.getExpiresTime());
			intent.putExtra("refreshToken", otoken.getRefreshToken());
			setResult(StartActivity.GET_ACCESS_TOKEN_ID, intent);
            //保存acesstooken 到SharedPreferences
            SharedPreferences sp = this.getApplicationContext().getSharedPreferences("weiboAccount",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("accessToken",otoken.getToken());
            editor.putString("refreshToken",otoken.getRefreshToken());
            editor.putLong("expiresTime", otoken.getExpiresTime());
            editor.commit();
			this.finish();
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	 
	class WebViewC extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		/**
		 * 由于授权页面采用https协议 执行此方法接受所有证书
		 */
		public void onReceivedSslError(WebView view, SslErrorHandler handler,SslError error) {
			handler.proceed();
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (url.contains("callback.php?code=")) {
				AuthorActivity.this.setVisible(false);
				AuthorActivity.this.strCodeUrl = url;
				AuthorActivity.this.getAccessToken();
			}
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}
}
