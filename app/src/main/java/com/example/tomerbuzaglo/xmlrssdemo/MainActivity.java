package com.example.tomerbuzaglo.xmlrssdemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.example.tomerbuzaglo.xmlrssdemo.adapters.YnetRecyclerViewAdapter;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.BusProvider;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.YnetEvent;
import com.example.tomerbuzaglo.xmlrssdemo.rssapi.YnetRssApi;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rvYnet)
    RecyclerView rvYnet;
    @Bind(R.id.coord)
    CoordinatorLayout coord;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ivHeader)
    ImageView ivHeader;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    private YnetRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        BusProvider.getInstance().register(this);
        YnetRssApi api = new YnetRssApi();
        api.getAllItems();
    }

    @Subscribe
    public void newData(YnetEvent event) {
        this.adapter = new YnetRecyclerViewAdapter(event.getRss(), this);
        rvYnet.setLayoutManager(new LinearLayoutManager(this));
        rvYnet.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(20, 0, 20, 50);
            }
        });


/*
        AnimationAdapter aAdapter = new ScaleInAnimationAdapter(adapter);
        aAdapter.setDuration(250);
        aAdapter.setInterpolator(new OvershootInterpolator(.5f));
*/

/*
        AnimationAdapter aAdapter = new AlphaInAnimationAdapter(adapter);
        aAdapter.setDuration(250);
        aAdapter.setInterpolator(new OvershootInterpolator(.5f));*/


        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(100);
        alphaAdapter.setInterpolator(new AnticipateOvershootInterpolator(1f));

        //ScaleAdapter + AlphaAdapter
        ScaleInAnimationAdapter scaleAndAlpahAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleAndAlpahAdapter.setDuration(250);
        scaleAndAlpahAdapter.setInterpolator(new OvershootInterpolator(.4f));


        rvYnet.setAdapter(scaleAndAlpahAdapter);
    }
}
