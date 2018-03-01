package com.guruofficeproject.guruoffice.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guruofficeproject.guruoffice.R;
import com.guruofficeproject.guruoffice.constants.Constants;
import com.guruofficeproject.guruoffice.network.RTService;
import com.guruofficeproject.guruoffice.network.ServiceGenerator;
import com.guruofficeproject.guruoffice.network.model.Movie;
import com.guruofficeproject.guruoffice.network.model.MovieForAlertDetails;
import com.guruofficeproject.guruoffice.realm.RealmManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 25.02.2018.
 */

public class RecyclerMoviesAdapter extends RecyclerView.Adapter<RecyclerMoviesAdapter.MovieHolder> {
    private List<Movie> moviesInfo;
    private Context context;
    private AlertDialog alertDialog;
    private View v;
    private RatingBar rateView;
    private TextView tvName, tvYear, tvType, tvTime, tvGenre, tvDirector, tvPlot, tvBoxOffice, tvProduction;
    private TextView[] massivTextView;
    private ImageView imgMovieAlert;
    private RTService rtService;
    private Snackbar snackbar;
    private ArrayList<String> creationInfo;
    private Activity activity;
    private Realm realm;
    boolean isFirstTime = true;

    public RecyclerMoviesAdapter(List<Movie> movieAbout, Context context, Activity activity) {
        moviesInfo = movieAbout;
        this.context = context;
        this.activity = activity;
        rtService = ServiceGenerator.createService(context, RTService.class);
        realm = RealmManager.getInstance().getDefaultInstance(activity);
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = moviesInfo.get(position);
        if (!movie.getPoster().equals("N/A")) {
            Picasso.with(context)
                    .load(movie.getPoster())
                    .into(holder.imgMovie);
        } else {
            holder.imgMovie.setImageResource(R.drawable.no_image_available);
        }
        holder.tvName.setText(movie.getTitle());
        holder.tvYear.setText(movie.getYear());
        holder.tvType.setText(movie.getType());
        if (realm.where(Movie.class).equalTo("imdbID", movie.getImdbID()).findFirst() != null) {
            holder.imgFavorite.setImageResource(R.drawable.ic_star_full);
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_star_empty);
        }
//        RealmResults<FavoriteModel> getStarRealm = realm.where(FavoriteModel.class).findAll();
//        if (!getStarRealm.isEmpty()) {
//            for (int j = 0; j < getStarRealm.size(); j++) {
//                if (moviesInfo.get(position).getImdbID().equals(getStarRealm.get(j).getIdMovie())) {
//                    holder.imgFavorite.setImageResource(R.drawable.ic_star_full);
//                }
//            }
//        }

//        Call<MovieForAlertDetails> movieDetailsCall = rtService.getMovieDetails(Constants.API_KEY,moviesInfo.get(position).getImdbID());
//        movieDetailsCall.enqueue(new Callback<MovieForAlertDetails>() {
//            @Override
//            public void onResponse(Call<MovieForAlertDetails> call, Response<MovieForAlertDetails> response) {
//                RealmResults<FavoriteModel> getStarRealm = realm.where(FavoriteModel.class).findAll();
//                if (response.isSuccessful()) {
//                    for (int i = 0; i <getStarRealm.size() ; i++) {
//                        if (response.body().getImdbID().equals(getStarRealm.get(i).getIdMovie())) {
//
//                        }
//                    }
//                 }
//            }
//
//            @Override
//            public void onFailure(Call<MovieForAlertDetails> call, Throwable t) {
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        realm.beginTransaction();
//
//        if (!getStarRealm.isEmpty()) {
//
//            holder.imgFavorite.setImageResource(getStarRealm.get(position).isStarActive() ? R.drawable.ic_star_full : R.drawable.ic_star_empty);
//        }
//        realm.commitTransaction();


        holder.imgFavorite.setOnClickListener(v1 -> {
//            holder.i++;
//            holder.imgFavorite.setImageResource(holder.i % 2 == 1 ? R.drawable.ic_star_full : R.drawable.ic_star_empty);

            //set or delete
            realm.beginTransaction();
            Movie favoriteMovie = realm.where(Movie.class).equalTo("imdbID", moviesInfo.get(position).getImdbID()).findFirst();
            if (favoriteMovie != null) {
                favoriteMovie.deleteFromRealm();
                holder.imgFavorite.setImageResource(R.drawable.ic_star_empty);
            } else {
                holder.imgFavorite.setImageResource(R.drawable.ic_star_full);
//                FavoriteModel addFavorite = realm.createObject(FavoriteModel.class);

//                addFavorite.setStarActive(holder.imgFavorite.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.ic_star_full).getConstantState());

//                FavoriteModel favoriteModel = new FavoriteModel(moviesInfo.get(position).getImdbID().toString(), holder.imgFavorite.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_star_full).getConstantState());
                realm.copyToRealm(moviesInfo.get(position));
//                addFavorite.setIdMovie(moviesInfo.get(position).getImdbID().toString());
                Log.i(Constants.MYTAG, "added:" + moviesInfo.get(position).getImdbID() + "\n");
            }

            realm.commitTransaction();
            //set



        });
        holder.btnMore.setOnClickListener((View v) -> {
            alertDialogRealize();
            Call<MovieForAlertDetails> movieDetailsCall = rtService.getMovieDetails(Constants.API_KEY, moviesInfo.get(position).getImdbID());
            movieDetailsCall.enqueue(new Callback<MovieForAlertDetails>() {
                @Override
                public void onResponse(Call<MovieForAlertDetails> call, Response<MovieForAlertDetails> response) {
                    if (response.isSuccessful()) {
                        MovieForAlertDetails lDetailsOfMovie = response.body();

                        tvName.setText(lDetailsOfMovie.getTitle());
                        tvYear.setText(lDetailsOfMovie.getYear());
                        tvType.setText(lDetailsOfMovie.getType());
                        tvTime.setText(lDetailsOfMovie.getTime());
                        tvGenre.setText(lDetailsOfMovie.getGenre());
                        tvDirector.setText(lDetailsOfMovie.getDirector());
                        tvPlot.setText(lDetailsOfMovie.getPlot());
                        tvBoxOffice.setText(lDetailsOfMovie.getBoxOffice());
                        tvProduction.setText(lDetailsOfMovie.getProduction());


                        rateView.setEnabled(false);
                        rateView.setRating(5 * formatRateStringToFloat(lDetailsOfMovie.getRate().get(0).getRatingValue()));
                        Log.i("formatted Number", formatRateStringToFloat(lDetailsOfMovie.getRate().get(0).getRatingValue()) + "");
                        Log.i("rateViewChange", rateView.getRating() + "");
                        massivTextView = new TextView[]
                                { tvName, tvYear, tvType, tvTime, tvGenre, tvDirector, tvPlot, tvBoxOffice, tvProduction } ;

                        for (TextView t : massivTextView) {
                            if (t.getText().equals("") || t.getText().equals("N/A")) {
                                t.setText("Nothing was found");
                            }
                        }
                        if (!lDetailsOfMovie.getPoster().equals("N/A")) {
                            Picasso.with(context)
                                    .load(lDetailsOfMovie.getPoster())
                                    .into(imgMovieAlert);
                        } else {
                            imgMovieAlert.setImageResource(R.drawable.no_image_available);
                        }

                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Go to Website", (dialog, which) -> {
                            snackbar = Snackbar
                                    .make(v, "This " + tvType.getText().toString() + " doesn't have any website", Snackbar.LENGTH_LONG);
                            if (lDetailsOfMovie.getWebsite() != null) {
                                if (!lDetailsOfMovie.getWebsite().equals("N/A")) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lDetailsOfMovie.getWebsite())));
                                } else {
                                    snackbar.show();
                                }
                            } else {
                                snackbar.show();
                            }
                        });
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<MovieForAlertDetails> call, Throwable t) {
                    Toast.makeText(context, "Something went wrong: " + t.getMessage() + ". Please contact the administrator.", Toast.LENGTH_SHORT).show();
                }

            });
        });
//        setAnimation(holder.itemView);
    }

//    private void setAnimation(View viewToAnimate) {
//        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
//        viewToAnimate.startAnimation(animation);
//    }

    public void alertDialogRealize() {
        alertDialog = new AlertDialog.Builder(activity).create();
        v = LayoutInflater.from(context).inflate(R.layout.alert_dialog_more, null);
        alertDialog.setView(v);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done", (dialog, which) -> {
        });
        init();

    }

    private void init() {
        tvName = v.findViewById(R.id.movie_is);
        tvYear = v.findViewById(R.id.movie_year_is);
        tvType = v.findViewById(R.id.movie_type_is);
        tvTime = v.findViewById(R.id.movie_time_is);
        tvGenre = v.findViewById(R.id.movie_genre_is);
        tvDirector = v.findViewById(R.id.movie_director_is);
        tvPlot = v.findViewById(R.id.movie_plot_is);
        tvBoxOffice = v.findViewById(R.id.movie_box_office_is);
        tvProduction = v.findViewById(R.id.movie_production_is);
        imgMovieAlert = v.findViewById(R.id.movie_img_alert);
        rateView = v.findViewById(R.id.rating_stars);

    }

    @Override
    public int getItemCount() {
        return moviesInfo.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvYear, tvType;
        ImageView imgMovie, imgFavorite;
        CardView cardView;
        Button btnMore;
        private int i = 0;

        public MovieHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.movie_is);
            tvYear = itemView.findViewById(R.id.movie_year_is);
            tvType = itemView.findViewById(R.id.movie_type_is);
            imgMovie = itemView.findViewById(R.id.movie_img);
            btnMore = itemView.findViewById(R.id.btn_more_info);
            cardView = itemView.findViewById(R.id.card_view_movie);
            imgFavorite = itemView.findViewById(R.id.favorite_star);
        }
    }

    private float formatRateStringToFloat(String detailsRate) {
        String[] parts = detailsRate.split("/");
        Log.i("part0", parts[0] + " part1=" + parts[1]);
        Float rateValue = Float.parseFloat(parts[0]) / Float.parseFloat(parts[1]);
        return rateValue;
    }
}