package com.zemingo.rsssimulation.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zemingo.rsssimulation.communication.RssCallback;
import com.zemingo.rsssimulation.models.LoadingProgress;
import com.zemingo.rsssimulation.repositories.RssRepository;

import java.util.ArrayList;
import java.util.List;

import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;

public class RssViewModel extends ViewModel {

    private static final String TAG = "RssViewModel";

    private int m_ind = 0;
    private  ArrayList<RssItem> m_rssFeedsList = new ArrayList<>();

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

    public void getRss(@NonNull final ArrayList<String> rssFeedsList) {
        Log.d("TEST", "getRss rssFeedsList size: " + rssFeedsList.size());
        fetchRss(rssFeedsList, 0);
    }

    private void fetchRss(@NonNull final ArrayList<String> rssFeedsList, @NonNull int ind) {

        Log.d("TEST", "loading feed " + ind);
        postLoadingProgress(LoadingProgress.LOADING);
        mRssRepo
                .fetchRss(rssFeedsList.get(ind), new RssCallback() {
                    @Override
                    public void onReceived(RssFeed feed) {
                        Log.d(TAG, "Got rss feed for " + rssFeedsList.get(m_ind));

                        m_rssFeedsList.addAll(feed.getItems());
                        postRssItems(m_rssFeedsList);

                        if((rssFeedsList.size() > 1))
                        {
                            rssFeedsList.remove(0);
                            m_ind = m_ind++;
                            Log.d(TAG, "Get next rss " + m_ind);
                            fetchRss(rssFeedsList, 0);
                        }
                        else {
                            postLoadingProgress(LoadingProgress.IDLE);
                            m_rssFeedsList = new ArrayList<>();
                        }
                    }

                    @Override
                    public void onFailure(Throwable tr) {
                        Log.d(TAG, "Failed to get rss for " + rssFeedsList.get(m_ind));
                        postRssItems(m_rssFeedsList);

                        if((rssFeedsList.size() > 1))
                        {
                            rssFeedsList.remove(0);
                            m_ind = m_ind++;
                            Log.d(TAG, "Get next rss " + m_ind);
                            fetchRss(rssFeedsList, 0);
                        }
                        else {
                            postLoadingProgress(LoadingProgress.IDLE);
                            m_rssFeedsList = new ArrayList<>();
                        }
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
