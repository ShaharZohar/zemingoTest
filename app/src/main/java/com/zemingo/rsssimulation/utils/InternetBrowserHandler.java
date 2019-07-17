package com.zemingo.rsssimulation.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

public class InternetBrowserHandler {
    private static final InternetBrowserHandler ourInstance = new InternetBrowserHandler();

    public static InternetBrowserHandler getInstance() {
        return ourInstance;
    }

    private InternetBrowserHandler() {
    }

    public void openBrowsweLink(@NonNull final Context context, @NonNull final String url) throws ActivityNotFoundException {
        context.startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(url))
        );
    }
}
