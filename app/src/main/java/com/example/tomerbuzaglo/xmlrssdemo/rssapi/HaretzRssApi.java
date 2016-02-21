package com.example.tomerbuzaglo.xmlrssdemo.rssapi;

import com.example.tomerbuzaglo.xmlrssdemo.AppManager;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.BusProvider;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.RssEvent;
import com.example.tomerbuzaglo.xmlrssdemo.model.Rss;
import com.squareup.otto.Bus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class HaretzRssApi {

    private static long CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    //The Apis root address without the node name:
    public static final String API_URL = "http://www.haaretz.co.il/";

    private final Retrofit retrofit;
    private final HaretzService wallaService;
    private final Bus mBus;

    public HaretzRssApi() {
        retrofit = new Retrofit.Builder().
                baseUrl(API_URL).
                addConverterFactory(SimpleXmlConverterFactory.create()).
                client(AppManager.instance.getClient()).
                build();

        mBus = BusProvider.getInstance();
        // Create an instance of our API interface.
        wallaService = retrofit.create(HaretzService.class);
    }

    public interface HaretzService {
        //@GET("StoryRss2.xml")
        @GET("cmlink/1.1617539")
        Call<Rss> items();
    }

    public void getAllItems() {
        // Create a call instance for looking up Retrofit contributors.
        Call<Rss> call = wallaService.items();
        call.enqueue(new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                Rss body = response.body();
                if (body != null) {
                    mBus.post(new RssEvent(body));
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}