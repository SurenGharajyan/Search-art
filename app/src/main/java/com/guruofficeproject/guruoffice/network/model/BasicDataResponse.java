package com.guruofficeproject.guruoffice.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hayk on 31/01/2017.
 */

public class BasicDataResponse implements Serializable {
    @SerializedName("Response")
    @Expose
    protected String response;

    @SerializedName("Error")
    @Expose
    protected String error;

    @SerializedName("totalResults")
    @Expose
    protected String totalResults;

    @SerializedName("Search")
    @Expose
    protected List<Movie> result;

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public List<Movie> getResult() {
        return result;
    }

    public boolean hasResponse() {
        return response.equals("True");
    }
}
