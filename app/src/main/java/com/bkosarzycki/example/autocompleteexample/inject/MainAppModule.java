package com.bkosarzycki.example.autocompleteexample.inject;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kosiara on 12/12/15.
 *
 * Dagger2 app main module
 */
@Module
public class MainAppModule {

    private final Context context;
    private String PREF_NAME = "prefs";

    public MainAppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context;
    }

    /**
     * Provides Android SharedPreferences for the entire app using Dagger2's injection.
     */
    @Singleton
    @Provides
    public SharedPreferences getAppPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
