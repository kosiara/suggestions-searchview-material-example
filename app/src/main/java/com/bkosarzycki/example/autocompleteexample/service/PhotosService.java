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

    private static final String PHOTOS_API_URL = "http://jsonplaceholder.typicode.com";
    private static final String TAG = PhotosService.class.getName();

    PhotoResource service;


    @Inject
    public PhotosService(Context context) {

        OkHttpClient okHttpClient = createOkHttpClientWithCache(context);
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(PHOTOS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PhotoResource.class);
    }

    private OkHttpClient createOkHttpClientWithCache(final Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "rest_responses");

        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.interceptors().add(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        String cacheHeaderValue = isOnline(context) ?
                                "public, max-age=2419200" : "public, only-if-cached, max-stale=2419200" ;

                        Request request = originalRequest.newBuilder().build();
                        Response response = chain.proceed(request);
                        return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue).build();
                    }
                }
        );
        okHttpClient.networkInterceptors().add(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        String cacheHeaderValue = isOnline(context) ?
                                "public, max-age=2419200" : "public, only-if-cached, max-stale=2419200" ;

                        Request request = originalRequest.newBuilder().build();
                        Response response = chain.proceed(request);
                        return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue).build();
                    }
                }
        );
        return okHttpClient;
    }

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

    private boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
