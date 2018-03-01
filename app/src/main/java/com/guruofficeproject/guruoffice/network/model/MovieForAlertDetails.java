package com.guruofficeproject.guruoffice.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 24.02.2018.
 */

public class MovieForAlertDetails implements Serializable {
    @SerializedName("Response")
    String response;

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

    @SerializedName("Error")
    String error;

    @SerializedName("Runtime")
    String time;

    @SerializedName("Genre")
    String genre;

    @SerializedName("Director")
    String director;

    @SerializedName("Plot")
    String plot;

    @SerializedName("BoxOffice")
    String boxOffice;

    @SerializedName("Production")
    String production;

    @SerializedName("Website")
    String website;

    @SerializedName("Ratings")
    List<Rate> rate;

    public List<Rate> getRate() {
        return rate;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

    public String getTime() {
        return time;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getProduction() {
        return production;
    }

    public String getWebsite() {
        return website;
    }

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

//    Ratings: [
//    {
//        Source: "Internet Movie Database",
//                Value: "7.7/10"
//    },
//    {
//        Source: "Rotten Tomatoes",
//                Value: "83%"
//    },
//    {
//        Source: "Metacritic",
//                Value: "67/100"
//    }
//],
}
