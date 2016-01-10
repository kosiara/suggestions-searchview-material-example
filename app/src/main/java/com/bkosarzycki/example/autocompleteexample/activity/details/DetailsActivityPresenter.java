package com.bkosarzycki.example.autocompleteexample.activity.details;

import android.content.Intent;

/**
 * Created by bkosarzycki on 1/10/16.
 */
public interface DetailsActivityPresenter {

    void setView(DetailsActivityView activity);
    void fillItemData(Intent intent);
    void initializeToolbar();
}
