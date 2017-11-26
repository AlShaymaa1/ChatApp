package com.chatapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chatapp.Adapters.MessAdapter;
import com.chatapp.Model.MessModel;
import com.chatapp.R;
import com.chatapp.Service.CallbackWithRetry;
import com.chatapp.Service.Injector;
import com.chatapp.Service.Model.FcmBodyModel;
import com.chatapp.Service.onRequestFailure;
import com.chatapp.app.MyPrefrenceManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class Mess_Activity extends AppCompatActivity {

    String room_id , token  ;
    FirebaseDatabase database ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_);




        database = FirebaseDatabase.getInstance();
        room_id = getIntent().getExtras().getString("room_id") ;
        token = getIntent().getExtras().getString("token") ;

        recycler_setup();
        input_setup() ;
        get_data();

    }

    ArrayList<MessModel> messModels = new ArrayList<>() ;
    MessAdapter adapter ;
    RecyclerView mess_list_view ;
    void recycler_setup(){
        mess_list_view = (RecyclerView) findViewById(R.id.mess_list_view) ;
        adapter = new MessAdapter(messModels , this) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , true) ;
        mess_list_view.setLayoutManager(linearLayoutManager);
        mess_list_view.setAdapter(adapter);
    }


    void get_data(){
        database.getReference("Messages").child(room_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessModel messModel = dataSnapshot.getValue(MessModel.class) ;
//                messModels.add(messModel) ;
//                adapter.notifyDataSetChanged();
                adapter.insert_data(messModel);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

    }


    void input_setup(){
        final EditText editText = (EditText) findViewById(R.id.message_input) ;
        View icon_send  = findViewById(R.id.message_send) ;
        icon_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message  = editText.getText().toString() ;
                if (!TextUtils.isEmpty(message)){
                    editText.setText("");
                    String my_id  = new MyPrefrenceManager(getApplicationContext()).getuser().getId() ;
                    MessModel messModel = new MessModel(message , my_id ) ;
                    database.getReference("Messages").child(room_id).push().setValue( messModel) ;
                    send_notficatio(messModel);

                }



            }
        });

    }

    private void send_notficatio(MessModel messModel) {

        messModel.setToken(FirebaseInstanceId.getInstance().getToken());
        messModel.setRoom_id(room_id);
        Call<JsonObject>  call = Injector.provideFcmApi().send_chat_noti(
                new FcmBodyModel("/"+token
                        , messModel
                )
        );


        call.enqueue(new CallbackWithRetry<JsonObject>(10, 30000, call, new onRequestFailure() {
            @Override
            public void onFailure() {

            }
        }) {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }
        });



    }
}
