package com.bkosarzycki.example.autocompleteexample.activity.main;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.bkosarzycki.example.autocompleteexample.R;
import com.bkosarzycki.example.autocompleteexample.activity.details.DetailsActivity;
import com.bkosarzycki.example.autocompleteexample.adapter.AutoSuggestionAdapter;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.setting.SettingsDialogManager;
import com.google.common.base.Function;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by bkosarzycki on 1/10/16.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter {

    @Inject SettingsDialogManager mSettingsDialogManager;

    private MainActivityView mMainActivityView;

    @Inject public MainActivityPresenterImpl() {}

    @Override
    public void addAutoCompleteTextChangedListener() {
        mMainActivityView.addAutoCompleteTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        if (s.toString().isEmpty())
                            mMainActivityView.applyFilter(null);
                    }
                }
        );
    }

    @Override
    public void setView(MainActivity activity) {
        mMainActivityView = activity;
    }

    @Override
    public void setOnDataLoadedListener() {
        mMainActivityView.setOnDataLoadedListener(new Function<List<Item> , Void>() {
            @Override
            public Void apply(List<Item>  list) {
                mMainActivityView.runFabScaleAnim();
                mMainActivityView.applyFilter(null);
                loadSearchViewSuggestionsData(list);
                return null;
            }
        });
    }

    /**
     * Fill the upper-left corner toolbar data with total number of user taps
     * and last-tapped item image.
     */
    @Override
    public void fillLastTappedInfo() {
        int noOfTaps = mSettingsDialogManager.getNumberOfSuggestionTaps();
        String lastTappedUrl = mSettingsDialogManager.getLastTappedAutosuggestionUrl();

        mMainActivityView.setNoOfTappedAutosuggestionsText(new Integer(noOfTaps).toString());
        if (lastTappedUrl != null && !lastTappedUrl.isEmpty())
            mMainActivityView.loadLastTappedImage(lastTappedUrl);
    }

    @Override
    public void applySearchFilter(String filterString) {
        mMainActivityView.applyFilter(filterString.isEmpty() ? null : filterString);
    }

    @Override
    public void createSortingSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity)mMainActivityView, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Suggestions sorting");
        View settingsView = LayoutInflater.from((MainActivity)mMainActivityView).inflate(R.layout.settings_dialog_content, null);
        mSettingsDialogManager.setCurrentView(settingsView);
        builder.setView(settingsView);
        final AlertDialog mSortingSettingsDialog = builder.show();
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
     * Sets the last tapped Item which is later displayed in the Toolbar
     * @param item
     */
    public void setLastTappedInfo(Item item) {
        mSettingsDialogManager.increaseNumberOfSuggestionTaps();
        mSettingsDialogManager.setLastTappedAutosuggestionUrl(item.getUrl());

        mMainActivityView.loadLastTappedImage(item.getUrl());
        int noOfTaps = mSettingsDialogManager.getNumberOfSuggestionTaps();
        mMainActivityView.setNoOfTappedAutosuggestionsText(new Integer(noOfTaps).toString());
    }

    /**
     * Loads data for SearchView's suggestions
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadSearchViewSuggestionsData(List<Item> items) {

            final AutoSuggestionAdapter adapter = new AutoSuggestionAdapter<Item>((MainActivity)mMainActivityView, R.layout.item_suggestion_autocomplete, items);
            adapter.setSettingsDialogManager(mSettingsDialogManager);

            mMainActivityView.setAutoSuggestionsData(adapter,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Item item = (Item)adapter.getTypedItem(position);
                            setLastTappedInfo(item);
                            DetailsActivity.start((MainActivity)mMainActivityView, item, null);
                            return;
                        }
                    }
            );
    }
}
