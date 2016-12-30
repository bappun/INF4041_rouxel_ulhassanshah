package org.esiea.rouxel_ulhassanshah.imagein.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.esiea.rouxel_ulhassanshah.imagein.activity.MainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class ImageService extends IntentService {

    private static final String ACTION_GET_ALL_IMAGES = "org.esiea.rouxel_ulhassanshah.imagein.service.action.GET_ALL_IMAGES";
    private static final String ACTION_UP_IMAGE = "org.esiea.rouxel_ulhassanshah.imagein.service.action.UP_IMAGE";

    private static final String JSON_URL = "https://api.myjson.com/bins/diwxp";

    public ImageService() {
        super("ImageService");
    }

    public static void startActionGetAllImages(Context context) {
        Intent intent = new Intent(context, ImageService.class);
        intent.setAction(ACTION_GET_ALL_IMAGES);
        context.startService(intent);
    }

    public static void startActionUpImage(Context context, String url) {
        Intent intent = new Intent(context, ImageService.class);
        intent.setAction(ACTION_UP_IMAGE);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_IMAGES.equals(action)) {
                handleActionGetAllImages();
            } else if (ACTION_UP_IMAGE.equals(action)) {
                handleActionUpImage(intent.getExtras().getString("url"));
            }
        }
    }

    private void handleActionGetAllImages() {
        Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
        URL url;
        try {
            url = new URL(JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "images.json"));
                Log.d(TAG, "Images json downloaded!");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.IMAGES_DOWNLOADED));
            } else {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.IMAGES_DOWNLOAD_FAILED));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleActionUpImage(String urlToAdd) {
        Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
        URL url;
        try {
            JSONObject newUrl = new JSONObject();
            newUrl.put("url", urlToAdd);

            String json;

            InputStream is = new FileInputStream(getCacheDir() + "/" + "images.json");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();

            JSONArray jArray = new JSONArray(new String(buf, "UTF-8"));
            jArray.put(newUrl);

            json = jArray.toString();
            Log.i("json", json);

            url = new URL(JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            conn.connect();

            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                Log.d(TAG, "Image uploaded!");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.IMAGE_UPLOADED));
            } else {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.IMAGE_UPLOAD_FAILED));
            }

            conn.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
