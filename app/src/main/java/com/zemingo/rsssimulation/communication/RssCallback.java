package com.zemingo.rsssimulation.communication;

import me.toptas.rssconverter.RssFeed;

public interface RssCallback {
    void onReceived(RssFeed feed);
    void onFailure(Throwable tr);
}
