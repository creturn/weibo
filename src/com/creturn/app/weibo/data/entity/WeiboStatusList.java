package com.creturn.app.weibo.data.entity;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: return
 * Date: 13-2-27
 * Time: 下午5:31
 * To change this template use File | Settings | File Templates.
 */
public class WeiboStatusList {

    private List<WeiboStatus> StatusList;

    //解析json数据
    public List<WeiboStatus> parser(String data){
        this.StatusList = new ArrayList<WeiboStatus>();
        JSONTokener jsonToken = new JSONTokener(data);
        try{
            Log.i("test","json data:"+data);
            JSONObject jsonObject = (JSONObject) jsonToken.nextValue();

            JSONArray dataArray = jsonObject.getJSONArray("statuses");
            Log.i("test","dataArrayLengh:"+dataArray.length());
            if (dataArray.length() > 0){
                for(int i = 0; i < dataArray.length(); i++){
                    WeiboStatus weiboStatusTmp = new WeiboStatus();
                    weiboStatusTmp.parser(dataArray.getString(i));
                    this.StatusList.add(weiboStatusTmp);
                }
            }
        }
        catch (Exception e){
            Log.i("test","出错了！"+e.getMessage());
        }
        return this.StatusList;
    }
}
