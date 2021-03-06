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
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 28.02.2018.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {
    private List<Movie> movieFavorite;
    private AlertDialog alertDialog;
    private Activity activity;
    View view;
    Context context;
    private TextView tvName,tvYear,tvType,tvTime,tvGenre,tvDirector,tvPlot,tvBoxOffice,tvProduction;
    private ImageView imgMovieAlert;
    private RatingBar rateView;
    private TextView[] massivTextView;
    RTService rtService;
    Snackbar snackbar;

    public FavoriteAdapter(Activity activity, Context context, List<Movie> moviesConstructor) {
        this.activity = activity;
        this.context = context;
        movieFavorite = moviesConstructor;
        rtService = ServiceGenerator.createService(context, RTService.class);
    }

    @Override
    public FavoriteAdapter.FavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,null);
        return new FavoriteHolder(v);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.FavoriteHolder holder, int position) {
        if (!movieFavorite.get(position).getPoster().equals("N/A")) {
            Picasso.with(context)
                    .load(movieFavorite.get(position).getPoster())
                    .into(holder.imgMovie);
        }else {
            holder.imgMovie.setImageResource(R.drawable.no_image_available);
        }
        holder.tvName.setText(movieFavorite.get(position).getTitle());
        holder.tvYear.setText(movieFavorite.get(position).getYear());
        holder.tvType.setText(movieFavorite.get(position).getType());

        holder.btnMore.setOnClickListener((View v) -> {
            alertDialogRealize();
            Call<MovieForAlertDetails> movieDetailsCall = rtService.getMovieDetails(Constants.API_KEY,movieFavorite.get(position).getImdbID());
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
                        Log.i("formatted Number",formatRateStringToFloat(lDetailsOfMovie.getRate().get(0).getRatingValue())+"");
                        Log.i("rateViewChange",rateView.getRating()+"");
                        massivTextView = new TextView[]{tvName, tvYear, tvType, tvTime, tvGenre, tvDirector, tvPlot, tvBoxOffice, tvProduction};

                        for (TextView t:massivTextView) {
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
                            if (lDetailsOfMovie.getWebsite()!=null) {
                                if (!lDetailsOfMovie.getWebsite().equals("N/A")) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lDetailsOfMovie.getWebsite())));
                                }else {
                                    snackbar.show();
                                }
                            }else {
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
    }


    @Override
    public int getItemCount() {
        return movieFavorite.size();
    }

    public void alertDialogRealize() {
        alertDialog = new AlertDialog.Builder(activity).create();
        view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_more,null);
        alertDialog.setView(view);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done", (dialog, which) -> {});
        init(view);

    }


    private void init(View v) {
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

    public class FavoriteHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvYear,tvType;
        ImageView imgMovie,imgFavorite;
        CardView cardView;
        Button btnMore;
        public FavoriteHolder(View itemView) {
            super(itemView);
            init();
        }
        private void init() {
            tvName = itemView.findViewById(R.id.movie_is);
            tvYear = itemView.findViewById(R.id.movie_year_is);
            tvType = itemView.findViewById(R.id.movie_type_is);
            imgMovie = itemView.findViewById(R.id.movie_img);
            btnMore = itemView.findViewById(R.id.btn_more_info);
            cardView = itemView.findViewById(R.id.card_view_movie);
            imgFavorite = itemView.findViewById(R.id.favorite_star);
            imgFavorite.setVisibility(View.GONE);
        }
    }

    private float formatRateStringToFloat(String detailsRate) {
        String[] parts = detailsRate.split("/");
        Log.i("part0",parts[0]+" part1=" +parts[1]);
        Float rateValue = Float.parseFloat(parts[0])/Float.parseFloat(parts[1]);
        return rateValue;
    }
}
