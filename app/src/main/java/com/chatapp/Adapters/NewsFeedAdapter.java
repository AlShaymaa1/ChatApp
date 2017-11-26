package com.chatapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.chatapp.Interface.OnHolderClicked;
import com.chatapp.Model.NewsFeedModel;
import com.chatapp.R;

import java.util.ArrayList;

/**
 * Created by shaymaa on 8/28/2017.
 */


// steps to make adapter
    // 1- create empty constructor  or constructor  that will receive array list and activity object
    // 2 create view holder class (you will find view holder class in the bottom )
    // 3 - make  your adapter  extends RecyclerView.Adapter of your view holder class name
    // 4 - implement adapter methods by clicking on red lamp then implement methods


public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>  {


    // define an array list  of your custom data model to save your incoming data from outside of your adapter
    ArrayList<NewsFeedModel> data  ;
    // define activity object to use it in glide object and get layout inflater from it
    Activity activity ;
    OnHolderClicked holderListener ;
    public NewsFeedAdapter() {

    }

    // this constructor made for receiving data when you declare new object
    public NewsFeedAdapter(ArrayList<NewsFeedModel> data, Activity activity , OnHolderClicked holdeinterface ) {
        this.data = data;
        this.activity = activity;
        this.holderListener = holdeinterface ;
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /// 1-  this view holder make to rerun a view holder to recycler view adapter  that will be sent to on bind view holde
        /// 2- to make object from view holder class you will have to passing a view tha have your item layout
        // 3- to define your item layout into view you will need to inflate by layout infltaer

        // make layout inflater  by one of those methods
        LayoutInflater inflater = LayoutInflater.from(activity) ;
//        LayoutInflater inflater = activity.getLayoutInflater() ;
        //  inflate your layout into view by layout inflater
        View itemView  = inflater.inflate(R.layout.news_feed_item , parent , false) ;
        // finally u can make your object from your view holder
        NewsFeedViewHolder newsFeedViewHolder = new NewsFeedViewHolder(itemView) ;
        // dont forget to change nuull with view holder object
        return newsFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsFeedViewHolder holder, final int position) {


        // 1- in this interface you will get your data from your array list by position that u can get it from onbind view holder
        // 2- start to assign your data from model to your views
        // getting data by position
        final NewsFeedModel current  =  data.get(position) ;

        // set txt to tile
        holder.text.setText(current.getTitle());

        // to can load image from url , drawable object  or  drawable recourse  you will use library called glide
        Glide.with(activity)
                .load(current.getImg())
                .apply(new RequestOptions().circleCrop())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade())
                .thumbnail(0.5f)
                .into(holder.img) ;



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderListener.onItemViewClicked();
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderListener.onImageClicked(position , current);
            }
        });

        holder.chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderListener.onMessageClicked(current);
            }
        });

    }

    @Override
    public int getItemCount() {
        // return you items count
        return data.size();
    }


    // to create view holder class
     // 1- make class that extends RecyclerView.ViewHolder
    // 2- click on red lamp to create constructor matching super
    // 3 - declare  your views that will be change for every item  as global variables
    // 4 - in constructor define your view by find view by id but this time from itemview because it only can by use without view in activity
    public  class NewsFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView img  , chat_icon;
        TextView text ;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.feed_item_img);
            text = (TextView) itemView.findViewById(R.id.feed_item_title);
            chat_icon = (ImageView) itemView.findViewById(R.id.chat_icon);
        }
    }
}
