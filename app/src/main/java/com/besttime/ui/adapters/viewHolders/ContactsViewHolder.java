package com.besttime.ui.adapters.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besttime.R;

public class ContactsViewHolder extends RecyclerView.ViewHolder {


    private RelativeLayout parentView;
    private TextView contactNameTextView;
    private Button whatsappRedirectButton;

    public ContactsViewHolder(@NonNull RelativeLayout parentView) {
        super(parentView);

        this.parentView = parentView;
        contactNameTextView = parentView.findViewById(R.id.contactNameTextView_itemView);
        whatsappRedirectButton = parentView.findViewById(R.id.whatsappRedirectButton_itemView);
    }



}
