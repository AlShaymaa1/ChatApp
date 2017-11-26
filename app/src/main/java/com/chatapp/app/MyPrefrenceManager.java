package com.chatapp.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;

import com.chatapp.Activities.Splash_Activity;
import com.chatapp.Manifest;
import com.chatapp.Model.UserModel;

/**
 * Created by Shaymaa on 18/09/2017.
 */

public class MyPrefrenceManager {
    String KEY_ID="id";
    String KEY_NAME="name";
    String KEY_EMAIL="email";
    String KEY_PASS="pass";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public MyPrefrenceManager(Context context) {
        this.context = context;
        pref=context.getSharedPreferences("chat app",Context.MODE_PRIVATE);
        editor=pref.edit();
    }
    public   void storeuser(UserModel userModel){
        editor.putString(KEY_ID,userModel.getId());
        editor.putString(KEY_NAME,userModel.getName());
        editor.putString(KEY_EMAIL,userModel.getEmail());
        editor.putString(KEY_PASS,userModel.getPass());

        editor.commit();
    }
   public UserModel getuser (){
        String is_exist=pref.getString(KEY_ID,null);
        if(is_exist==null){
            return null;
        }
        UserModel userModel=new UserModel();
        userModel.setId(pref.getString(KEY_ID,""));
        userModel.setName(pref.getString(KEY_NAME,""));
        userModel.setEmail(pref.getString(KEY_EMAIL,""));
        userModel.setPass(pref.getString(KEY_PASS,""));

        return userModel;
    }
    public   void clear (){
        editor.clear();
        editor.commit();
        Intent intent=new Intent(context, Splash_Activity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent= IntentCompat.makeRestartActivityTask(cn);
        context.startActivity(mainIntent);
    }



}
