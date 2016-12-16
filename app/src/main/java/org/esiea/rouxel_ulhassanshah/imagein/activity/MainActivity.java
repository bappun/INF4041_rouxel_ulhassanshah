package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.esiea.rouxel_ulhassanshah.imagein.adapter.GalleryAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private GalleryAdapter gAdapter;
    private SwipeRefreshLayout swipeContainer;
    public static JSONAsyncTask updateTask;
    private final String url = "https://api.myjson.com/bins/diwxp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.activity_main);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTask = new JSONAsyncTask();
                updateTask.execute();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        updateTask = new JSONAsyncTask();
        updateTask.execute();
        gAdapter = new GalleryAdapter(updateTask.getJArray());
            rv.setAdapter(gAdapter);
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        private JSONArray jArray;

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                try {
                    InputStream inputStream = httpEntity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line).append("\n");
                    }

                    JSONObject jObject = new JSONObject(sBuilder.toString());
                    jArray = jObject.getJSONArray("images");
                    inputStream.close();

                    if(httpResponse.getStatusLine().getStatusCode() == 200)
                        return true;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                gAdapter.clear();
                gAdapter.addAll(updateTask.getJArray());
                Toast.makeText(MainActivity.this, "Téléchargement du JSON terminé", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Téléchargement du JSON échoué", Toast.LENGTH_SHORT).show();
            }
        }

        public JSONArray getJArray() {
            return this.jArray;
        }
    }
}
