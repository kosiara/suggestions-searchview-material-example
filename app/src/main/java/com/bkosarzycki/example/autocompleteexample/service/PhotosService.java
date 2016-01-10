package com.bkosarzycki.example.autocompleteexample.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.rest.PhotoResource;
import com.google.common.base.Optional;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import dagger.Module;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by bkosarzycki on 12/12/15.
 */
@Module
public class PhotosService {

    private static final String TAG = PhotosService.class.getName();

    @Inject PhotoResource service;

    @Inject public PhotosService() {}

    public Optional<List<Item>> getPhotos() {
        try {
            return Optional.of(service.getPhotos().execute().body());
        } catch (Exception exc) {
            Log.e(TAG, "Error getting photos: " + exc.toString());
            return Optional.absent();
        }
    }

    public void getPhotosAsync(Callback<List<Item>> callback) {
        Call<List<Item>> call = service.getPhotos();
        call.enqueue(callback);
    }

}
