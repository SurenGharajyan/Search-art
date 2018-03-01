package com.guruofficeproject.guruoffice.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by USER on 26.02.2018.
 */

public class DialogManager {
    private static DialogManager _instance;
    ProgressDialog progressDialog;


    public static DialogManager getInstance() {
        if (_instance == null) {
            _instance = new DialogManager();
        }
        return _instance;
    }
    public void progressLoading(boolean show, Activity activity) {
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading data...");
        }
        if (show) {
            progressDialog.show();
        }else {
            progressDialog.hide();
        }
    }
}
