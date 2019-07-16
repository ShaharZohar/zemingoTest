package com.zemingo.rsssimulation.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.zemingo.rsssimulation.repositories.RssRepository;

public class RssViewModelFactory implements ViewModelProvider.Factory {

    private RssRepository mRssRepo;

    public RssViewModelFactory(RssRepository rssRepo) {
        this.mRssRepo = rssRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        final T vm = modelClass.cast(new RssViewModel(mRssRepo));
        if (vm == null) {
            throw new IllegalArgumentException("Unable to create rss view model");
        }

        return vm;
    }
}
