package com.besttime.app.mockClasses;

import com.besttime.app.ContactEntry;
import com.besttime.app.helpers.WhatsappCallPerformable;
import com.besttime.app.helpers.WhatsappRedirector;

public class MockApp implements WhatsappCallPerformable {

    private WhatsappRedirector whatsappRedirector;

    public MockApp(WhatsappRedirector whatsappRedirector) {
        this.whatsappRedirector = whatsappRedirector;
    }

    @Override
    public void whatsappForward(ContactEntry contactEntry) {
        whatsappRedirector.redirectToWhatsappVideoCall(contactEntry);
    }


}
