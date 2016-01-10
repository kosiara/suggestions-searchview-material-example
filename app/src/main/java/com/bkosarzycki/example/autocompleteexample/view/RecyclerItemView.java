package com.bkosarzycki.example.autocompleteexample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkosarzycki.example.autocompleteexample.R;
import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bumptech.glide.Glide;

/**
 * Created by bkosarzycki on 12/12/15.
 *
 * MVVM view for RecyclerView's item.
 */
public class RecyclerItemView extends FrameLayout {

    private Context mContext;
    private Item item;
    private ImageView thumbnailIV;

    public RecyclerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        removeAllViews();
        addView(LayoutInflater.from(context).inflate(R.layout.item_main_item_content, null, true));
    }

    public void bind(Item item, int position, OnClickListener clickListener) {
        this.item = item;

        TextView nameTextView = (TextView) this.findViewById(R.id.item_main_item_description);
        thumbnailIV = (ImageView) this.findViewById(R.id.item_main_item_thumbnail);


        nameTextView.setText(item.getTitle());

        Glide.with(mContext).load(item.getUrl()).into(thumbnailIV);

        if (clickListener != null)
            this.setOnClickListener(clickListener);
    }

    public Item getItem() {
        return item;
    }

    public ImageView getThumbnailImageView() {
        return thumbnailIV;
    }
}
