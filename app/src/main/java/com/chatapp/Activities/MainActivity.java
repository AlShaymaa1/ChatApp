package com.chatapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chatapp.Model.UserModel;
import com.chatapp.R;

import com.chatapp.app.MyPrefrenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    EditText name,pass;
    View container  , progressView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Configure Google
        // start
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //end




        name = (EditText) findViewById(R.id.username_input) ;
        pass = (EditText) findViewById(R.id.pass_input) ;

        progressView=findViewById(R.id.loading);
        container=findViewById(R.id.container);

        Button  re_Button = (Button) findViewById(R.id.register_btt);
        re_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  reqintent = new Intent(getApplicationContext() ,
                        activity_regesteration.class);
                startActivity(reqintent);
            }
        });


        Button login_Button = (Button) findViewById(R.id.login_btt);
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }


    void login (){
if(check_empty(name)){
    return;
    }
        String name_value = name.getText().toString();
        if(check_empty(pass)){
    return;
    }
        final String pass_value = pass.getText().toString();

       showloading(true);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference user_ref =  database.getReference("Users") ;
        user_ref.orderByChild("email").equalTo(name_value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("data"  ,  dataSnapshot.toString());
                if (!dataSnapshot.hasChildren() ){

                    // login err
                    Log.e("bcew",dataSnapshot.getChildren().toString());
                    Toast.makeText(getApplicationContext(),"please check your data " , Toast.LENGTH_LONG).show();
                    showloading(false);
                    return;
                }

                for ( DataSnapshot child  : dataSnapshot.getChildren()){

                    if (child.exists()){
                        // login
                        final  UserModel user_model = child.getValue(UserModel.class);
                        if (user_model.getPass().equals(pass_value)){
                            // open home activity
                            String token  = FirebaseInstanceId.getInstance().getToken();
                            database.getReference("Users").child(user_model.getId())
                                    .child("token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new MyPrefrenceManager(getApplicationContext()).storeuser(user_model);
                                    Intent intent = new Intent(getApplicationContext() , activity_home.class) ;
                                    intent.putExtra("user" , user_model);
                                    intent.putExtra("message" , "value");
                                    intent.putExtra("message2" , 1);
                                    startActivity(intent);
                                    finish();
                                }
                            }) ;



                        }else {
                            // login error
                            Toast.makeText(getApplicationContext(),"please check your data " , Toast.LENGTH_LONG).show();
                        }
                    }else{
                        // login err
                        Toast.makeText(getApplicationContext(),"please check your data " , Toast.LENGTH_LONG).show();

                    }
                    showloading(false);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    boolean check_empty(EditText editText ){
        if (TextUtils.isEmpty(editText.getText().toString())){
            editText.setError("please fill this filed");
            editText.requestFocus() ;
            return true;
        }else  return false ;

    }
    void showloading(boolean vis){
        if(vis==true){
            container.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
        }
        else {
            container.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.GONE);
        }

    }

}
