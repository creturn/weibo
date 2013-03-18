package com.creturn.app.weibo.data.entity;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * User: return
 * Date: 13-2-26
 * Time: 下午6:13
 * To change this template use File | Settings | File Templates.
 */
public class WeiboUser {
    private int id;
    private String idstr;
    private String screen_name;
    private String name;
    private int province;
    private int city;
    private String location;
    private String description;
    private String url;
    private String profile_image_url;
    public void parser(String data){
        JSONTokener jsonToken = new JSONTokener(data);
        try {
            JSONObject jsonObject = (JSONObject) jsonToken.nextValue();
           
            this.setScreen_name(jsonObject.getString("screen_name"));
            this.setId(jsonObject.getInt("id"));
            this.setDescription(jsonObject.getString("description"));
            this.setUrl(jsonObject.getString("url"));
            this.setProfile_image_url(jsonObject.getString("profile_image_url"));
            this.setName(jsonObject.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.i("test", "user解析json錯誤");
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }
}
