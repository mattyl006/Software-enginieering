package com.besttime.ui.utils;

import androidx.annotation.Nullable;

import com.besttime.app.ContactEntry;

public interface ContactSelectionListenable {

    void contactSelectionChanged(@Nullable ContactEntry newSelectedContact);
}
