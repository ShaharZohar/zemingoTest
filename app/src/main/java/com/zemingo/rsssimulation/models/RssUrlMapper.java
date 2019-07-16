package com.zemingo.rsssimulation.models;

import androidx.annotation.NonNull;

public interface RssUrlMapper {
    String getUrl(@NonNull final RssCategory category);
}
