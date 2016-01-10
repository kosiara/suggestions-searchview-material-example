package com.bkosarzycki.example.autocompleteexample.activity.details;

import android.text.TextWatcher;
import android.widget.AdapterView;

import com.bkosarzycki.example.autocompleteexample.adapter.AutoSuggestionAdapter;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.google.common.base.Function;

import java.util.List;

/**
 * Created by bkosarzycki on 1/10/16.
 */
public interface DetailsActivityView {

    void loadToolbarBackgroundImage(String url);
    void setItemTitleText(String text);
    void setToolbarText(String text);
    void setToolbarDisplayAsHomeUp(boolean enabled);
}
