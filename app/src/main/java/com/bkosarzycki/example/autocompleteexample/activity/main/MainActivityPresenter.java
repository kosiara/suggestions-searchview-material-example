package com.bkosarzycki.example.autocompleteexample.activity.main;

/**
 * Created by bkosarzycki on 1/10/16.
 */
public interface MainActivityPresenter {
    void addAutoCompleteTextChangedListener();
    void setView(MainActivity mainActivity);
    void setOnDataLoadedListener();
    void fillLastTappedInfo();
    void applySearchFilter(String filterString);
    void createSortingSettingsDialog();
}
