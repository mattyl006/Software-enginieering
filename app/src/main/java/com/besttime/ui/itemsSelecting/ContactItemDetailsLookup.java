package com.besttime.ui.itemsSelecting;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.ui.adapters.viewHolders.ContactsViewHolder;

public class ContactItemDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public ContactItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if(view != null){
            RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(view);
            if(vh instanceof ContactsViewHolder){
                return ((ContactsViewHolder)vh).getItemDetails();
            }
        }

        return null;
    }
}
