package com.bkosarzycki.example.autocompleteexample.activity.main;

import android.text.TextWatcher;
import android.widget.AdapterView;

import com.bkosarzycki.example.autocompleteexample.adapter.AutoSuggestionAdapter;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.google.common.base.Function;

import java.util.List;

/**
 * Created by bkosarzycki on 1/10/16.
 */
public interface MainActivityView {

    void runFabScaleAnim();
    void applyFilter(String filterString);
    void addAutoCompleteTextChangedListener(TextWatcher listener);
    void setOnDataLoadedListener(Function<List<Item> , Void> func);
    void setNoOfTappedAutosuggestionsText(String text);
    void loadLastTappedImage(String url);
    void setAutoSuggestionsData(AutoSuggestionAdapter adapter, AdapterView.OnItemClickListener clickListener);
}
