package com.chatapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;

import com.chatapp.R;
import com.chatapp.app.MyPrefrenceManager;

public class Splash_Activity extends AppCompatActivity {
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);


        logo = (ImageView) findViewById(R.id.logo);



        Animation animationSet = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        animationSet.setInterpolator(new AnticipateInterpolator(2));
        animationSet.setDuration(500);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Animation.AnimationListener animatorListener= this ;
                Animation animationSet = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim2);
                animationSet.setInterpolator(new LinearOutSlowInInterpolator());
                animationSet.setDuration(500);
                animationSet.setAnimationListener(animatorListener);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation animationSet = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim3);
                        animationSet.setInterpolator(new FastOutLinearInInterpolator());
                        animationSet.setDuration(500);
                        animationSet.setAnimationListener(animatorListener);
                        logo.startAnimation(animationSet);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                logo.startAnimation(animationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

//        animationSet.setRepeatMode(Animation.INFINITE);
        logo.startAnimation(animationSet);

        // start home after 3 sec

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //if the user logged in start home else start loin
                MyPrefrenceManager myPrefrenceManager=new MyPrefrenceManager(getApplicationContext());
             if(myPrefrenceManager.getuser()==null){
                 //start login
                 Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                 startActivity(intent);
             }else{

              //start home

                 Intent intent=new Intent(getApplicationContext(),activity_home.class);
                 startActivity(intent);
             }

            }
        },3000);

    }
}
