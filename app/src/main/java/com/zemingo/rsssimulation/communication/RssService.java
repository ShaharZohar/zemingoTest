package com.zemingo.rsssimulation.communication;

import me.toptas.rssconverter.RssFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RssService {

    @GET("FeederNode")
    Call<RssFeed> getRss(@Query("iID") String path);

}
