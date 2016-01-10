package com.bkosarzycki.example.autocompleteexample.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.bkosarzycki.example.autocompleteexample.search.SearchFilter;
import com.bkosarzycki.example.autocompleteexample.setting.SettingsDialogManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.util.Collections;
import java.util.List;

/**
 * Created by bkosarzycki on 14.12.15.
 *
 * Provides data for SearchView's auto-suggestions.
 */
public class AutoSuggestionAdapter<T> extends ArrayAdapter<String> {

    SettingsDialogManager mSettingsDialogManager;

    private List<T> items;
    private List<T> filteredItems;
    private ArrayFilter mFilter;

    public AutoSuggestionAdapter(Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, Lists.<String>newArrayList());
        this.items = objects;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {

        T item = filteredItems.get(position);
        if (item instanceof String)
            return (String)item;
        else if (item instanceof Item)
            return ((Item) item).getTitle();

        return null;
    }

    public T getTypedItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    public int getCount() {
        return filteredItems.size();
    }

    public void setSettingsDialogManager(SettingsDialogManager settingsDialogManager) {
        mSettingsDialogManager = settingsDialogManager;
    }

    /**
     * Filters and sorts data. User can select sorting type in preferences.
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            List<T> toSort = new SearchFilter<T>().filter(prefix, items);

            if (mSettingsDialogManager.getCurrentSearchType() != SettingsDialogManager.SEARCH_TYPE.ORIGINAL)
                if (mSettingsDialogManager.getCurrentSearchType().equals(SettingsDialogManager.SEARCH_TYPE.ORIGINAL_REVERSED)) {
                    toSort = Lists.reverse(toSort);
                } else {
                    Collections.sort(toSort, new Ordering<T>() {
                        @Override
                        public int compare(T left, T right) {
                            if (left instanceof String)
                                return ((String) left).compareTo((String) right);
                            else if (left instanceof Item)
                                return ((Item) left).getTitle().compareTo(((Item) right).getTitle());
                            return 0;
                        }
                    });

                    if (mSettingsDialogManager.getCurrentSearchType().equals(SettingsDialogManager.SEARCH_TYPE.Z_A))
                        toSort = Lists.reverse(toSort);
                }
            filteredItems = toSort;

            results.values = filteredItems;
            results.count = filteredItems.size();

            return results;
        }

        @Override
        protected void publishResults(final CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
