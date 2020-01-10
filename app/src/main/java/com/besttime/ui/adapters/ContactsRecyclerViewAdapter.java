package com.besttime.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.models.Contact;
import com.besttime.ui.adapters.viewHolders.ContactsViewHolder;
import com.besttime.ui.animation.ContactSelectAnimationManager;
import com.example.besttime.R;

import java.util.ArrayList;


public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsViewHolder> {

    private ArrayList<Contact> contactsList;
    private SelectionTracker selectionTracker = null;
    private boolean doNothingOnItemStateChanged;
    private long previousSelectionKey;
    private ContactsViewHolder selectedViewHolder;

    public void setAnimationManager(ContactSelectAnimationManager animationManager) {
        this.animationManager = animationManager;

    }

    private ContactSelectAnimationManager animationManager;

    public ContactsRecyclerViewAdapter(ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
        this.setHasStableIds(true);
    }

    public void setSelectionTracker(final SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
        selectionTracker.select((long)0);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
                if (!doNothingOnItemStateChanged) {
                    int numOfSelectedItems = selectionTracker.getSelection().size();
                    if (numOfSelectedItems == 1) {
                        previousSelectionKey = key;
                    } else if (numOfSelectedItems == 2) {
                        doNothingOnItemStateChanged = true;
                        selectionTracker.deselect(previousSelectionKey);
                        previousSelectionKey = key;
                        doNothingOnItemStateChanged = false;
                    } else if (numOfSelectedItems == 0) {
                        doNothingOnItemStateChanged = true;
                        selectionTracker.select(previousSelectionKey);
                        doNothingOnItemStateChanged = false;
                    }
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout viewHolderMainView = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item_view, parent, false);

        ContactsViewHolder viewHolder = new ContactsViewHolder(viewHolderMainView);

        viewHolder.setIsRecyclable(false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.setContactName(contactsList.get(position).getName());
        if(selectionTracker != null){
            boolean isActive = selectionTracker.isSelected((long)position);

            // Prevent selected view from being selected again (deselected, one view has always to be selected)
            if(isActive != holder.isActive()){
                if(isActive){
                    if(animationManager != null){
                        animationManager.PlaySelectAnimation(selectedViewHolder, holder);
                    }
                    else{
                        holder.setActive(isActive);
                    }
                    selectedViewHolder = holder;
                }
                else{
                    holder.setActive(isActive);
                }
            }
        }

        
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}
