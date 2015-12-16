package com.bkosarzycki.example.autocompleteexample.activity;

import android.app.Instrumentation;
import android.support.v7.widget.SearchView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bkosarzycki.example.autocompleteexample.R;
import com.robotium.solo.Solo;

public class MainActivityTest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private Solo mSolo;
    private Instrumentation mInstrumentation;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private ImageView mSortSettingsImageView;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mInstrumentation = getInstrumentation();
        mSolo = new Solo(mInstrumentation, mMainActivity);
        mSearchView = (SearchView) mMainActivity.findViewById(R.id.autocomplete_searchview);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mMainActivity.findViewById(R.id.search_src_text);
        mSortSettingsImageView = (ImageView) mMainActivity.findViewById(R.id.sorting_settings_image_view);
    }

    public void testPreconditions() {
        assertEquals("SearchView not visible!", View.VISIBLE, mSearchView.getVisibility());
        assertEquals("SearchAutoComplete has text entered on app start", "", mSearchAutoComplete.getText().toString());
    }

    public void testLaunchAboutAndSettingsActivities() {

        mSolo.sendKey(Solo.MENU);
        mSolo.sendKey(KeyEvent.KEYCODE_MENU);
        mSolo.clickOnMenuItem("About");
        mSolo.sleep(1500);

        mSolo.assertCurrentActivity("About activity has not started", AboutActivity.class);

        mSolo.getCurrentActivity().finish();

        mSolo.sleep(2000);

        TouchUtils.clickView(this, mSortSettingsImageView);

        mSolo.sleep(1000);
        assertTrue("Could not find the dialog!", mSolo.searchText("Suggestions sorting"));
        mSolo.clickOnButton("OK");
        mSolo.sleep(500);
    }
}