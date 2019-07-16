package com.zemingo.rsssimulation.repositories;

import androidx.annotation.NonNull;
import com.zemingo.rsssimulation.communication.RssCallback;

public interface RssRepository {
    void fetchRss(@NonNull final String url, @NonNull final RssCallback callback);
}
