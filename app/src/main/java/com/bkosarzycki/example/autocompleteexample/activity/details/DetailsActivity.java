package com.bkosarzycki.example.autocompleteexample.activity.details;

import android.app.Activity;
import android.content.Context;
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
import javax.inject.Inject;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements DetailsActivityView {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.item_main_item_thumbnail) ImageView mToolbarBackgroundImage;
    @Bind(R.id.activity_details_item_title) TextView mItemTitleTextView;
    @Inject Context mContext;
    @Inject DetailsActivityPresenterImpl mDetailsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        AutoCompleteApp.getApp(this).getDaggerMainComponent().inject(this);
        mDetailsActivityPresenter.setView(this);

        initializeToolbar();
        mDetailsActivityPresenter.fillItemData(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override public void loadToolbarBackgroundImage(String url) { Glide.with(this).load(url).into(mToolbarBackgroundImage); }

    @Override public void setItemTitleText(String text) { mItemTitleTextView.setText(text); }

    @Override public void setToolbarText(String text) { getSupportActionBar().setTitle(text); }

    @Override public void setToolbarDisplayAsHomeUp(boolean enabled) { getSupportActionBar().setDisplayHomeAsUpEnabled(enabled); }

    @Override public void onBackPressed() { DetailsActivity.this.supportFinishAfterTransition(); }

    private void initializeToolbar() {
        setSupportActionBar(mToolbar);
        mDetailsActivityPresenter.initializeToolbar();
    }

    public static void start(Activity activity, Item item, View sharedImageView) { DetailsActivityPresenterImpl.startActivity(activity, item, sharedImageView); }
}
