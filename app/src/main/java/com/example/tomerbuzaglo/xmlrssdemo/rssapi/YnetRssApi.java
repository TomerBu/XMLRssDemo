package com.example.tomerbuzaglo.xmlrssdemo.rssapi;

import com.example.tomerbuzaglo.xmlrssdemo.AppManager;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.BusProvider;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.YnetEvent;
import com.example.tomerbuzaglo.xmlrssdemo.model.Rss;
import com.squareup.otto.Bus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class YnetRssApi {

    private static long CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    //The Apis root address without the node name: ...StoryRss2.xml/
    public static final String API_URL = "http://www.ynet.co.il/Integration/";
    private final Retrofit retrofit;
    private final YnetService ynetService;
    private final Bus mBus;

    public YnetRssApi() {
        retrofit = new Retrofit.Builder().
                baseUrl(API_URL).
                addConverterFactory(SimpleXmlConverterFactory.create()).
                client(AppManager.instance.getClient()).
                build();

        mBus = BusProvider.getInstance();
        // Create an instance of our API interface.
        ynetService = retrofit.create(YnetService.class);
    }

    public interface YnetService {
        @GET("StoryRss2.xml")
        Call<Rss> items();
    }

    public void getAllItems() {
        // Create a call instance for looking up Retrofit contributors.
        Call<Rss> call = ynetService.items();
        call.enqueue(new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                Rss body = response.body();
                if (body != null) {
                    mBus.post(new YnetEvent(body));
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}