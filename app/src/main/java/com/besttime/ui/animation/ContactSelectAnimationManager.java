package com.besttime.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import com.besttime.ui.adapters.viewHolders.ContactsViewHolder;

public class ContactSelectAnimationManager {

    private View movingContactItem;
    private View clickBlocker;
    private long animationDuration;
    private int yOffset;


    /**
     *  @param movingContactItem View that will simulate movement of selection
     * @param clickBlocker View which will prevent user from selecting other contacts while animation is being played
     * @param animationDuration
     */
    public ContactSelectAnimationManager(View movingContactItem, View clickBlocker, long animationDuration, int yOffset) {
        this.movingContactItem = movingContactItem;
        this.clickBlocker = clickBlocker;
        this.animationDuration = animationDuration;
        this.yOffset = yOffset;
    }

    public void PlaySelectAnimation(final ContactsViewHolder viewHolderFrom, final ContactsViewHolder viewHolderTo, boolean isViewHolderFromRecycled){

        float startingY = yOffset;
        float endingY = yOffset;

        if(!isViewHolderFromRecycled){
            if (viewHolderFrom != null) {
                startingY += viewHolderFrom.itemView.getY();
            } else {
                startingY += viewHolderTo.itemView.getY();
            }
        }
        else{
            startingY = 0;
        }


        endingY += viewHolderTo.itemView.getY();

        ObjectAnimator selectAnimation = ObjectAnimator.ofFloat(movingContactItem, "y", startingY, endingY);
        selectAnimation.setDuration(animationDuration);

        selectAnimation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                movingContactItem.setVisibility(View.VISIBLE);
                clickBlocker.setVisibility(View.VISIBLE);
                clickBlocker.setClickable(true);
                clickBlocker.setFocusable(true);
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                movingContactItem.setVisibility(View.GONE);
                viewHolderTo.setActive(true);
                clickBlocker.setVisibility(View.GONE);
                clickBlocker.setClickable(false);
                clickBlocker.setFocusable(false);
            }
        });
        selectAnimation.start();
    }
}
