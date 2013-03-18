package com.creturn.app.weibo.data.entity;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.creturn.app.weibo.common.StringUtils;

/**
 * User: return
 * Date: 13-2-27
 * Time: 上午1:38
 */
public class WeiboStatus {
    private int id;
    private String created_at;
    private String text ;
    private String thumbnail_pic;
    private WeiboUser user;
    private int comments_count;
    private WeiboSource retweeted_status;
    private int is_ret = 0;
    //解析微博数据
    public void parser(String data){
        Log.i("test","正在解析json微博数据："+data);
        JSONTokener jsonToken = new JSONTokener(data);
        try {
            JSONObject jsonObject = (JSONObject) jsonToken.nextValue();
            this.setText(jsonObject.getString("text"));
            this.setCreated_at(StringUtils.beautfullTime(jsonObject.getString("created_at")));
            this.setComments_count(jsonObject.getInt("comments_count"));
            this.setId(jsonObject.getInt("id"));
            try{
                this.setThumbnail_pic(jsonObject.getString("thumbnail_pic"));
            }catch (Exception e){
                Log.i("test","没有图片数据");
            }
            try{
            if (!jsonObject.getString("retweeted_status").isEmpty()){
                WeiboSource weiboSource = new WeiboSource();
                weiboSource.parser(jsonObject.getString("retweeted_status"));
                this.setRetweeted_status(weiboSource);
                this.setIs_ret(1);
            }
            } catch (Exception e){
                Log.i("test","不是转发的微博");
            }
            //解析用户
            WeiboUser weiboUser = new WeiboUser();
            weiboUser.parser(jsonObject.getString("user"));
            this.setUser(weiboUser);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int getIs_ret() {
        return is_ret;
    }

    public void setIs_ret(int is_ret) {
        this.is_ret = is_ret;
    }

    public WeiboSource getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(WeiboSource retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public WeiboUser getUser() {
        return user;
    }

    public void setUser(WeiboUser user) {
        this.user = user;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

}
