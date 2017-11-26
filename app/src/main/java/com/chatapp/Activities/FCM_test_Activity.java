package com.chatapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chatapp.R;
import com.chatapp.Service.CallbackWithRetry;
import com.chatapp.Service.Injector;
import com.chatapp.Service.Model.FcmBodyModel;
import com.chatapp.Service.Model.FcmDataModel;
import com.chatapp.Service.onRequestFailure;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public class FCM_test_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm_test_);

        FirebaseMessaging.getInstance().subscribeToTopic("test");

        Button button = (Button) findViewById(R.id.send_hi) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_push_notfication();
            }
        });
    }

    private void send_push_notfication() {

        FcmBodyModel fcmBodyModel = new FcmBodyModel("/topics/test" , new FcmDataModel("hi")) ;
        Call<JsonObject> call =  Injector.provideFcmApi().send_fcm(
                fcmBodyModel
        );

        call.enqueue(new CallbackWithRetry<JsonObject>(10, 30000, call, new onRequestFailure() {
            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext() , " please check your internet connection" , Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(getApplicationContext() , " fcm message has sent " , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
