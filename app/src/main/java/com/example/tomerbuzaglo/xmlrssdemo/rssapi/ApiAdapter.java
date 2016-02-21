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


public class ApiAdapter {

    private final Bus mBus;

    private final Class<?> service;
    private final Object mAPI;


    public ApiAdapter(String baseUrl, Class<?> service) {
        this.service = service;

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(baseUrl).
                addConverterFactory(SimpleXmlConverterFactory.create()).
                client(AppManager.instance.getClient()).
                build();

        mBus = BusProvider.getInstance();
        mAPI = retrofit.create(service);
    }

    public void getAllItems() {
        // Create a call instance for looking up Retrofit contributors.
        Call<Rss> call = null;
        if (mAPI instanceof YnetRssApi.YnetService) {

            call = ((YnetRssApi.YnetService) mAPI).items();
        } else if (mAPI instanceof WallaRssApi.WallaService) {
            call = ((WallaRssApi.WallaService) mAPI).items();
        }
        else if (mAPI instanceof HaretzRssApi.HaretzService) {
            call = ((HaretzRssApi.HaretzService) mAPI).items();
        }
        else return;
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
