package com.example.tomerbuzaglo.xmlrssdemo.rssapi;

import android.content.Context;

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
    private static Context mContext;
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





//
//    public static void init() {
//        // Create Cache
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(cacheDirectory, cacheSize);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .cache(cache)
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//    }
//
//
//
//    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//
//            // Add Cache Control only for GET methods
//            if (request.method().equals("GET")) {
//                if (ConnectivityHelper.isNetworkAvailable(mContext)) {
//                    // 1 day
//                    request.newBuilder()
//                            .header("Cache-Control", "only-if-cached")
//                            .build();
//                } else {
//                    // 4 weeks stale
//                    request.newBuilder()
//                            .header("Cache-Control", "public, max-stale=2419200")
//                            .build();
//                }
//            }
//
//            okhttp3.Response response = chain.proceed(request);
//
//            // Re-write response CC header to force use of cache
//            return response.newBuilder()
//                    .header("Cache-Control", "public, max-age=86400") // 1 day
//                    .build();
//
//
//        }
//
//    };
}