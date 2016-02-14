package com.example.tomerbuzaglo.xmlrssdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.tomerbuzaglo.xmlrssdemo.rssapi.YnetRssApi;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.BusProvider;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.YnetEvent;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusProvider.getInstance().register(this);
        YnetRssApi api = new YnetRssApi();

        api.getAllItems();
    }

    @Subscribe
    public void newData(YnetEvent event) {
        Toast.makeText(this, event.getRss().toString(), Toast.LENGTH_LONG).show();
    }
}
