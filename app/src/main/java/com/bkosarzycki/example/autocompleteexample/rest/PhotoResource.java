package com.bkosarzycki.example.autocompleteexample.rest;

import com.bkosarzycki.example.autocompleteexample.model.Item;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by bkosarzycki on 12/12/15.
 *
 * Retrofit REST service for: http://jsonplaceholder.typicode.com/photos
 */
public interface PhotoResource {

    @GET("photos")
    Call<List<Item>> getPhotos();
}
