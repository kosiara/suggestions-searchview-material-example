package com.bkosarzycki.example.autocompleteexample.service;

import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.rest.PhotoResource;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import retrofit.Call;
import retrofit.Callback;

/**
 * Created by bkosarzycki on 12/12/15.
 */
@Module
public class PhotosService {

    private static final String TAG = PhotosService.class.getName();

    @Inject PhotoResource service;

    @Inject public PhotosService() {}

    public void getPhotosAsync(Callback<List<Item>> callback) {
        Call<List<Item>> call = service.getPhotos();
        call.enqueue(callback);
    }

}
