package com.zemingo.rsssimulation.models;

import androidx.annotation.NonNull;
import com.zemingo.rsssimulation.communication.Constants;

public class RssCategoryUrlMapper implements RssUrlMapper {

    @Override
    public String getUrl(@NonNull RssCategory category) {
        String url = null;
        switch (category) {
            case SPORT:
                url = Constants.SPORTS_URL;
                break;
            case CARS:
                url = Constants.CARS_URL;
                break;
            case CULTURE:
                url = Constants.CULTURE_URL;
                break;
        }

        return url;
    }
}
