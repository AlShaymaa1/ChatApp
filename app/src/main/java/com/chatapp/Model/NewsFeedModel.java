package com.chatapp.Model;

/**
 * Created by shaymaa on 8/28/2017.
 */

public class NewsFeedModel {
    String title;
    String img;
    String user_id;

    public NewsFeedModel() {
    }

    public NewsFeedModel(String title, String img, String user_id) {
        this.title = title;
        this.img = img;
        this.user_id = user_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
