package com.zemingo.rsssimulation.communication;

import androidx.annotation.NonNull;

public interface RssRepo {
    void fetchRss(@NonNull final String url, @NonNull final RssCallback callback);
}
