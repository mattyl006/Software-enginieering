package com.besttime.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.app.helpers.WhatsappCallPerformable;
import com.besttime.ui.adapters.viewHolders.ContactsViewHolder;
import com.besttime.ui.animation.ContactSelectAnimationManager;
import com.besttime.ui.utils.ContactSelectionListenable;
import com.besttime.ui.viewModels.ContactEntryWithWhatsappId;
import com.example.besttime.R;

import java.util.ArrayList;


public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsViewHolder> implements Filterable {

    private ArrayList<ContactEntryWithWhatsappId> contactsList;
    private ArrayList<ContactEntryWithWhatsappId> contactsListFiltered;
    private SelectionTracker selectionTracker = null;
    private boolean doNothingOnItemStateChanged;
    private long previousSelectionKey;
    private ContactsViewHolder selectedViewHolder;
    private ContactEntryWithWhatsappId selectedContact = null;
    private boolean clearingSelection = false;

    private boolean isSelectedContactViewHolderRecycled = false;

    private WhatsappCallPerformable whatsappCallPerformable;

    private ContactSelectionListenable contactSelectionChangeListener;

    private final long ghostSelectionKey = 1111;


    public void setAnimationManager(ContactSelectAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    public void setWhatsappCallPerformable(WhatsappCallPerformable whatsappCallPerformable) {
        this.whatsappCallPerformable = whatsappCallPerformable;
    }

    private ContactSelectAnimationManager animationManager;

    public ContactsRecyclerViewAdapter(ArrayList<ContactEntryWithWhatsappId> contactsList, @Nullable WhatsappCallPerformable whatsappCallPerformable,
                                       @Nullable ContactSelectionListenable contactSelectionChangeListener) {
        this.contactsList = contactsList;
        contactsListFiltered =  new ArrayList<>(contactsList);
        this.whatsappCallPerformable = whatsappCallPerformable;
        this.contactSelectionChangeListener = contactSelectionChangeListener;
        this.setHasStableIds(true);
    }

    public void setSelectionTracker(final SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
        selectionTracker.select(ghostSelectionKey);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
                if (!doNothingOnItemStateChanged && !clearingSelection) {
                    int numOfSelectedItems = selectionTracker.getSelection().size();
                    if (numOfSelectedItems == 1 + 1) {
                        previousSelectionKey = key;
                    } else if (numOfSelectedItems == 2 + 1) {
                        doNothingOnItemStateChanged = true;
                        selectionTracker.deselect(previousSelectionKey);
                        previousSelectionKey = key;
                        doNothingOnItemStateChanged = false;
                    } else if (numOfSelectedItems == 0 + 1) {
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

        ContactsViewHolder viewHolder = new ContactsViewHolder(viewHolderMainView, whatsappCallPerformable);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.bind(contactsListFiltered.get(position));
        if(selectionTracker != null){
            boolean activeStateInSelectionTracker = selectionTracker.isSelected((long)position);

            // Prevent selected view from being selected again (deselected, one view has always to be selected)
            if(activeStateInSelectionTracker != holder.isViewActive()){
                if(activeStateInSelectionTracker == true){

                    if(selectedContact != null){
                        if(selectedContact.getContactEntry().getContactId() == holder.getContact().getContactId()){
                            holder.setActive(true);
                            return;
                        }
                    }

                    if(animationManager == null){
                        holder.setActive(true);
                    }
                    else if(animationManager != null){
                        animationManager.PlaySelectAnimation(selectedViewHolder, holder, isSelectedContactViewHolderRecycled);
                    }

                    // Deselect previously selected view
                    if(selectedViewHolder != null){
                        selectedViewHolder.setActive(false);
                    }
                    selectedViewHolder = holder;
                    selectedContact = contactsListFiltered.get(position);
                    if(contactSelectionChangeListener != null){
                        contactSelectionChangeListener.contactSelectionChanged(selectedContact.getContactEntry());
                    }

                    isSelectedContactViewHolderRecycled = false;
                }
                else if(activeStateInSelectionTracker == false){
                    holder.setActive(false);
                }
            }
        }

    }


    @Override
    public void onViewRecycled(@NonNull ContactsViewHolder holder) {
        super.onViewRecycled(holder);

        if(selectedContact != null){
            if(holder.getContactEntryWithWhatsappId().getContactEntry().getContactId() == selectedContact.getContactEntry().getContactId()){
                isSelectedContactViewHolderRecycled = true;
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
                    ArrayList<ContactEntryWithWhatsappId> filteredList = new ArrayList<>();
                    for(ContactEntryWithWhatsappId contact: contactsList){
                        if(contact.getContactEntry().getContactName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(contact);
                        }
                    }

                    contactsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactsListFiltered = (ArrayList<ContactEntryWithWhatsappId>) filterResults.values;

                clearingSelection = true;

                selectionTracker.clearSelection();
                selectedContact = null;
                notifyDataSetChanged();

                if(contactSelectionChangeListener != null){
                    contactSelectionChangeListener.contactSelectionChanged(null);
                }

                clearingSelection = false;
            }
        };
    }
}
