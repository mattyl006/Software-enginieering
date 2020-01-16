package com.besttime.ui.viewModels;

import com.besttime.app.ContactEntry;
import com.besttime.ui.helpers.WhatsappContactIdRetriever;

public class ContactEntryWithWhatsappId {

    private ContactEntry contactEntry;
    private WhatsappContactIdRetriever whatsappContactIdRetriever;

    /**
     * This class holds contactEntry and whatsappContactIdRetriever together, beacuse whatsapp video call id can change over time and must be retrieved dynamically.
     * @param contactEntry
     * @param whatsappContactIdRetriever
     */
    public ContactEntryWithWhatsappId(ContactEntry contactEntry, WhatsappContactIdRetriever whatsappContactIdRetriever) {
        this.contactEntry = contactEntry;
        this.whatsappContactIdRetriever = whatsappContactIdRetriever;
    }
}
