package com.example.tomerbuzaglo.xmlrssdemo;

import android.app.Application;

import com.example.tomerbuzaglo.xmlrssdemo.utils.ConnectivityHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tomerbuzaglo on 18/02/2016.
 */
public class AppManager extends Application {
    public static AppManager instance;
    private OkHttpClient client;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    //Use cache for network requests
    public synchronized OkHttpClient getClient() {
        if (client == null) {
            // set Cache
            int cacheSize = 10 * 1024 * 1024; // 10 MiB

            //setup cache
            File httpCacheDirectory = new File(getCacheDir(), "responses");


            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(mCacheControlInterceptor)

                    .build();
            this.client = client;
        }
        return client;
    }


    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                if (!ConnectivityHelper.isConnected()) {
                    // 4 weeks stale
                    request = request.newBuilder()
                            .header("Cache-Control", "public, max-stale=2419200")
                            .build();
                }
            }

            Response originalResponse = chain.proceed(request);
            return originalResponse.newBuilder()
                    .header("Cache-Control", "max-age=600")
                    .build();
        }
    };

//    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//
//            // Add Cache Control only for GET methods
//            if (request.method().equals("GET")) {
//                if (!ConnectivityHelper.isConnected()) {
//                    request.newBuilder()
//                            .header("Cache-Control", "only-if-cached")
//                            .build();
//                }
//            }
//
//            okhttp3.Response originalResponse = chain.proceed(request);
//
//            // Re-write response CC header to force use of cache
//            return originalResponse.newBuilder()
//                    //.header("Cache-Control", "public, max-age=40*60*60*24") // save cache for 40 days (No need to interfere with the servers headers)
//                    .header("Cache-Control", "public")
//                    .build();
//
//        }
//    };
}
