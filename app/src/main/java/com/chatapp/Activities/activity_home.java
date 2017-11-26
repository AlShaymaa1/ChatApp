package com.chatapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chatapp.Adapters.TitlePagerAdapter;
import com.chatapp.Fragments.fragment_fav;
import com.chatapp.Fragments.fragment_news_feed;

import com.chatapp.Model.UserModel;
import com.chatapp.R;
import com.chatapp.app.MyPrefrenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_home extends AppCompatActivity {
    private EditText search_inputt;
    ImageView menu_icon ;
    PopupMenu popupMenu ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getIntent().getExtras()!=null) {

            UserModel userModel = (UserModel) getIntent().getExtras().get("user");
            // String message =getIntent().getExtras().getString("message");
            Toast.makeText(this, "welcome" + userModel.getName(), Toast.LENGTH_SHORT).show();
        }
        search_setup();
        popSetup() ;
        tabsSetup();

    }


    void tabsSetup(){

        // step 1 define your tab layout that exist in xml file
        TabLayout tabslayout = (TabLayout) findViewById(R.id.tabs);

        // step 2 define your view pager  that exist in xml file
        ViewPager viewPager =  (ViewPager) findViewById(R.id.pager) ;

        // step 3 define your adapter
        TitlePagerAdapter titlePagerAdapter  = new TitlePagerAdapter(getSupportFragmentManager()) ;

//        // step4 add frgament to your adapter with title
//        titlePagerAdapter.addFragment(new NewsFeedFragment() , "news feed");
//        titlePagerAdapter.addFragment(new FavFragment() , "favourits");

//        / step4 add frgament to your adapter with just icon
        titlePagerAdapter.addFragment(new fragment_news_feed() , "");
        titlePagerAdapter.addFragment(new fragment_fav() , "");

        // step 5 add your adapter into your pager
        viewPager.setAdapter(titlePagerAdapter);

        // setp 6  add your pager into your tab layout
        tabslayout.setupWithViewPager(viewPager);

        Drawable newsfeedIcon = ContextCompat.getDrawable(getApplicationContext() , R.drawable.ic_account_circle_black_36dp) ;
        int white = ContextCompat.getColor(getApplicationContext() , R.color.white) ;
        newsfeedIcon.setColorFilter(white , PorterDuff.Mode.SRC_ATOP);
        tabslayout.getTabAt(0).setIcon(newsfeedIcon) ;

        Drawable newsfeedIcon2 = ContextCompat.getDrawable(getApplicationContext() ,
                R.drawable.ic_search_black_24dp) ;
        int gray = ContextCompat.getColor(getApplicationContext() , R.color.gray700) ;
        newsfeedIcon2.setColorFilter(gray , PorterDuff.Mode.SRC_ATOP);
        tabslayout.getTabAt(1).setIcon(newsfeedIcon2) ;

//        viewPager.setCurrentItem(1);



        tabslayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int white = ContextCompat.getColor(getApplicationContext() , R.color.white)  ;
                tab.getIcon().setColorFilter(white , PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int gray = ContextCompat.getColor(getApplicationContext() , R.color.gray700) ;
                tab.getIcon().setColorFilter(gray , PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    void search_setup()
    {
        final ImageView search_icon = (ImageView) findViewById(R.id.search_icon);

        search_inputt = (EditText) findViewById(R.id.search_input) ;

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_key_word = search_inputt.getText().toString() ;
                if (TextUtils.isEmpty(search_key_word)){
                    search_inputt.setError(" please fill the input ");
                    search_inputt.requestFocus();
                }else {
                    Toast.makeText(getApplicationContext() , search_key_word , Toast.LENGTH_LONG).show() ;
                }
            }
        });

    }

    void popSetup (){

        // menu image view declare
        menu_icon = (ImageView) findViewById(R.id.menu_icon);

        // pop declare
        popupMenu = new PopupMenu(activity_home.this, menu_icon ) ;

        // MenuInflater declare
        MenuInflater inflater = popupMenu.getMenuInflater();

        // bind your menu to popmenu
        // setp 1 create menu file
        //to create menu folder click on res then new android resource directory
        // to create menu file click o menu folder then new menu resource file then write yur items in it
        inflater.inflate(R.menu.search_toolbar_menu, popupMenu.getMenu());

        // set click listener to menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.logout_action){
                    new MyPrefrenceManager(getApplicationContext()).clear();
                    Toast.makeText(getApplicationContext() , "logout is clicked " , Toast.LENGTH_LONG).show();
                }

                if (item.getItemId() == R.id.help_action){
                    Toast.makeText(getApplicationContext() , "help is clicked " , Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        // set click listener to menu icon that will open the popup
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        signinannonymously();
    }

    private void signinannonymously (){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.e("homeactivity","sign in success");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("homeactivity","sign in anonymously:FAILURE"+e);

            }
        });

    }




}


