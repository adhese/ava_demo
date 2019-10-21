package com.endare.adhese.sdksample;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.NonNull;

public class SnackbarManager {

    private View rootLayout;
    private boolean isDisplayingSnackbar;

    private final Queue<Snackbar> snackbarQueue;
    private final int colorInfo;
    private final int colorSuccess;
    private final int colorError;
    private final int colorText;

    public SnackbarManager(@NonNull Context context, @NonNull View rootLayout) {
        this.rootLayout = rootLayout;
        this.colorInfo = context.getResources().getColor(R.color.colorInfo);
        this.colorSuccess = context.getResources().getColor(R.color.colorSuccess);
        this.colorError = context.getResources().getColor(R.color.colorError);
        this.colorText = context.getResources().getColor(android.R.color.white);
        this.snackbarQueue = new LinkedList<>();
    }

    public void setRootLayout(@NonNull View rootLayout) {
        this.rootLayout = rootLayout;
    }

    public void showInfo(@NonNull String message) {
        show(message, colorInfo);
    }

    public void showSuccess(@NonNull String message) {
        show(message, colorSuccess);
    }

    public void showError(@NonNull String message) {
        show(message, colorError);
    }

    /**
     * Calling this method will queue the Snackbar and show it when it's its turn in the queue.
     */
    private void show(@NonNull String message, int backgroundColor) {
        Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
                .setActionTextColor(colorText)
                .setDuration(BaseTransientBottomBar.LENGTH_LONG)
                .addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {

                        if (snackbarQueue.size() > 0 && event != Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                            showNext();
                            return;
                        }

                        isDisplayingSnackbar = false;
                    }
                });

        snackbar.getView().setBackgroundColor(backgroundColor);

        snackbarQueue.add(snackbar);

        if (!isDisplayingSnackbar) {
            showNext();
        }
    }

    /**
     * Shows the next Snackbar in the queue
     */
    private void showNext() {
        Snackbar nextSnackbar = snackbarQueue.poll();

        if (nextSnackbar == null) {
            return;
        }

        nextSnackbar.show();
        isDisplayingSnackbar = true;
    }

}
