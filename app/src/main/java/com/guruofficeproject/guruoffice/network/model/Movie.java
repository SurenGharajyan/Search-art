package com.guruofficeproject.guruoffice.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by USER on 24.02.2018.
 */

public class Movie extends RealmObject implements Serializable {
    @SerializedName("Title")
    String title;
    @SerializedName("Year")
    String year;
    @SerializedName("imdbID")
    String imdbID;
    @SerializedName("Type")
    String type;
    @SerializedName("Poster")
    String poster;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }
}
