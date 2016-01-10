package com.bkosarzycki.example.autocompleteexample.activity.details;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by bkosarzycki on 1/10/16.
 */
public class DetailsActivityPresenterImpl implements DetailsActivityPresenter {

    private DetailsActivityView mDetailsActivityView;

    @Inject public DetailsActivityPresenterImpl() {}

    @Override
    public void setView(DetailsActivityView activity) {
        mDetailsActivityView = activity;
    }

    /**
     * Fills view with item data.
     *
     * @param intent
     */
    @Override
    public void fillItemData(Intent intent) {
        if (intent != null) {
            String itemString = intent.getStringExtra("item");
            if (itemString != null && !itemString.isEmpty()) {
                Item mItem = new Gson().fromJson(itemString, Item.class);
                if (mItem != null) {
                    mDetailsActivityView.setToolbarText("Item - " + mItem.getId());
                    mDetailsActivityView.setItemTitleText(mItem.getTitle());
                    mDetailsActivityView.loadToolbarBackgroundImage(mItem.getUrl());
                }
            }
        }
    }

    @Override
    public void initializeToolbar() {
        mDetailsActivityView.setToolbarText("");
        mDetailsActivityView.setToolbarDisplayAsHomeUp(true);
    }

    /**
     * Starts the activity and handles activity transitions.
     *
     * @param activity
     * @param item
     * @param sharedImageView
     */
    public static void startActivity(Activity activity, Item item, View sharedImageView) {
        Intent myIntent = new Intent(activity, DetailsActivity.class);
        myIntent.putExtra("item", new Gson().toJson(item));

        if (sharedImageView != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(myIntent,
                    ActivityOptions.makeSceneTransitionAnimation(activity, sharedImageView, "item_main_item_thumbnail_transition").toBundle());

        } else {
            activity.startActivity(myIntent);
        }
    }

}
