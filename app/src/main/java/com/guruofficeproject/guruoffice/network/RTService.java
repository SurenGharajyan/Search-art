package com.guruofficeproject.guruoffice.network;

import com.guruofficeproject.guruoffice.network.model.BasicDataResponse;
import com.guruofficeproject.guruoffice.network.model.MovieForAlertDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Hayk on 29/12/2017.
 */

public interface RTService {
    @GET("/")
    Call<BasicDataResponse> getMoviesByTitle(@Query("apikey") String apiKey, @Query("s") String title, @Query("page") int page);

    @GET("/")
    Call<MovieForAlertDetails> getMovieDetails(@Query("apikey") String apiKey, @Query("i") String idCode);

    @GET("/")
    Call<MovieForAlertDetails> getMoviesById(@Query("apikey") String apiKey, @Query("i") String idCode);

}
