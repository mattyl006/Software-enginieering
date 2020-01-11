package com.besttime.ui.adapters.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.models.Contact;
import com.example.besttime.R;

public class ContactsViewHolder extends RecyclerView.ViewHolder {


    private RelativeLayout parentView;
    private TextView contactNameTextView;
    private Button whatsappRedirectButton;

    private boolean isSelected = false;

    private Contact contact;

    public ContactsViewHolder(@NonNull RelativeLayout itemView) {
        super(itemView);

        this.parentView = itemView;
        contactNameTextView = itemView.findViewById(R.id.contactNameTextView_itemView);
        whatsappRedirectButton = itemView.findViewById(R.id.whatsappRedirectButton_itemView);
    }

    public void setContactName(String contactName){
        contactNameTextView.setText(contactName);
    }

    public void bind(Contact contact){
        this.contact = contact;
        contactNameTextView.setText(contact.getName());
    }

    public Contact getContact(){
        return contact;
    }

    public void setActive(boolean isActive){
        if(isActive != this.isViewActive())
        {
            parentView.setActivated(isActive);
            if(isActive){
                whatsappRedirectButton.setVisibility(View.VISIBLE);
                isSelected = true;
            }
            else{
                whatsappRedirectButton.setVisibility(View.GONE);
                isSelected = false;
            }
        }
    }



    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
        ItemDetailsLookup.ItemDetails<Long> itemDetails = new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Override
            public Long getSelectionKey() {
                return (long)getAdapterPosition();
            }
        };

        return itemDetails;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public boolean isViewActive() {
        return parentView.isActivated();
    }
}
