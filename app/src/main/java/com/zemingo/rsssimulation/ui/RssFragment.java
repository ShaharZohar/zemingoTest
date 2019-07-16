package com.zemingo.rsssimulation.ui;

import android.os.Bundle;
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
import com.zemingo.rsssimulation.communication.RemoteRssRepository;
import com.zemingo.rsssimulation.viewModel.RssViewModel;
import com.zemingo.rsssimulation.viewModel.RssViewModelFactory;
import me.toptas.rssconverter.RssItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RssFragment extends Fragment {

    private static final String TAG = "RssFragment";
    private static final String KEY_RSS_URL = "com.zemingo.rsssimulation.KEY_RSS_URL";

    private final RssAdapter mAdapter = new RssAdapter();
    private View mProgressBar;

    static RssFragment getInstance(@NonNull final String url) {
        final RssFragment rssFragment = new RssFragment();
        final Bundle args = new Bundle();
        args.putString(KEY_RSS_URL, url);
        rssFragment.setArguments(args);
        return rssFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RssViewModel rssViewModel = initRssViewModel();

        try {
            String url = getRssUrl(getArguments());
            rssViewModel.getRss(url);
        }
        catch (Exception e) {
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
        String url = null;
        if (savedInstanceState != null) {
            url = savedInstanceState.getString(KEY_RSS_URL);
        }

        if (url == null) {
            throw new IllegalArgumentException("Could not get rss url");
        }

        return url;
    }

    @NotNull
    private RssViewModel initRssViewModel() {
        RssViewModel mRssViewModel = ViewModelProviders
                .of(this, new RssViewModelFactory(new RemoteRssRepository()))
                .get(RssViewModel.class);

        setProgressBarVisibility(true);
        mRssViewModel
                .getRssItemsLiveData()
                .observe(this, new Observer<List<RssItem>>() {
                    @Override
                    public void onChanged(List<RssItem> rssItems) {
                        setProgressBarVisibility(false);
                        onRssFeedReceived(rssItems);
                    }
                });
        return mRssViewModel;
    }

    private void initRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rss_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    private void initProgressBar(@NonNull View view) {
        mProgressBar = view.findViewById(R.id.rss_progress_bar);
    }

    private void setProgressBarVisibility(boolean isVisible) {
        if (isVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void onRssFeedReceived(List<RssItem> rssItems) {
        if (rssItems != null) {
            mAdapter.updateData(rssItems);
        }
    }
}
