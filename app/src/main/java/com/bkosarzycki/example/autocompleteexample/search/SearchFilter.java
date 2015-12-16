package com.bkosarzycki.example.autocompleteexample.search;

import com.bkosarzycki.example.autocompleteexample.model.Item;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by bkosarzycki on 12/16/15.
 */
public class SearchFilter<T> {

    public List<T> filter(final CharSequence constraint, List<T> list) {
        return Lists.newArrayList(Iterables.filter(list, new Predicate<T>() {
            @Override
            public boolean apply(T input) {
                if (constraint == null || constraint.toString().isEmpty())
                    return false;
                if (input instanceof String) {
                    if (((String) input).contains(constraint))
                        return true;
                } else if (input instanceof Item) {
                    if (((Item) input).getTitle().contains(constraint))
                        return true;
                }
                return false;
            }
        }));
    }
}
