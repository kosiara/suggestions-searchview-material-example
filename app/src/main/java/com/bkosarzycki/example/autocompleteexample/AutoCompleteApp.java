package com.bkosarzycki.example.autocompleteexample;

import android.app.Application;
import android.content.Context;

import com.bkosarzycki.example.autocompleteexample.inject.DaggerMainComponent;
import com.bkosarzycki.example.autocompleteexample.inject.MainComponent;
import com.bkosarzycki.example.autocompleteexample.inject.MainAppModule;

/**
 * Created by bkosarzycki on 12/12/15.
 */
public class AutoCompleteApp extends Application {

    MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMainComponent
                .builder()
                .mainAppModule(new MainAppModule(getApplicationContext()))
                .build();
    }

    public MainComponent getDaggerMainComponent() {
        return component;
    }

    public static AutoCompleteApp getApp(Context context) {
        return (AutoCompleteApp)context.getApplicationContext();
    }
}
