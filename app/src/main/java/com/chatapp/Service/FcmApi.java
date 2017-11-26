package com.chatapp.Service;

import com.chatapp.Service.Model.FcmBodyModel;
import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by shaymaa on 9/20/2017.
 */

public interface FcmApi {


    @Headers({
            "Content-Type:application/json" ,
            "Authorization:key=AIzaSyAXnA3bYGJcyegl3KEJ_2fdVEw8O8rqP2I"
    })
    @POST("/fcm/send")
    Call<JsonObject> send_fcm(
            @Body FcmBodyModel bodyModel
    );

    Call<JsonObject> send_chat_noti(FcmBodyModel fcmBodyModel);

}
