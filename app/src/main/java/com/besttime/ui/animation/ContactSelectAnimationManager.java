package com.besttime.ui.animation;

import android.view.View;

public class ContactSelectAnimationManager {

    private View movingContactItem;
    private long animationDuration;


    /**
     *
     * @param movingContactItem View that will simulate movement of selection
     * @param animationDuration
     */
    public ContactSelectAnimationManager(View movingContactItem, long animationDuration) {
        this.movingContactItem = movingContactItem;
        this.animationDuration = animationDuration;
    }
}
