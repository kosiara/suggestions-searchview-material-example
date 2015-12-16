package com.bkosarzycki.example.autocompleteexample.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bkosarzycki on 12/12/15.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}
