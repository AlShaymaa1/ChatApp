package com.chatapp.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.chatapp.Activities.Mess_Activity;
import com.chatapp.Adapters.NewsFeedAdapter;
import com.chatapp.Interface.OnHolderClicked;
import com.chatapp.Model.NewsFeedModel;
import com.chatapp.Model.UserModel;
import com.chatapp.R;
import com.chatapp.app.MyPrefrenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_news_feed extends Fragment  implements OnHolderClicked {
    private View layout_resourse;
    String picturepath;


    public fragment_news_feed() {
        // Required empty public constructor
    }
    EditText post_text;
    ImageView post_image_Upload,post_insert;
    ArrayList<NewsFeedModel> newsFeedModels;
    NewsFeedAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (layout_resourse == null){
            // Inflate the layout for this fragment
            layout_resourse  = inflater.inflate(R.layout.fragment_fragment_news_feed, container, false) ;
            post_text=(EditText)layout_resourse.findViewById(R.id.post_text);
            post_image_Upload=(ImageView) layout_resourse.findViewById(R.id.post_img_upload);
            post_insert=(ImageView) layout_resourse.findViewById(R.id.post_insert);
            post_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(picturepath!=null){
                        upload();
                    }
                }
            });
            post_image_Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pick_image_permission();
                }
            });

            // make ArrayList of custom model   have all your data
            // why custom model ? to make retrieving data and bind moreeasierr
             newsFeedModels = new ArrayList<>() ;


            // make object of your custom recycler view adapter
             adapter = new NewsFeedAdapter(newsFeedModels , getActivity(),this) ;

            // define your recycler view that exist in xml file
            RecyclerView newsList  = (RecyclerView) layout_resourse.findViewById(R.id.news_recycler);

            // you have to make one of those layout manger to inform your recycler view what style that will it go on

            // linear for list style
            LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL ,false ) ;

            // grid for columns  desplay like gallery
//           GridLayoutManager gridLayoutManager  = new GridLayoutManager(getContext() , 2 , LinearLayoutManager.HORIZONTAL , false) ;
            // staggered for grid display but  items not forced to be the same height in the same row
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL) ;


            // add your desired layout manger
            newsList.setLayoutManager(linearLayoutManager);

            // set your adapter into your news list
            newsList.setAdapter(adapter);
            get_posts();
            post_text.setText("");
        }

        // return your view that have fragment layout
        return layout_resourse;
    }


    void get_posts(){
        FirebaseDatabase database = FirebaseDatabase.getInstance() ;
        database.getReference("Posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewsFeedModel model = dataSnapshot.getValue(NewsFeedModel.class) ;
                newsFeedModels.add(model) ;

                adapter.notifyDataSetChanged();

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
        });
    }



    @Override
    public void onItemViewClicked() {
        Toast.makeText(getActivity() , "item view clicked" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageClicked(int pos, NewsFeedModel feedModel) {
        Toast.makeText(getActivity() , "image is clicked " + " pos:" +pos +
                        " and its tile was: " + feedModel.getTitle()

                , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMessageClicked(final NewsFeedModel feedModel) {

        final FirebaseDatabase  database  = FirebaseDatabase.getInstance() ;

        final DatabaseReference chat_ref = database.getReference("Chat") ;



        final String current_user_id = new MyPrefrenceManager(getContext()).getuser().getId() ;
        final String path1 =   current_user_id+"/"+feedModel.getUser_id() ;
        final  String path2 = feedModel.getUser_id()+"/"+ current_user_id  ;

        database.getReference("Chat/"+path1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String key ;
                if (dataSnapshot.exists())
                {
//                    for(DataSnapshot child : dataSnapshot.getChildren()){
//                        String key2 = child.getKey() ;
//                    }
                    key  = dataSnapshot.getChildren().iterator().next().getKey() ;


                }else  {
                    key  = chat_ref.push().getKey() ;
                    database.getReference("Chat/"+path1+"/"+key).setValue(true) ;
                    database.getReference("Chat/"+path2+"/"+key).setValue(true) ;

                }
                database.getReference("Users").child(feedModel.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModel userModel  = dataSnapshot.getValue(UserModel.class) ;


                        Intent intent = new Intent(getActivity() , Mess_Activity.class) ;
                        intent.putExtra("room_id" , key) ;
                        intent.putExtra("token" , userModel.getToken()) ;
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    void pick_image_permission(){
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},262);
        }else {
            pick_image();
        }

    }



    void pick_image(){

        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,300);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==262){
            //if request  is cancelled ,the reasult array are empty
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //permission was granted
                //do the contacts related task you need to do
                pick_image();
            }else{
                //permission denied
                //disable the functionality that depends on this permission
                Toast.makeText(getActivity(), "storage permission is needs to get into your image", Toast.LENGTH_SHORT).show();

            }return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==300&&resultCode==getActivity().RESULT_OK&&data!=null){
            Uri selectedImage =data.getData();
            String[] filpathcolumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getActivity().getContentResolver().query(selectedImage, filpathcolumn,null,null,null);
            cursor.moveToFirst();
            int columnindex =cursor.getColumnIndex(filpathcolumn[0]);
            picturepath=cursor.getString(columnindex);

        }
    }
    void upload(){
        File file =  new File(picturepath);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("images")
                .child(currentDateFormat()+"_"+file.getName());

        UploadTask uploadTask = riversRef.putFile(Uri.fromFile(file));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("onSuccess" , taskSnapshot.getDownloadUrl() +"") ;
                NewsFeedModel model = new NewsFeedModel(
                        post_text.getText().toString() ,
                        taskSnapshot.getDownloadUrl()+""
                        ,new MyPrefrenceManager(getContext()).getuser().getId()
                );
                FirebaseDatabase database = FirebaseDatabase.getInstance() ;
                database.getReference("Posts").push().setValue(model);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("onFailure" , e.toString() +"") ;

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("onProgress" , taskSnapshot.getBytesTransferred() +"") ;
            }
        });



    }



    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
}