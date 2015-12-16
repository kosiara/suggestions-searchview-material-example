package com.bkosarzycki.example.autocompleteexample.activity;

import android.support.v7.widget.SearchView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.bkosarzycki.example.autocompleteexample.R;

public class MainActivityTest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mSearchView = (SearchView) mMainActivity.findViewById(R.id.autocomplete_searchview);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mMainActivity.findViewById(R.id.search_src_text);
    }

    public void testPreconditions() {
        assertEquals("SearchView not visible!", View.VISIBLE, mSearchView.getVisibility());
        assertEquals("SearchAutoComplete has text entered on app start", "", mSearchAutoComplete.getText().toString());
    }
}