package com.bkosarzycki.example.autocompleteexample.activity.details;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkosarzycki.example.autocompleteexample.AutoCompleteApp;
import com.bkosarzycki.example.autocompleteexample.R;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.item_main_item_thumbnail) ImageView mToolbarBackgroundImage;
    @Bind(R.id.activity_details_item_title) TextView mItemTitleTextView;
    @Inject Context mContext;

    Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        AutoCompleteApp.getApp(this).getDaggerMainComponent().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fillItemData(getIntent());
    }

    /**
     * Fills view with item data.
     *
     * @param intent
     */
    private void fillItemData(Intent intent) {
        if (intent != null) {
            String itemString = intent.getStringExtra("item");
            if (itemString != null && !itemString.isEmpty()) {
                mItem = new Gson().fromJson(itemString, Item.class);
                if (mItem != null) {
                    getSupportActionBar().setTitle("Item - " + mItem.getId());
                    mItemTitleTextView.setText(mItem.getTitle());
                    Glide.with(mContext).load(mItem.getUrl()).into(mToolbarBackgroundImage);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DetailsActivity.this.supportFinishAfterTransition();
    }

    /**
     * Starts the activity and handles activity transitions.
     *
     * @param activity
     * @param item
     * @param sharedImageView
     */
    public static void start(Activity activity, Item item, View sharedImageView) {
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
