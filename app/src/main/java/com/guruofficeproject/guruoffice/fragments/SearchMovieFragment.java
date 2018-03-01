package com.guruofficeproject.guruoffice.fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.guruofficeproject.guruoffice.R;
import com.guruofficeproject.guruoffice.adapter.RecyclerMoviesAdapter;
import com.guruofficeproject.guruoffice.constants.Constants;
import com.guruofficeproject.guruoffice.dialog.DialogManager;
import com.guruofficeproject.guruoffice.network.RTService;
import com.guruofficeproject.guruoffice.network.ServiceGenerator;
import com.guruofficeproject.guruoffice.network.model.BasicDataResponse;
import com.guruofficeproject.guruoffice.network.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 27.02.2018.
 */

@SuppressLint("ValidFragment")
public class SearchMovieFragment extends Fragment {
    InputMethodManager imm;
    private String currentSearch;
    private boolean isTextDublicate;
    int y = 0;
    String oldText = "";
    private RTService rtService;
    private int page = 1;
    RecyclerView recyclerMovies;
    RecyclerMoviesAdapter adapterMovie;
    List<Movie> lMovies;
    SearchView searchMovie;
    LinearLayout lSearchViews;
    TextView txtHint;
    ObjectAnimator animateUp;
    Context context;
    SharedPreferences sharedPreferences;
    public SearchMovieFragment() {

    }

    @SuppressLint("ValidFragment")
    public SearchMovieFragment(Context context) {
        this.context = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        init(v);
        rtService = ServiceGenerator.createService(context, RTService.class);
        if (loadSearchLine() != null) {
            if (!loadSearchLine().equals("")) {
                searchMovie.setQuery(loadSearchLine(), true);
            Call<BasicDataResponse> call = rtService.getMoviesByTitle(Constants.API_KEY, searchMovie.getQuery().toString(), page);
            call.enqueue(new Callback<BasicDataResponse>() {
                @Override
                public void onResponse(Call<BasicDataResponse> call, Response<BasicDataResponse> response) {
                    if (response.body().hasResponse()) {
                        isTextDublicate = true;
                        lMovies = response.body().getResult();
                        adapterMovie = new RecyclerMoviesAdapter(lMovies, context, getActivity());
                        recyclerMovies.setAdapter(adapterMovie);
                    } else {
                        Toast.makeText(context, response.body().getError() + " yeees", Toast.LENGTH_SHORT).show();
                    }
                    DialogManager.getInstance().progressLoading(false, getActivity());
                }

                @Override
                public void onFailure(Call<BasicDataResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//        searchMovie.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ObjectAnimatorBinding")
//            @Override
//            public void onClick(View v) {
//                animateUp = ObjectAnimator.ofFloat(lSearchViews,"translateY",lSearchViews.getY(),0);
//                animateUp.setDuration(1000);
//                animateUp.start();
//            }
//        });
        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        searchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new Handler().postDelayed(() -> {
                    if (oldText.equals(newText)) {
                        DialogManager.getInstance().progressLoading(true, getActivity());
                    } else {
                        oldText = newText;
                    }
                }, 5000);
                currentSearch = newText;
                page = 1;
                txtHint.setVisibility(View.GONE);
                Call<BasicDataResponse> call = rtService.getMoviesByTitle(Constants.API_KEY, newText, page);
                call.enqueue(new Callback<BasicDataResponse>() {
                    @Override
                    public void onResponse(Call<BasicDataResponse> call, Response<BasicDataResponse> response) {

                        if (response.body().hasResponse()) {
                            isTextDublicate = true;
                            lMovies = response.body().getResult();
                            adapterMovie = new RecyclerMoviesAdapter(lMovies, context,getActivity());
                            recyclerMovies.setAdapter(adapterMovie);
                        } else {
                            if (!newText.isEmpty()) {
                                if (newText.charAt(newText.length() - 1) != ' ') {
                                    Toast.makeText(context, response.body().getError(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        DialogManager.getInstance().progressLoading(false,getActivity());
                    }

                    @Override
                    public void onFailure(Call<BasicDataResponse> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        DialogManager.getInstance().progressLoading(false,getActivity());
                    }
                });
                if (newText.isEmpty() && !(lMovies ==null)) {
                    lMovies.clear();
                    adapterMovie.notifyDataSetChanged();
                }
                return false;
            }
        });
        recyclerMovies.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {super.onScrolled(recyclerView, dx, dy);
                if(dy != y) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }


            }
        });
        recyclerMovies.setOnScrollChangeListener((View, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!recyclerMovies.canScrollVertically(1)) {

                DialogManager.getInstance().progressLoading(true,getActivity());

                Call<BasicDataResponse> call = rtService.getMoviesByTitle(Constants.API_KEY,currentSearch,++page);
                call.enqueue(new Callback<BasicDataResponse>() {
                    @Override
                    public void onResponse(Call<BasicDataResponse> call, Response<BasicDataResponse> response) {

                        if (response.body().hasResponse()) {
                            if (Integer.parseInt(response.body().getTotalResults())/10*page!=0) {
                                lMovies.addAll(response.body().getResult());
                                adapterMovie.notifyDataSetChanged();
                            }
                        }else {
                            if (currentSearch != null) {
                                if (!currentSearch.isEmpty()) {
                                    if (currentSearch.charAt(currentSearch.length() - 1) != ' ') {
                                        Toast.makeText(context, response.body().getError(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        DialogManager.getInstance().progressLoading(false, getActivity());
                    }

                    @Override
                    public void onFailure(Call<BasicDataResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        DialogManager.getInstance().progressLoading(false,getActivity());
                    }
                });

//                Log.i("page",page+"");
            }
        });
        return v;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveSearchLine(searchMovie.getQuery().toString());
    }

    private void init(View view) {
        recyclerMovies = view.findViewById(R.id.recycle_movies);
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        recyclerMovies.setLayoutManager(lManager);
        recyclerMovies.setHasFixedSize(true);
        searchMovie = view.findViewById(R.id.searching_film);
        lSearchViews = view.findViewById(R.id.searching_something);
        txtHint = view.findViewById(R.id.text_hint);
    }

    private void saveSearchLine(String savingSearch) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Constants.SHARED_PREFERENCES_SEARCH,savingSearch);
        edit.apply();
    }

    private String loadSearchLine() {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_PREFERENCES_SEARCH,"");
    }
}
