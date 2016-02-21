package com.example.tomerbuzaglo.xmlrssdemo.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tomerbuzaglo.xmlrssdemo.R;
import com.example.tomerbuzaglo.xmlrssdemo.fragments.NewsFragment;
import com.example.tomerbuzaglo.xmlrssdemo.interfaces.OnSearchChangedListener;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.ivHeader)
    ImageView ivHeader;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.newsFragments)
    RelativeLayout newsFragments;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private SearchView mSearchView;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);
        return true;
    }


    private OnSearchChangedListener searchListener;
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            // newText is text entered by user to SearchView
            Toast.makeText(NewsActivity.this, query, Toast.LENGTH_SHORT).show();
            if (searchListener != null)
                searchListener.performSearch(query);
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ynet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.newsFragments, NewsFragment.newInstance(NewsFragment.API_TYPE_YNET)).commit();
            Picasso.with(this).load(R.drawable.cheese_1).into(ivHeader);
        } else if (id == R.id.nav_walla) {
            getSupportFragmentManager().beginTransaction().replace(R.id.newsFragments, NewsFragment.newInstance(NewsFragment.API_TYPE_WALLA)).commit();
            Picasso.with(this).load(R.drawable.cheese_2).into(ivHeader);

        } else if (id == R.id.nav_haretz) {
            getSupportFragmentManager().beginTransaction().replace(R.id.newsFragments, NewsFragment.newInstance(NewsFragment.API_TYPE_HARETZ)).commit();
            Picasso.with(this).load(R.drawable.cheese_3).into(ivHeader);
        } else if (id == R.id.nav_rotter) {
            getSupportFragmentManager().beginTransaction().replace(R.id.newsFragments, NewsFragment.newInstance(NewsFragment.API_TYPE_WALLA)).commit();
            Picasso.with(this).load(R.drawable.cheese_4).into(ivHeader);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
