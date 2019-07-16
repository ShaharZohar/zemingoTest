package com.zemingo.rsssimulation.communication;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.toptas.rssconverter.RssConverterFactory;
import me.toptas.rssconverter.RssFeed;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.concurrent.Executors;

public class RssRequestHandler {

    private static final String TAG = "RssRequestHandler";
    private static RssRequestHandler ourInstance;

    public static RssRequestHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new RssRequestHandler();
        }
        return ourInstance;
    }

    private RssRequestHandler() {
        msRssService =
                buildClient()
                        .create(RssService.class);
    }

    private static RssService msRssService;

    private Retrofit buildClient() {
        return new Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(RssConverterFactory.Companion.create())
                .build();
    }

    public void getRss(@NonNull final String url, @NonNull final RssCallback callback) {
        msRssService
                .getRss(url)
                .enqueue(new Callback<RssFeed>() {
                    @Override
                    public void onResponse(@NotNull Call<RssFeed> call, @NotNull Response<RssFeed> response) {
                        try {
                            final RssFeed rssFeed = parseFeed(response);
                            if (rssFeed != null) {
                                callback.onReceived(rssFeed);
                            } else {
                                callback.onFailure(new Exception("Unable to get rss feed"));
                            }
                        } catch (Exception e) {
                            callback.onFailure(e);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RssFeed> call, @NotNull Throwable t) {
                        Log.e(TAG, "Failed to get Rss", t);
                        callback.onFailure(t);
                    }
                });
    }

    @Nullable
    private RssFeed parseFeed(@NotNull final Response<RssFeed> response) {
        if (response.isSuccessful()) {
            return response.body();
        }

        return null;
    }
}
