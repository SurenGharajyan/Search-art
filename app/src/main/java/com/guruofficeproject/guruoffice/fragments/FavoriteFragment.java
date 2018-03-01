package com.guruofficeproject.guruoffice.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guruofficeproject.guruoffice.R;
import com.guruofficeproject.guruoffice.adapter.FavoriteAdapter;
import com.guruofficeproject.guruoffice.network.model.Movie;
import com.guruofficeproject.guruoffice.realm.RealmManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by USER on 27.02.2018.
 */

public class FavoriteFragment extends Fragment {
    private Context context;
    private Realm realm;
    private RecyclerView recyclerMovies;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorite_fragment, container, false);
        init(v);
        realm = RealmManager.getInstance().getDefaultInstance(getActivity());
        realm.beginTransaction();
        List<Movie> fMovies = new ArrayList<>();
        RealmResults<Movie> favMovies = realm.where(Movie.class).findAll();
        if (favMovies != null) {
            fMovies = realm.copyFromRealm(favMovies);
        }

        recyclerMovies.setAdapter(new FavoriteAdapter(getActivity(), getContext(), fMovies));
        realm.commitTransaction();
        return v;
    }


    public FavoriteFragment() {

    }

    @SuppressLint("ValidFragment")
    public FavoriteFragment(Context context) {
        this.context = context;

    }


    void init(View view) {
        recyclerMovies = view.findViewById(R.id.recycle_view_favorite);
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        recyclerMovies.setLayoutManager(lManager);
        recyclerMovies.setHasFixedSize(true);

    }
}
