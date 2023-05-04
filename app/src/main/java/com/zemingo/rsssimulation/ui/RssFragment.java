package com.zemingo.rsssimulation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zemingo.rsssimulation.R;
import com.zemingo.rsssimulation.models.LoadingProgress;
import com.zemingo.rsssimulation.repositories.RemoteRssRepository;
import com.zemingo.rsssimulation.utils.InternetBrowserHandler;
import com.zemingo.rsssimulation.viewModel.RssViewModel;
import com.zemingo.rsssimulation.viewModel.RssViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.toptas.rssconverter.RssItem;

public class RssFragment extends Fragment {

    private static final String TAG = "RssFragment";
    private static final String KEY_RSS_URL_ID = "com.zemingo.rsssimulation.KEY_RSS_URL_ID";

    private static ArrayList<String> s_rssUrlsList = new ArrayList<>();
    private static boolean s_isMultiRssFeed = false;

    private  final Handler refreshHandler = new Handler();

    private final RssAdapter mAdapter = new RssAdapter();
    private View mProgressBar;

    static RssFragment getInstance(@NonNull final String rssUrlId) {
        s_isMultiRssFeed = false;
        final RssFragment rssFragment = new RssFragment();
        final Bundle args = new Bundle();
        args.putString(KEY_RSS_URL_ID, rssUrlId);
        rssFragment.setArguments(args);
        return rssFragment;
    }

    static RssFragment getInstance(@NonNull final ArrayList<String> rssUrlsList) {
        s_isMultiRssFeed = true;
        final RssFragment rssFragment = new RssFragment();
        s_rssUrlsList = rssUrlsList;
        return rssFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getRssFeeds();

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                // check for feeds updates
//                getRssFeeds();
//                refreshHandler.postDelayed(this, 5 * 1000);
//            }
//        };
//
//        runnable.run();

    }

    private void getRssFeeds(){
        RssViewModel rssViewModel = initRssViewModel();

        try {
            if(!s_isMultiRssFeed) {
                String url = getRssUrl(getArguments());
                ArrayList<String> rssUrlsList = new ArrayList<>();
                rssUrlsList.add(url);
                rssViewModel.getRss(rssUrlsList);
            }
            else // is multi rss-feed
            {
                rssViewModel.getRss(s_rssUrlsList);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse rss URL", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initProgressBar(view);
        initRecyclerView(view);
    }

    private String getRssUrl(@Nullable Bundle savedInstanceState) {
        String urlId = null;
        if (savedInstanceState != null) {
            urlId = savedInstanceState.getString(KEY_RSS_URL_ID);
        }

        if (urlId == null) {
            throw new IllegalArgumentException("Could not get rss urlId");
        }

        return urlId;
    }

    @NotNull
    private RssViewModel initRssViewModel() {
        RssViewModel rssViewModel = ViewModelProviders
                .of(this, new RssViewModelFactory(new RemoteRssRepository()))
                .get(RssViewModel.class);

        rssViewModel
                .getLoadingProgressLiveData()
                .observe(this, new Observer<LoadingProgress>() {
                    @Override
                    public void onChanged(LoadingProgress loadingProgress) {
                        onProgressChanged(loadingProgress);
                    }
                });

        rssViewModel
                .getRssItemsLiveData()
                .observe(this, new Observer<List<RssItem>>() {
                    @Override
                    public void onChanged(List<RssItem> rssItems) {
                        onRssFeedReceived(rssItems);
                    }
                });
        return rssViewModel;
    }

    private void initRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rss_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnRssClickListener(new RssAdapter.OnRssClickListener() {
            @Override
            public void onClick(@NotNull final RssItem item) {
                if (item.getLink() != null) {
                    openBrowser(item.getLink());
                }

                if(item.getTitle() != null) {
                    // save rss title into shared preference
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("RSS_SP", 0).edit();
                    editor.putString("rss_title", item.getTitle());
                    editor.apply();
                }
            }
        });
    }

    private void openBrowser(@NonNull final String url) {
        try {
            InternetBrowserHandler.getInstance().openBrowsweLink(requireContext(), url);
        } catch (Exception e) {
            Log.e(TAG, "Unable to open link", e);
        }
    }

    private void initProgressBar(@NonNull View view) {
        mProgressBar = view.findViewById(R.id.rss_progress_bar);
    }

    private void onProgressChanged(LoadingProgress loadingProgress) {
        switch (loadingProgress) {
            case IDLE:
                setProgressBarVisibility(false);
                break;
            case LOADING:
                setProgressBarVisibility(true);
                break;
        }
    }

    private void setProgressBarVisibility(boolean isVisible) {
        if (isVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void onRssFeedReceived(List<RssItem> rssItems) {
        if (rssItems != null) {
            mAdapter.updateData(rssItems);
        }
    }
}
