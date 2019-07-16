package com.zemingo.rsssimulation.models;

import androidx.annotation.NonNull;
import com.zemingo.rsssimulation.communication.Constants;

public class RssCategoryIdMapper implements RssIdMapper {

    @Override
    public String getRssId(@NonNull RssCategory category) {
        String id = null;
        switch (category) {
            case SPORT:
                id = Constants.SPORTS_URL_ID;
                break;
            case CARS:
                id = Constants.CARS_URL_ID;
                break;
            case CULTURE:
                id = Constants.CULTURE_URL_ID;
                break;
        }

        return id;
    }
}
