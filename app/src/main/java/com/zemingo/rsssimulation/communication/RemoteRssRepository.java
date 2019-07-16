package com.zemingo.rsssimulation.communication;

import androidx.annotation.NonNull;

public class RemoteRssRepository implements RssRepo {

    @Override
    public void fetchRss(@NonNull String url, @NonNull RssCallback callback) {
        RssRequestHandler
                .getInstance()
                .getRss(url, callback);
    }
}
