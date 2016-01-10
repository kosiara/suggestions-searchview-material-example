package com.bkosarzycki.example.autocompleteexample.activity.main;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkosarzycki.example.autocompleteexample.AutoCompleteApp;
import com.bkosarzycki.example.autocompleteexample.R;
import com.bkosarzycki.example.autocompleteexample.activity.AboutActivity;
import com.bkosarzycki.example.autocompleteexample.activity.DetailsActivity;
import com.bkosarzycki.example.autocompleteexample.adapter.AutoSuggestionAdapter;
import com.bkosarzycki.example.autocompleteexample.fragment.MainContentFragment;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.setting.SettingsDialogManager;
import com.bumptech.glide.Glide;
import com.google.common.base.Function;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.autocomplete_searchview) SearchView mSearchView;
    @Bind(R.id.search_src_text) SearchView.SearchAutoComplete mSearchAutoComplete;
    @Bind(R.id.activity_main_no_of_tapped_autosuggestions) TextView mNoOfTappedAutosuggestionsTextView;
    @Bind(R.id.activity_main_last_tapped_image_view) ImageView mLastTappedImageView;
    @Bind(R.id.fab) FloatingActionButton mSearchFab;

    @Inject MainContentFragment mMainContentFragment;
    @Inject MainActivityPresenterImpl mMainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AutoCompleteApp.getApp(this).getDaggerMainComponent().inject(this);
        mMainActivityPresenter.setView(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        if (findViewById(R.id.fragment_main_container) != null) {
            if (savedInstanceState != null)
                return;

            mMainActivityPresenter.setOnDataLoadedListener();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_container, mMainContentFragment).commit();
        }

        mMainActivityPresenter.addAutoCompleteTextChangedListener();
        mMainActivityPresenter.fillLastTappedInfo();
    }

    @Override public void applyFilter(String filterString) { mMainContentFragment.applyFilter(filterString); }

    @Override
    public void addAutoCompleteTextChangedListener(TextWatcher listener) {
        mSearchAutoComplete.setDropDownHeight(getResources().getDisplayMetrics().heightPixels/3);
        mSearchAutoComplete.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mSearchAutoComplete.addTextChangedListener(listener);
    }

    @Override public void setOnDataLoadedListener(Function<List<Item>, Void> func) { mMainContentFragment.setOnDataLoadedListener(func); }

    @Override public void setNoOfTappedAutosuggestionsText(String text) { mNoOfTappedAutosuggestionsTextView.setText(text); }

    @Override public void loadLastTappedImage(String url) { Glide.with(this).load(url).into(mLastTappedImageView); }

    @Override
    public void setAutoSuggestionsData(AutoSuggestionAdapter adapter, AdapterView.OnItemClickListener clickListener) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchView.SearchAutoComplete searchSrcTextView = (SearchView.SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchSrcTextView.setThreshold(1);
            searchSrcTextView.setText("");
            searchSrcTextView.setAdapter(adapter);
            searchSrcTextView.setOnItemClickListener(clickListener);
        }
    }

    @OnClick(R.id.fab)
    public void searchFabClick(View view) {
        String filterString = mSearchAutoComplete.getText().toString().trim();
        mMainActivityPresenter.applySearchFilter(filterString);
    }

    @OnClick(R.id.sorting_settings_image_view)
    public void sortingSettingsClick(View view) {
        mMainActivityPresenter.createSortingSettingsDialog();
    }

    public MainActivityPresenterImpl getPresenter() { return mMainActivityPresenter; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AboutActivity.start(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Animates FAB after successful data download.
     */
    @Override
    public void runFabScaleAnim() {
        float animScale = 1.25f;
        mSearchFab.animate()
                .scaleX(animScale)
                .scaleY(animScale)
                .setDuration(100)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mSearchFab.animate().scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100).start();
                    }
                }).start();
    }
}
