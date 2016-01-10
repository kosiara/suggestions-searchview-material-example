package com.bkosarzycki.example.autocompleteexample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bkosarzycki.example.autocompleteexample.AutoCompleteApp;
import com.bkosarzycki.example.autocompleteexample.R;
import com.bkosarzycki.example.autocompleteexample.adapter.ItemsRecyclerViewAdapter;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.service.PhotosService;
import com.google.common.base.Function;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by bkosarzycki on 12/12/15.
 */
public class MainContentFragment extends Fragment {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefresh;

    @Inject PhotosService mPhotosService;
    @Inject ItemsRecyclerViewAdapter mAdapter;

    Function<List<Item>, Void> mDataLoadedFunc;

    @Inject public MainContentFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoCompleteApp.getApp(this.getActivity()).getDaggerMainComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        afterViews();
        return view;
    }

    public void setOnDataLoadedListener(Function<List<Item>,Void> function) {
        mDataLoadedFunc = function;
    }

    private void afterViews() {
        setRefreshing(true);
        initializeRecyclerView();
        loadData();

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                redownloadData();
            }
        });
    }

    public void redownloadData() {
        setRefreshing(true);
        loadData();
    }

    private void initializeRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter.setMainActivity(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }


    private void loadData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPhotosService.getPhotosAsync(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Response<List<Item>> response, Retrofit retrofit) {
                        setRefreshing(false);
                        List<Item> list = response.body();
                        mAdapter.addItems(list);
                        if (mDataLoadedFunc != null)
                            mDataLoadedFunc.apply(list);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        setRefreshing(false);
                        View view = getView();
                        if (view != null)
                            Snackbar.make(view, "Unable to connect", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                    }
                });
            }
        });
    }

    private void setRefreshing(final boolean val) {
        mSwipeRefresh.post(new Runnable() {
            @Override public void run() { mSwipeRefresh.setRefreshing(val); }
        });
    }

    public void applyFilter(String filterString) {
        mAdapter.setFilterPattern(filterString);
        mAdapter.notifyDataSetChanged();
    }
}
