package com.chatapp.Model;

import java.io.Serializable;

/**
 * Created by shaymaa on 9/21/2017.
 */

public class MessModel implements Serializable {

    String message   , user_id;
    String  token  , room_id ;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public MessModel(String message, String user_id) {
        this.message = message;
        this.user_id = user_id;
    }

    public MessModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
