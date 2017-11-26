package com.chatapp.Interface;

import com.chatapp.Model.NewsFeedModel;


/**
 * Created by Asmaa on 9/9/2017.
 */

public interface OnHolderClicked {
    void onItemViewClicked();
    void onImageClicked(int pos, NewsFeedModel feedModel) ;
    void onMessageClicked(NewsFeedModel  feedModel);
}
