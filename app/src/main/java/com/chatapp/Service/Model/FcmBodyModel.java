package com.chatapp.Service.Model;

import com.chatapp.Model.MessModel;

import java.io.Serializable;

/**
 * Created by Asmaa on 9/20/2017.
 */

public class FcmBodyModel implements Serializable {

    String to ;
    FcmDataModel data ;

    public FcmBodyModel(String to, FcmDataModel data) {
        this.to = to;
        this.data = data;
    }

    public FcmBodyModel(String to, MessModel messModel) {

    }
}
