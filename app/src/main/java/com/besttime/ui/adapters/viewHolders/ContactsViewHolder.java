package com.besttime.ui.adapters.viewHolders;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.besttime.app.ContactEntry;
import com.besttime.app.helpers.WhatsappCallPerformable;
import com.besttime.ui.viewModels.ContactEntryWithWhatsappId;
import com.example.besttime.R;

public class ContactsViewHolder extends RecyclerView.ViewHolder {


    private RelativeLayout parentView;
    private TextView contactNameTextView;
    private Button whatsappRedirectButton;

    private boolean isSelected = false;

    private ContactEntryWithWhatsappId contactEntryWithWhatsappId;

    private WhatsappCallPerformable whatsappCallPerformable;

    public ContactsViewHolder(@NonNull RelativeLayout itemView, @Nullable final WhatsappCallPerformable whatsappCallPerformable) {
        super(itemView);

        this.parentView = itemView;
        contactNameTextView = itemView.findViewById(R.id.contactNameTextView_itemView);
        whatsappRedirectButton = itemView.findViewById(R.id.whatsappRedirectButton_itemView);
        this.whatsappCallPerformable = whatsappCallPerformable;


        whatsappRedirectButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(whatsappCallPerformable != null){
                    if(contactEntryWithWhatsappId != null){
                        whatsappCallPerformable.whatsappForward(contactEntryWithWhatsappId.getContactEntry());
                    }
                }
                return true;
            }
        });
    }

    public void setWhatsappCallPerformable(WhatsappCallPerformable whatsappCallPerformable) {
        this.whatsappCallPerformable = whatsappCallPerformable;
    }

    public void setContactName(String contactName){
        contactNameTextView.setText(contactName);
    }

    public void bind(ContactEntryWithWhatsappId contactEntryWithWhatsappId){
        this.contactEntryWithWhatsappId = contactEntryWithWhatsappId;
        contactNameTextView.setText(contactEntryWithWhatsappId.getContactEntry().getContactName());
    }

    public ContactEntryWithWhatsappId getContactEntryWithWhatsappId(){
        return contactEntryWithWhatsappId;
    }

    public ContactEntry getContact(){
        return contactEntryWithWhatsappId.getContactEntry();
    }

    public long getWhatsappVideCallId(){
        return contactEntryWithWhatsappId.getWhatsappVideCallId();
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
