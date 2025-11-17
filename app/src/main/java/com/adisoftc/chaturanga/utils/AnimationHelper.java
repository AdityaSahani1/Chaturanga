package com.adisoftc.chaturanga.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

public class AnimationHelper {

    public static void animateButtonClick(View button) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 0.95f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 0.95f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(button, "scaleX", 1.0f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 1.0f);
        scaleUpX.setDuration(100);
        scaleUpY.setDuration(100);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.play(scaleUpX).with(scaleUpY);

        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.play(scaleDown).before(scaleUp);
        fullAnimation.start();
    }


    public static void animateDiceRoll(View diceView, Runnable onComplete) {
        ObjectAnimator rotateX = ObjectAnimator.ofFloat(diceView, "rotationX", 0f, 720f);
        ObjectAnimator rotateY = ObjectAnimator.ofFloat(diceView, "rotationY", 0f, 720f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(diceView, "scaleX", 1.0f, 1.3f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(diceView, "scaleY", 1.0f, 1.3f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateX, rotateY, scaleX, scaleY);
        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        if (onComplete != null) {
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onComplete.run();
                }
            });
        }

        animatorSet.start();
    }


    public static void animatePieceCapture(View pieceView) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(pieceView, "alpha", 1.0f, 0.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(pieceView, "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(pieceView, "scaleY", 1.0f, 0.2f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(pieceView, "rotation", 0f, 360f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeOut, scaleX, scaleY, rotate);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }


    public static void animateVictory(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.2f, 1.0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotate);
        animatorSet.setDuration(800);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.start();
    }


    public static void animateFadeIn(View view) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.start();
    }


    public static void animateFadeOut(View view) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.start();
    }


    public static void animateSlideIn(View view, String direction) {
        float startX = 0, startY = 0;

        switch (direction) {
            case "left":
                startX = -view.getWidth();
                break;
            case "right":
                startX = view.getWidth();
                break;
            case "top":
                startY = -view.getHeight();
                break;
            case "bottom":
                startY = view.getHeight();
                break;
        }

        view.setTranslationX(startX);
        view.setTranslationY(startY);

        ObjectAnimator slideX = ObjectAnimator.ofFloat(view, "translationX", 0f);
        ObjectAnimator slideY = ObjectAnimator.ofFloat(view, "translationY", 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(slideX, slideY);
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.start();
    }


    public static void animatePulse(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.1f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.1f, 1.0f);

        // Repeat on each animator (correct way)
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.RESTART);

        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.RESTART);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(600);
        animatorSet.start();
    }


    public static void animateShake(View view) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(
                view,
                "translationX",
                0f, -25f, 25f, -25f, 25f, -15f, 15f, -5f, 5f, 0f
        );

        shake.setDuration(500);
        shake.start();
    }
}