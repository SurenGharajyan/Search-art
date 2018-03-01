package com.guruofficeproject.guruoffice.realm;

import android.content.Context;

import com.guruofficeproject.guruoffice.constants.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by USER on 01.03.2018.
 */

public class RealmManager {
    private RealmManager() {

    }

    private static RealmManager _instance;

    public static RealmManager getInstance() {
        if (_instance == null) {
            _instance = new RealmManager();
        }
        return _instance;
    }

    public void init(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Constants.REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public Realm getDefaultInstance(Context context) {
        try {
            Realm.getDefaultInstance();
        } catch (IllegalStateException e) {
            init(context);
        }
        return Realm.getDefaultInstance();
    }
}
