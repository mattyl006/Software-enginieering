package com.besttime.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.models.Contact;
import com.besttime.ui.adapters.viewHolders.ContactsViewHolder;
import com.besttime.ui.animation.ContactSelectAnimationManager;
import com.example.besttime.R;

import java.util.ArrayList;
import java.util.List;


public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsViewHolder> implements Filterable {

    private ArrayList<Contact> contactsList;
    private ArrayList<Contact> contactsListFiltered;
    private SelectionTracker selectionTracker = null;
    private boolean doNothingOnItemStateChanged;
    private long previousSelectionKey;
    private ContactsViewHolder selectedViewHolder;

    private boolean clearingSelection = false;

    public void setAnimationManager(ContactSelectAnimationManager animationManager) {
        this.animationManager = animationManager;

    }

    private ContactSelectAnimationManager animationManager;

    public ContactsRecyclerViewAdapter(ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
        contactsListFiltered =  new ArrayList<>(contactsList);
        this.setHasStableIds(true);
    }

    public void setSelectionTracker(final SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
        selectionTracker.select((long)0);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
                if (!doNothingOnItemStateChanged && !clearingSelection) {
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

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.setContactName(contactsListFiltered.get(position).getName());
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
                    if(selectedViewHolder != null){
                        selectedViewHolder.setActive(false);
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
        return contactsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
                if(charString.isEmpty()){
                    contactsListFiltered.clear();
                    contactsListFiltered.addAll(contactsList);
                }
                else {
                    List<Contact> filteredList = new ArrayList<>();
                    for(Contact contact: contactsList){
                        if(contact.getName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(contact);
                        }
                    }

                    contactsListFiltered = (ArrayList<Contact>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactsListFiltered = (ArrayList<Contact>) filterResults.values;

                clearingSelection = true;

                selectionTracker.clearSelection();
                notifyDataSetChanged();

                clearingSelection = false;
                selectionTracker.select((long)0);
            }
        };
    }
}
