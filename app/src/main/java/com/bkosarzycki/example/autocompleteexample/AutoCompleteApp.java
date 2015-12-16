package com.bkosarzycki.example.autocompleteexample;

import android.app.Application;
import android.content.Context;

import com.bkosarzycki.example.autocompleteexample.inject.DaggerServicesComponent;
import com.bkosarzycki.example.autocompleteexample.inject.ServicesComponent;
import com.bkosarzycki.example.autocompleteexample.inject.ServicesModule;

/**
 * Created by bkosarzycki on 12/12/15.
 */
public class AutoCompleteApp extends Application {

    ServicesComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerServicesComponent
                .builder()
                .servicesModule(new ServicesModule(getApplicationContext()))
                .build();
    }

    public ServicesComponent getServicesComponent() {
        return component;
    }

    public static AutoCompleteApp getApp(Context context) {
        return (AutoCompleteApp)context.getApplicationContext();
    }
}
