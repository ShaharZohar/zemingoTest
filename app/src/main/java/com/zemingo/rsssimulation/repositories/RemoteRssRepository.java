package com.zemingo.rsssimulation.repositories;

import androidx.annotation.NonNull;
import com.zemingo.rsssimulation.communication.RssCallback;
import com.zemingo.rsssimulation.communication.RssRequestHandler;

public class RemoteRssRepository implements RssRepository {

    @Override
    public void fetchRss(@NonNull String url, @NonNull RssCallback callback) {
        RssRequestHandler
                .getInstance()
                .getRss(url, callback);
    }
}
