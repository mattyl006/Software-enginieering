package com.besttime.ui.itemsSelecting;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class ContactItemDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public ContactItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        return null;
    }
}
