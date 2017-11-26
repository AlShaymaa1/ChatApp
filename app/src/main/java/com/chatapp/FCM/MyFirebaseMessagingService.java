package com.chatapp.FCM;
/**
 * Created by Asmaa on 5/27/2016.
 */

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       //testing incoming messeage

//        Log.e("incomeing notfication" , remoteMessage.getNotification().getBody()) ;
        Log.e("incoming message",remoteMessage.getData().toString());


    }


    // [END receive_message]



}