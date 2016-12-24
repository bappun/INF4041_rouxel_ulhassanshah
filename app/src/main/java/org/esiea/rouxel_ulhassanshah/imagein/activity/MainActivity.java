package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lm = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        }
        else {
            lm = new StaggeredGridLayoutManager(4, GridLayoutManager.VERTICAL);
        }
        rv.setLayoutManager(lm);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.activity_main);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetImagesService.startActionGetAllImages(MainActivity.this);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        gAdapter = new GalleryAdapter();
        rv.setAdapter(gAdapter);

        GetImagesService.startActionGetAllImages(this);
        IntentFilter intentFilter = new IntentFilter(IMAGES_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ImagesUpdate(), intentFilter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lm.setSpanCount(2);
        }
        else {
            lm.setSpanCount(4);
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
            if (true)
                Toast.makeText(MainActivity.this, R.string.refresh_success, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, R.string.refresh_error, Toast.LENGTH_SHORT).show();
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
