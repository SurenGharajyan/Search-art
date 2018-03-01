package com.guruofficeproject.guruoffice.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by USER on 26.02.2018.
 */

public class Rate implements Serializable {
    @SerializedName("Source")
    String whoRate;

    @SerializedName("Value")
    String ratingValue;


    public String getWhoRate() {
        return whoRate;
    }

    public String getRatingValue() {
        return ratingValue;
    }
}
