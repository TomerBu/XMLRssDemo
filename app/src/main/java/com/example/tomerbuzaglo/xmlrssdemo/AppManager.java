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
import okhttp3.logging.HttpLoggingInterceptor;


public class AppManager extends Application {
    public static AppManager instance;
    private OkHttpClient client;

    @Override
    public void onCreate() {
        instance = this;
        ConnectivityHelper.registerDefault(this);
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

            //Logging Interceptor, added to the client
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(mCacheControlInterceptor)
                   // .addInterceptor(logging)  // <-- add logging as LAST! interceptor
                    .build();
            this.client = client;
        }
        return client;
    }


    private void f() {

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
}
