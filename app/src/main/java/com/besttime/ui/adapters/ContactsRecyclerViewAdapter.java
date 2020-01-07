package com.besttime.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.models.Contact;
import com.besttime.ui.adapters.viewHolders.ContactsViewHolder;
import com.example.besttime.R;

import java.util.ArrayList;


public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsViewHolder> {

    private ArrayList<Contact> contactsList;

    public ContactsRecyclerViewAdapter(ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
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
        holder.setContactName(contactsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}
