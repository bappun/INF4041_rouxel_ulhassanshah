package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by bachi on 13/12/16.
 */

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fullscreen);

        InputStream is = getResources().openRawResource(R.raw.images);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray jArray = json.getJSONArray("list");

            Bundle extras = getIntent().getExtras();
            int imagePosition = extras.getInt("imagePosition");

            WebView wv = (WebView) findViewById(R.id.image_fullscreen_webview);

            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            String html = "<style>body { margin: 0; background: black url(" +
                    jArray.getJSONObject(imagePosition).getString("url") +
                    ") no-repeat center center; }</style>";

            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setDisplayZoomControls(false);
            wv.loadDataWithBaseURL("", html, mimeType, encoding, "");

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
