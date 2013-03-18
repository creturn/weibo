package com.creturn.app.weibo.data.entity;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 微博来源
 * User: return
 * Date: 13-3-3
 * Time: 上午1:05
 * To change this template use File | Settings | File Templates.
 */
public class WeiboSource{
    private static final String TAG = "test";
    private String text;

    public void parser(String data){
        Log.i(TAG, "正在解析Source json数据：" + data);
        JSONTokener jsonToken = new JSONTokener(data);
        try {
            JSONObject jsonObject = (JSONObject) jsonToken.nextValue();
            this.setText(jsonObject.getString("text"));
        } catch (JSONException e) {
            Log.i(TAG,"Source json 数据解析错误："+e.getMessage());
        }

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
