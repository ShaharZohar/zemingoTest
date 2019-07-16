package com.zemingo.rsssimulation.models;

import androidx.annotation.NonNull;

public interface RssIdMapper {
    String getRssId(@NonNull final RssCategory category);
}
