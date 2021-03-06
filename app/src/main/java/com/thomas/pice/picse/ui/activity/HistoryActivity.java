package com.thomas.pice.picse.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.thomas.pice.picse.R;
import com.thomas.pice.picse.data.dao.SearchDao;
import com.thomas.pice.picse.ui.adapter.SearchHistoryAdapter;
import io.realm.Realm;

/**
 * Created by Thomas on 13/04/2017.
 *
 * Activity to show the history of searches
 */

public class HistoryActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_searches);
        emptyView = (TextView) findViewById(R.id.empty_view);

        setupToolbar();

        //Open connection to the database
        realm = Realm.getDefaultInstance();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Start Search local data manager
        SearchDao dao = new SearchDao(realm);
        mAdapter = new SearchHistoryAdapter(HistoryActivity.this, dao.getAllSearches());

        // Manage data display
        if (mAdapter.getItemCount() > 0) {
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        //Enable back to main activity
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.setResult(Activity.RESULT_CANCELED, new Intent());
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();

    }
}
