package com.example.maxpayne.mytodoapp.recycler_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemAnimator extends DefaultItemAnimator {
    private AccelerateInterpolator mAccelerateInterpolator = new AccelerateInterpolator();
    private DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder, int changeFlags, @NonNull List<Object> payloads) {
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder) {
        return super.recordPostLayoutInformation(state, viewHolder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        //super.animateAdd(holder);
        AnimatorSet as = new AnimatorSet();
        ObjectAnimator slideInRight = ObjectAnimator.ofFloat(holder.itemView, RecyclerView.TRANSLATION_X, -holder.itemView.getWidth(), 0);
        ObjectAnimator slideInFromBottom = ObjectAnimator.ofFloat(holder.itemView, RecyclerView.TRANSLATION_Y, -holder.itemView.getHeight(), 0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0, 1);
        as.playTogether(slideInFromBottom, fadeIn);
        as.setDuration(1000);
        as.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchAnimationFinished(holder);
            }
        });
        as.start();
        return true;
    }
}
