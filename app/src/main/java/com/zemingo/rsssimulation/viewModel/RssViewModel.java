package com.zemingo.rsssimulation.viewModel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zemingo.rsssimulation.communication.RssCallback;
import com.zemingo.rsssimulation.communication.RssRequestHandler;
import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;

import java.util.ArrayList;
import java.util.List;

public class RssViewModel extends ViewModel {

    private static final String TAG = "RssViewModel";

    private MutableLiveData<List<RssItem>> mRssItemsLiveData = new MutableLiveData<>();

    public LiveData<List<RssItem>> getRssItemsLiveData() {
        return mRssItemsLiveData;
    }

    public void getRss(@NonNull final String url) {
        fetchRss(url);
    }

    private void fetchRss(@NonNull final String url) {
        RssRequestHandler
                .getInstance()
                .getRss(url, new RssCallback() {
                    @Override
                    public void onReceived(RssFeed feed) {
                        Log.d(TAG, "Got rss feed for " + url);
                        postRssItems(feed.getItems());
                    }

                    @Override
                    public void onFailure(Throwable tr) {
                        Log.d(TAG, "Failed to get rss for " + url);
                        postRssItems(new ArrayList<RssItem>());
                    }
                });
    }

    private void postRssItems(List<RssItem> feed) {
        if (feed != null) {
            mRssItemsLiveData.postValue(feed);
        }
    }
}
