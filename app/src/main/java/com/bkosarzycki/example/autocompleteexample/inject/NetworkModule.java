package com.bkosarzycki.example.autocompleteexample.inject;

import android.content.Context;

import com.bkosarzycki.example.autocompleteexample.rest.PhotoResource;
import com.bkosarzycki.example.autocompleteexample.util.AndroidUtil;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by bkosarzycki on 1/9/16.
 *
 * Dagger2 network module
 */
@Module
public class NetworkModule {

    private static final String PHOTOS_API_URL = "http://jsonplaceholder.typicode.com";

    @Provides @Singleton
    PhotoResource providePhotoResouce(Retrofit retrofit) {
        return retrofit.create(PhotoResource.class);
    }

    @Provides @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(PHOTOS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient(Context context, Cache cache) {
        OkHttpClient okHttpClient = createOkHttpClientWithCache(context, cache);
        return okHttpClient;
    }

    @Provides @Singleton
    Cache provideOkHttpCache(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "rest_responses");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        return cache;
    }

    private OkHttpClient createOkHttpClientWithCache(final Context context, Cache cache) {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.interceptors().add(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        String cacheHeaderValue = AndroidUtil.isOnline(context) ?
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

                        String cacheHeaderValue = AndroidUtil.isOnline(context) ?
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
}
