package com.guruofficeproject.guruoffice.realm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by USER on 28.02.2018.
 */

public class FavoriteModel extends RealmObject implements Serializable {
    @SerializedName("imdbID")
    String imdbID;

    boolean starIsActive;

    public FavoriteModel(String imdbID, boolean starIsActive) {
        this.imdbID = imdbID;
        this.starIsActive = starIsActive;
    }

    public FavoriteModel() {
    }

    public String getIdMovie() {
        return imdbID;
    }

    public void setIdMovie(String imdbID) {
        this.imdbID = imdbID;
    }

    public boolean isStarActive() {
        return starIsActive;
    }

    public void setStarActive(boolean starIsActive) {
        this.starIsActive = starIsActive;
    }
}
