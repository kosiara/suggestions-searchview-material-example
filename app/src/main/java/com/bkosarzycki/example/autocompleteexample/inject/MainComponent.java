package com.bkosarzycki.example.autocompleteexample.inject;

import android.content.Context;
import android.content.SharedPreferences;

import com.bkosarzycki.example.autocompleteexample.activity.AboutActivity;
import com.bkosarzycki.example.autocompleteexample.activity.DetailsActivity;
import com.bkosarzycki.example.autocompleteexample.activity.main.MainActivity;
import com.bkosarzycki.example.autocompleteexample.fragment.MainContentFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by bkosarzycki on 12/12/15.
 *
 * Dagger 2 main app interface for injecting data and providing Context and UserSharedPreferences.
 *
 */
@Singleton
@Component(modules = {MainAppModule.class, NetworkModule.class})
public interface MainComponent {
    Context context();
    SharedPreferences sharedPreferences();
    void inject(MainActivity mainActivity);
    void inject(DetailsActivity detailsActivity);
    void inject(MainContentFragment mainContentFragment);
    void inject(AboutActivity aboutActivity);
}
