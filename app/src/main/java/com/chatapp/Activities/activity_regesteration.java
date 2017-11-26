package com.chatapp.Activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chatapp.Model.UserModel;
import com.chatapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_regesteration extends AppCompatActivity {
    EditText name  ,  phone  , email , pass  ;
    TextInputLayout nam_layout , phone_layout , emaile_layout , pass_layout ;

    View container  , progressView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regesteration);
        name = (EditText) findViewById(R.id.username_input) ;
        phone = (EditText) findViewById(R.id.phone_input) ;
        email = (EditText) findViewById(R.id.email_input) ;
        pass = (EditText) findViewById(R.id.pass_input) ;

        progressView=findViewById(R.id.loading);
        container=findViewById(R.id.container);


        nam_layout = (TextInputLayout) findViewById(R.id.username_input_layout) ;
        phone_layout = (TextInputLayout) findViewById(R.id.phoneinput_layout) ;
        emaile_layout = (TextInputLayout) findViewById(R.id.email_input_layout) ;
        pass_layout = (TextInputLayout) findViewById(R.id.pass_input_layout);

        View reg_btt =  findViewById(R.id.reg_btt) ;

        reg_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rigster();
            }
        });

    }


    void rigster(){

        if (check_empty(name ,nam_layout)){
            return;
        }
        String name_value  = name.getText().toString() ;


        if (check_empty(email)){
            return;
        }
        if (check_email(email)){
            return;
        }
        String email_value  = email.getText().toString() ;


        if (check_empty(phone)){
            return;
        }
        String phone_value  = phone.getText().toString() ;



        if (check_empty(pass)){
            return;
        }
        String pass_value  = pass.getText().toString() ;

        final  UserModel userModel = new UserModel();
        userModel.setName(name_value);
        userModel.setEmail(email_value);
        userModel.setPass(pass_value);
        userModel.setPhone(phone_value);

        //start db
        showloading(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference user_ref =  database.getReference("Users") ;
        //search by email
        user_ref.orderByChild("email").equalTo(email_value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    email.setError("already exist");
                    email.requestFocus();
                    showloading(false);
                    return;
                }
                else{

                    //start insert into database
                    String user_push_key  =  user_ref.push().getKey() ;
                    userModel.setId(user_push_key);
                    Log.e("sxbk",userModel.getId().toString());
                    user_ref.child(user_push_key).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showloading(false);
                            Toast.makeText(activity_regesteration.this, "regestration succsee", Toast.LENGTH_SHORT).show();
                        }
                    });

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
    boolean check_email(EditText editText ){
        String value  = editText.getText().toString() ;
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            editText.setError("please enter a valid email ");
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
    boolean check_empty(EditText editText,TextInputLayout textinputlayout ){
        if (TextUtils.isEmpty(editText.getText().toString())){
            textinputlayout.setError("please fill this filed");
            textinputlayout.requestFocus() ;
            return true;
        }else
            textinputlayout.setError(null);
            return false ;

    }
}


