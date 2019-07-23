package com.zemingo.rsssimulation.viewModel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zemingo.rsssimulation.communication.RssCallback;
import com.zemingo.rsssimulation.models.LoadingProgress;
import com.zemingo.rsssimulation.repositories.RssRepository;
import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;

import java.util.ArrayList;
import java.util.List;

public class RssViewModel extends ViewModel {

    private static final String TAG = "RssViewModel";

    RssViewModel(RssRepository mRssRepo) {
        this.mRssRepo = mRssRepo;
    }

    private RssRepository mRssRepo;

    private MutableLiveData<List<RssItem>> mRssItemsLiveData = new MutableLiveData<>();

    private MutableLiveData<LoadingProgress> mLoadingProgressLiveData = new MutableLiveData<>();

    public LiveData<List<RssItem>> getRssItemsLiveData() {
        return mRssItemsLiveData;
    }

    public LiveData<LoadingProgress> getLoadingProgressLiveData() {
        return mLoadingProgressLiveData;
    }

    public void getRss(@NonNull final String urlId) {
        fetchRss(urlId);
    }

    private void fetchRss(@NonNull final String urlId) {
        postLoadingProgress(LoadingProgress.LOADING);
        mRssRepo
                .fetchRss(urlId, new RssCallback() {
                    @Override
                    public void onReceived(RssFeed feed) {
                        Log.d(TAG, "Got rss feed for " + urlId);
                        postRssItems(feed.getItems());
                        postLoadingProgress(LoadingProgress.IDLE);
                    }

                    @Override
                    public void onFailure(Throwable tr) {
                        Log.d(TAG, "Failed to get rss for " + urlId);
                        postRssItems(new ArrayList<RssItem>());
                        postLoadingProgress(LoadingProgress.IDLE);
                    }
                });
    }

    private void postRssItems(List<RssItem> feed) {
        if (feed != null) {
            mRssItemsLiveData.postValue(feed);
        }
    }

    private void postLoadingProgress(LoadingProgress loadingProgress) {
        mLoadingProgressLiveData.postValue(loadingProgress);
    }
}
