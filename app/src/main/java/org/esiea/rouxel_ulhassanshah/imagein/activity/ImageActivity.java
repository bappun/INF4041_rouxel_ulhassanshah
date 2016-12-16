package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by bachi on 13/12/16.
 */

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fullscreen);

        try {
            JSONArray jArray = MainActivity.updateTask.getJArray();

            Bundle extras = getIntent().getExtras();
            int imagePosition = extras.getInt("imagePosition");

            WebView wv = (WebView) findViewById(R.id.image_fullscreen_webview);

            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            String html = "<style>body { margin: 0; background: black url('" +
                    jArray.getJSONObject(imagePosition).getString("url") +
                    "') no-repeat center center; background-size: contain; }</style>";

            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setDisplayZoomControls(false);
//            wv.getSettings().setUseWideViewPort(true);
//            wv.getSettings().setLoadWithOverviewMode(true);
            wv.loadDataWithBaseURL("", html, mimeType, encoding, "");

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
