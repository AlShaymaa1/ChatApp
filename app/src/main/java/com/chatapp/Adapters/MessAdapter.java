package com.chatapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chatapp.Model.MessModel;
import com.chatapp.R;
import com.chatapp.app.MyPrefrenceManager;

import java.util.ArrayList;

/**
 * Created by shaymaa on 8/28/2017.
 */


// steps to make adapter
    // 1- create empty constructor  or constructor  that will receive array list and activity object
    // 2 create view holder class (you will find view holder class in the bottom )
    // 3 - make  your adapter  extends RecyclerView.Adapter of your view holder class name
    // 4 - implement adapter methods by clicking on red lamp then implement methods


public class MessAdapter extends RecyclerView.Adapter<MessAdapter.MessagesViewHolder>  {


    // define an array list  of your custom data model to save your incoming data from outside of your adapter
    ArrayList<MessModel> data  ;
    // define activity object to use it in glide object and get layout inflater from it
    Activity activity ;

    public MessAdapter() {

    }

    // this constructor made for receiving data when you declare new object
    public MessAdapter(ArrayList<MessModel> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /// 1-  this view holder make to rerun a view holder to recycler view adapter  that will be sent to on bind view holde
        /// 2- to make object from view holder class you will have to passing a view tha have your item layout
        // 3- to define your item layout into view you will need to inflate by layout infltaer

        // make layout inflater  by one of those methods
        LayoutInflater inflater = LayoutInflater.from(activity) ;
//        LayoutInflater inflater = activity.getLayoutInflater() ;
        //  inflate your layout into view by layout inflater
        View itemView  = inflater.inflate(R.layout.mess_item , parent , false) ;
        // finally u can make your object from your view holder
        MessagesViewHolder newsFeedViewHolder = new MessagesViewHolder(itemView) ;
        // dont forget to change nuull with view holder object
        return newsFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, final int position) {

        MessModel current  = data.get(position) ;
        holder.text.setText(current.getMessage());
        String my_id = new MyPrefrenceManager(activity).getuser().getId() ;
        if (my_id.equals(current.getUser_id())){
            holder.message_container.setGravity(Gravity.END);
        }else {
            holder.message_container.setGravity(Gravity.START);
        }



    }


    @Override
    public int getItemCount() {
        // return you items count
        return data.size();
    }


    MessModel get_item (int postion){
        int new_pos = data.size() - postion - 1 ;
        return  data.get(new_pos) ;
    }

    public  void insert_data( MessModel messModel){
        data.add(0 , messModel);
        notifyDataSetChanged();
    }


    // to create view holder class
     // 1- make class that extends RecyclerView.ViewHolder
    // 2- click on red lamp to create constructor matching super
    // 3 - declare  your views that will be change for every item  as global variables
    // 4 - in constructor define your view by find view by id but this time from itemview because it only can by use without view in activity
    public  class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView text ;
        LinearLayout message_container  ;
        public MessagesViewHolder(View itemView) {
            super(itemView);
            message_container = (LinearLayout) itemView.findViewById(R.id.mess_container) ;
            text = (TextView) itemView.findViewById(R.id.mess_item_text);
        }
    }
}
