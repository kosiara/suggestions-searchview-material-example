package com.bkosarzycki.example.autocompleteexample.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bkosarzycki.example.autocompleteexample.activity.DetailsActivity;
import com.bkosarzycki.example.autocompleteexample.activity.main.MainActivity;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.search.SearchFilter;
import com.bkosarzycki.example.autocompleteexample.view.RecyclerItemView;
import com.bkosarzycki.example.autocompleteexample.viewmodel.RecyclerViewAdapterBase;
import com.bkosarzycki.example.autocompleteexample.viewmodel.ViewWrapper;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by bkosarzycki on 12/12/15.
 *
 * Provides data for MainActivity's RecyclerView
 */
public class ItemsRecyclerViewAdapter extends RecyclerViewAdapterBase<Item, RecyclerItemView>
            implements AdapterView.OnClickListener {

    @Inject Context mContext;
    private Activity mMainActivity;
    private String mCurrentFilterPattern;
    private List<Item> mFilteredItems;

    @Inject public ItemsRecyclerViewAdapter() {}

    @Override
    public void onBindViewHolder(ViewWrapper<RecyclerItemView> viewHolder, int position) {
        RecyclerItemView view = viewHolder.getView();

        List<Item> currentItems = (mCurrentFilterPattern == null || mCurrentFilterPattern.isEmpty())
                ? items : mFilteredItems;

        final Item msg = currentItems.get(position);
        view.bind(msg, position, this);
    }

    @Override
    public void onClick(View v) {
        RecyclerItemView view = (RecyclerItemView)v;
        Item item = view.getItem();
        ((MainActivity)mMainActivity).setLastTappedInfo(item);
        DetailsActivity.start(mMainActivity, item, view.getThumbnailImageView());
    }

    @Override
    protected RecyclerItemView onCreateItemView(ViewGroup parent, int viewType) {
        RecyclerItemView v = new RecyclerItemView(parent.getContext(), null);
        return v;
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (observer != null)
            super.unregisterAdapterDataObserver(observer);
    }

    public void clearAll() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Item> itemsToAdd) {
        if (itemsToAdd != null)
            items.addAll(itemsToAdd);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCurrentFilterPattern == null || mCurrentFilterPattern.isEmpty())
            return super.getItemCount();
        else
            return mFilteredItems.size();
    }

    public void setMainActivity(Activity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public void setFilterPattern(String filterString) {
        mCurrentFilterPattern = filterString;
        mFilteredItems = new SearchFilter<Item>().filter(mCurrentFilterPattern, items);
    }
}


