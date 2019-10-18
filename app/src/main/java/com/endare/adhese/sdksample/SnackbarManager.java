package com.endare.adhese.sdksample;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;

public class SnackbarManager {

    private View rootLayout;

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

    private void show(@NonNull String message, int backgroundColor) {
        Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
                .setActionTextColor(colorText);
        snackbar.getView().setBackgroundColor(backgroundColor);
        snackbar.show();
    }

}
