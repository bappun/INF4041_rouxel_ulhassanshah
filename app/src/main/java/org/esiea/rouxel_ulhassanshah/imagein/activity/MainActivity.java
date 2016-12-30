package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.esiea.rouxel_ulhassanshah.imagein.adapter.GalleryAdapter;
import org.esiea.rouxel_ulhassanshah.imagein.service.GetImagesService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private StaggeredGridLayoutManager lm;
    private GalleryAdapter gAdapter;
    private RecyclerView rv ;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetImagesService.startActionGetAllImages(MainActivity.this);
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        rv = (RecyclerView) findViewById(R.id.recycler_view);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int storedPreference = Integer.parseInt(preferences.getString("pref_row_count", "2"));
            lm = new StaggeredGridLayoutManager(storedPreference, GridLayoutManager.VERTICAL);
        }
        else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int storedPreference = Integer.parseInt(preferences.getString("pref_row_count_land", "4"));
            lm = new StaggeredGridLayoutManager(storedPreference, GridLayoutManager.VERTICAL);
        }
        rv.setLayoutManager(lm);

        gAdapter = new GalleryAdapter();
        rv.setAdapter(gAdapter);

        GetImagesService.startActionGetAllImages(this);
        IntentFilter intentFilter = new IntentFilter(IMAGES_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ImagesUpdate(), intentFilter);

        IntentFilter intentFilterPref = new IntentFilter(PREFS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PrefUpdate(), intentFilterPref);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Context context = MainActivity.this;
                Intent openSettings = new Intent(context, SettingsActivity.class);
                context.startActivity(openSettings);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int storedPreference = Integer.parseInt(preferences.getString("pref_row_count", "2"));
            lm.setSpanCount(storedPreference);
        }
        else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int storedPreference = Integer.parseInt(preferences.getString("pref_row_count_land", "2"));
            lm.setSpanCount(storedPreference);
        }
    }

    public JSONArray getJArrayFromAdapter() {
        return this.gAdapter.getJArray();
    }

    public static final String IMAGES_UPDATE = "IMAGES_UPDATE";
    public class ImagesUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            gAdapter.setData(getImagesFromFile());
            if (intent.getAction().equals(IMAGES_UPDATE))
                Toast.makeText(MainActivity.this, R.string.refresh_success, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, R.string.refresh_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static final String PREFS_UPDATE = "PREFS_UPDATE";
    public class PrefUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            onConfigurationChanged(getResources().getConfiguration());
        }
    }

    public JSONArray getImagesFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "images.json");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();
            return new JSONObject(new String(buf, "UTF-8")).getJSONArray("images");
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
