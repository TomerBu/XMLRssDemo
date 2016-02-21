package com.example.tomerbuzaglo.xmlrssdemo.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.tomerbuzaglo.xmlrssdemo.R;
import com.example.tomerbuzaglo.xmlrssdemo.adapters.NewsRecyclerViewAdapter;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.BusProvider;
import com.example.tomerbuzaglo.xmlrssdemo.eventbus.RssEvent;
import com.example.tomerbuzaglo.xmlrssdemo.model.Item;
import com.example.tomerbuzaglo.xmlrssdemo.rssapi.ApiAdapter;
import com.example.tomerbuzaglo.xmlrssdemo.rssapi.HaretzRssApi;
import com.example.tomerbuzaglo.xmlrssdemo.rssapi.WallaRssApi;
import com.example.tomerbuzaglo.xmlrssdemo.rssapi.YnetRssApi;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import tomerbu.edu.recyclerviewhelper.VerticalScrollSupport;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    public static final int API_TYPE_WALLA = 1;
    public static final int API_TYPE_YNET = 2;
    public static final int API_TYPE_ROTTER = 3;
    public static final int API_TYPE_HARETZ = 4;
    public static final String ARG_PARAM_API_TYPE = "Api Type";


    @Bind(R.id.rvYnet)
    RecyclerView rvYnet;


    private NewsRecyclerViewAdapter adapter;
    private SearchView mSearchView;
    private MenuItem searchMenuItem;

    private ApiAdapter apiAdapter;
    private int mApiType;

    public NewsFragment() {
        // Required empty public constructor
    }


    public static NewsFragment newInstance(int apiType) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_API_TYPE, apiType);
        newsFragment.setArguments(args);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mApiType = getArguments().getInt(ARG_PARAM_API_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        BusProvider.getInstance().register(this);
        initApiAdapter();

        apiAdapter.getAllItems();
        setupRecyclerView();

        return view;
    }

    private void initApiAdapter() {
        switch (mApiType) {
            case API_TYPE_YNET:
                apiAdapter = new ApiAdapter(YnetRssApi.API_URL, YnetRssApi.YnetService.class);
                break;
            case API_TYPE_WALLA:
                apiAdapter = new ApiAdapter(WallaRssApi.API_URL, WallaRssApi.WallaService.class);
                break;
            case API_TYPE_HARETZ:
                apiAdapter = new ApiAdapter(HaretzRssApi.API_URL, HaretzRssApi.HaretzService.class);
                break;
        }

    }


    private void setupRecyclerView() {
        rvYnet.setLayoutManager(new LinearLayoutManager(getContext()));

        VerticalScrollSupport.addTo(rvYnet).setOnPullToRefreshListener(new VerticalScrollSupport.OnPullToRefresh() {
            @Override
            public void onPullToRefresh() {
                //appbar.setExpanded(true, true);
                apiAdapter.getAllItems();
            }
        });
        rvYnet.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(20, 0, 20, 50);
            }
        });
    }

    @Subscribe
    public void newData(RssEvent event) {
        if (adapter == null) {
            adapter = new NewsRecyclerViewAdapter(event.getRss(), getContext());
            ScaleInAnimationAdapter scaleAndAlpahAdapter = addAnimations();
            rvYnet.setAdapter(scaleAndAlpahAdapter);
        } else
            adapter.refreshData(event.getRss());

        Item item = event.getRss().getChannel().getItems().get(0);
        if (item.hasImage()) {
            String url = item.getImage();
            // Picasso.with(getContext()).load(url).into(ivHeader);
        }
    }

    @NonNull
    private ScaleInAnimationAdapter addAnimations() {
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(100);
        alphaAdapter.setInterpolator(new AnticipateOvershootInterpolator(1f));

        //ScaleAdapter + AlphaAdapter
        ScaleInAnimationAdapter scaleAndAlpahAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleAndAlpahAdapter.setDuration(250);
        scaleAndAlpahAdapter.setInterpolator(new OvershootInterpolator(.4f));
        return scaleAndAlpahAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
