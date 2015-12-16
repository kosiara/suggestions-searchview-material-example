package com.bkosarzycki.example.autocompleteexample.setting;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bkosarzycki.example.autocompleteexample.R;

import javax.inject.Inject;

/**
 * Created by bkosarzycki on 12/15/15.
 *
 * Stores sorting preferences and fills SettingsDialog view.
 */
public class SettingsDialogManager {

    public enum SEARCH_TYPE { ORIGINAL, ORIGINAL_REVERSED, A_Z, Z_A }

    @Inject SharedPreferences mSharedPreferences;

    private View currentView;
    private final String CURRENT_SEARCH_TYPE_KEY = "current_search";
    private final String LAST_TAPPED_URL_KEY = "last_tapped_url";
    private final String NUMBER_OF_SUGGESTION_TAPS_KEY = "number_of_taps";

    @Inject public SettingsDialogManager() {}

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
        RadioGroup radioGroup = (RadioGroup)currentView.findViewById(R.id.settings_dialog_radio_group);
        ((RadioButton)radioGroup.getChildAt(getCurrentSearchTypeInt())).setChecked(true);
    }

    public SEARCH_TYPE getSearchTypeFromRadioButtons() {
        if (currentView != null) {
            RadioGroup radioGroup = (RadioGroup)currentView.findViewById(R.id.settings_dialog_radio_group);

            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(radioButtonID);
            int selection = radioGroup.indexOfChild(radioButton);
            return SEARCH_TYPE.values()[selection];
        }
        return SEARCH_TYPE.ORIGINAL;
    }

    public SEARCH_TYPE getCurrentSearchType() {
        int type = getCurrentSearchTypeInt();
        return SEARCH_TYPE.values()[type];
    }

    public int getCurrentSearchTypeInt() {
        int type = mSharedPreferences.getInt(CURRENT_SEARCH_TYPE_KEY, -1);
        return type == -1 ? 0 : type;
    }

    public void setCurrentSearchType(SEARCH_TYPE type) {
        int index = 0;
        for (int i = 0; i < SEARCH_TYPE.values().length; i++)
            if (type.equals(SEARCH_TYPE.values()[i]))
                index = i;

        mSharedPreferences.edit().putInt(CURRENT_SEARCH_TYPE_KEY, index).commit();
    }

    public void setLastTappedAutosuggestionUrl(String url) {
        mSharedPreferences.edit().putString(LAST_TAPPED_URL_KEY, url).commit();
    }

    public String getLastTappedAutosuggestionUrl() {
        return mSharedPreferences.getString(LAST_TAPPED_URL_KEY, "");
    }

    public int getNumberOfSuggestionTaps() {
        return mSharedPreferences.getInt(NUMBER_OF_SUGGESTION_TAPS_KEY, 0);
    }

    public void increaseNumberOfSuggestionTaps() {
        int currentNoOfTaps = getNumberOfSuggestionTaps();
        currentNoOfTaps++;
        mSharedPreferences.edit().putInt(NUMBER_OF_SUGGESTION_TAPS_KEY, currentNoOfTaps).commit();
    }
}
