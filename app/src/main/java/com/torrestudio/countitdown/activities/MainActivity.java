/**
 *
 *  Author: P. Torres
 *  Last Modified: 10/30/18
 */

package com.torrestudio.countitdown.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.torrestudio.countitdown.R;
import com.torrestudio.countitdown.controllers.EventDataController;
import com.torrestudio.countitdown.entities.Category;
import com.torrestudio.countitdown.entities.Event;
import com.torrestudio.countitdown.entities.EventAdapter;
import com.torrestudio.countitdown.fragments.SortByDialogFragment;
import com.torrestudio.countitdown.interfaces.EventDataSubscriber;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, EventDataSubscriber,
        SortByDialogFragment.OnSortOptionSelected {

    // Member views of this activity
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addFabButton;

    private EventDataController mDataController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mDataController = EventDataController.initController(this);
        EventDataController.subscribe(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartUI();
    }

    private void initViews() {
        setUpNavDrawer();
        setUpToolbar();
        setUpFab();
        setUpRecyclerView();
    }


    private void setUpNavDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpToolbar() {
        mToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle(getString(R.string.category_all_events));
    }

    private void setUpFab() {
        addFabButton = findViewById(R.id.addFabButton);
        addFabButton.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.events_recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void restartUI() {
        setToolbarTitle(getString(R.string.category_all_events));
        mDataController.filterEventsByCategory(Category.ALL_EVENTS);
        mAdapter.notifyDataSetChanged();
    }

    private void setToolbarTitle(String newName) {
        getSupportActionBar().setTitle(newName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFabButton:
                Intent activityIntent = new Intent(this, CreateEventActivity.class);
                startActivity(activityIntent);
                break;
        }
    }

    @Override
    public void onEventCreated(Event e) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSortOptionSelected(int sortOption) {
        mDataController.sortEvents(sortOption);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventDataLoaded() {
        runOnUiThread(() -> updateUI());
    }

    private void updateUI() {
        mAdapter = new EventAdapter(this, mDataController.getEvents());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_menu, menu);
        setUpSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_sort_by:
                SortByDialogFragment sortByDialogFragment = new SortByDialogFragment();
                sortByDialogFragment.show(getSupportFragmentManager(), "sortByDialog");
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        String newToolbarName = "";
        Category categoryToFilterBy = null;

        switch (item.getItemId()) {
            case R.id.nav_all_events:
                newToolbarName = getString(R.string.category_all_events);
                categoryToFilterBy = Category.ALL_EVENTS;
                break;
            case R.id.nav_past_events:
                newToolbarName = getString(R.string.category_past_events);
                categoryToFilterBy = Category.PAST_EVENTS;
                break;
            case R.id.nav_business:
                newToolbarName = getString(R.string.category_business);
                categoryToFilterBy = Category.BUSINESS;
                break;
            case R.id.nav_education:
                newToolbarName = getString(R.string.category_education);
                categoryToFilterBy = Category.EDUCATION;
                break;
            case R.id.nav_leisure:
                newToolbarName = getString(R.string.category_leisure);
                categoryToFilterBy = Category.LEISURE;
                break;
            case R.id.nav_special:
                newToolbarName = getString(R.string.category_special);
                categoryToFilterBy = Category.SPECIAL;
                break;
            case R.id.nav_sports:
                newToolbarName = getString(R.string.category_sports);
                categoryToFilterBy = Category.SPORTS;
                break;
        }
        setToolbarTitle(newToolbarName);
        mDataController.filterEventsByCategory(categoryToFilterBy);
        mAdapter.notifyDataSetChanged();
        return true;
    }


}
