package com.bkosarzycki.example.autocompleteexample.activity;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkosarzycki.example.autocompleteexample.AutoCompleteApp;
import com.bkosarzycki.example.autocompleteexample.R;
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

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.autocomplete_searchview) SearchView mSearchView;
    @Bind(R.id.search_src_text) SearchView.SearchAutoComplete mSearchAutoComplete;
    @Bind(R.id.activity_main_no_of_tapped_autosuggestions) TextView mNoOfTappedAutosuggestionsTextView;
    @Bind(R.id.activity_main_last_tapped_image_view) ImageView mLastTappedImageView;
    @Bind(R.id.fab) FloatingActionButton mSearchFab;

    @Inject SettingsDialogManager mSettingsDialogManager;
    @Inject MainContentFragment mMainContentFragment;

    AutoSuggestionAdapter mAutoSuggestionAdapter;
    AlertDialog mSortingSettingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AutoCompleteApp.getApp(this).getDaggerMainComponent().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        if (findViewById(R.id.fragment_main_container) != null) {
            if (savedInstanceState != null)
                return;

            mMainContentFragment.setOnDataLoadedListener(new Function<List<Item> , Void>() {
                @Override
                public Void apply(List<Item>  list) {
                    runFabScaleAnim();
                    mMainContentFragment.applyFilter(null);
                    loadSearchViewSuggestionsData(list);
                    return null;
                }
            });
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_container, mMainContentFragment).commit();
        }

        mSearchAutoComplete.setDropDownHeight(getResources().getDisplayMetrics().heightPixels/3);
        mSearchAutoComplete.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        mSearchAutoComplete.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        if (s.toString().isEmpty())
                            mMainContentFragment.applyFilter(null);
                    }
                }
        );

        fillLastTappedInfo();
    }

    /**
     * Animates FAB after successful data download.
     */
    private void runFabScaleAnim() {
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

    /**
     * Fill the upper-left corner toolbar data with total number of user taps
     * and last-tapped item image.
     */
    private void fillLastTappedInfo() {
        int noOfTaps = mSettingsDialogManager.getNumberOfSuggestionTaps();
        String lastTappedUrl = mSettingsDialogManager.getLastTappedAutosuggestionUrl();

        mNoOfTappedAutosuggestionsTextView.setText(new Integer(noOfTaps).toString());
        if (lastTappedUrl != null && !lastTappedUrl.isEmpty())
            Glide.with(this).load(lastTappedUrl).into(mLastTappedImageView);
    }

    @OnClick(R.id.fab)
    public void searchFabClick(View view) {
        String filterString = mSearchAutoComplete.getText().toString().trim();
        mMainContentFragment.applyFilter(filterString.isEmpty() ? null : filterString);
    }

    @OnClick(R.id.sorting_settings_image_view)
    public void sortingSettingsClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Suggestions sorting");
        View settingsView = LayoutInflater.from(this).inflate(R.layout.settings_dialog_content, null);
        mSettingsDialogManager.setCurrentView(settingsView);
        builder.setView(settingsView);
        mSortingSettingsDialog = builder.show();
        Button okBtn = (Button) mSortingSettingsDialog.findViewById(R.id.settings_dialog_ok_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSortingSettingsDialog != null) {
                    mSettingsDialogManager.setCurrentSearchType(
                            mSettingsDialogManager.getSearchTypeFromRadioButtons()
                    );
                    mSortingSettingsDialog.dismiss();
                }
            }
        });
    }

    /**
     * Loads data for SearchView's suggestions
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadSearchViewSuggestionsData(List<Item> items) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchView.SearchAutoComplete searchSrcTextView = (SearchView.SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchSrcTextView.setThreshold(1);
            searchSrcTextView.setText("");
            final AutoSuggestionAdapter adapter = new AutoSuggestionAdapter<Item>(this, R.layout.item_suggestion_autocomplete, items);
            adapter.setSettingsDialogManager(mSettingsDialogManager);
            searchSrcTextView.setAdapter(adapter);
            searchSrcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Item item = (Item)adapter.getTypedItem(position);
                    setLastTappedInfo(item);
                    DetailsActivity.start(MainActivity.this, item, null);
                    return;
                }
            });
        }
    }

    /**
     * Sets the last tapped Item which is later displayed in the Toolbar
     * @param item
     */
    public void setLastTappedInfo(Item item) {
        mSettingsDialogManager.increaseNumberOfSuggestionTaps();
        mSettingsDialogManager.setLastTappedAutosuggestionUrl(item.getUrl());

        Glide.with(this).load(item.getUrl()).into(mLastTappedImageView);
        int noOfTaps = mSettingsDialogManager.getNumberOfSuggestionTaps();
        mNoOfTappedAutosuggestionsTextView.setText(new Integer(noOfTaps).toString());
    }

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
}
