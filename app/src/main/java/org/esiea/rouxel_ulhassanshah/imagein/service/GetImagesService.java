package org.esiea.rouxel_ulhassanshah.imagein.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.esiea.rouxel_ulhassanshah.imagein.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class GetImagesService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_ALL_IMAGES = "org.esiea.rouxel_ulhassanshah.imagein.service.action.GET_ALL_IMAGES";

    public GetImagesService() {
        super("GetImagesService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetAllImages(Context context) {
        Intent intent = new Intent(context, GetImagesService.class);
        intent.setAction(ACTION_GET_ALL_IMAGES);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_IMAGES.equals(action)) {
                handleActionGetAllImages();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetAllImages() {
        Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
        URL url;
        try {
            url = new URL("https://api.myjson.com/bins/diwxp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "images.json"));
                Log.d(TAG, "Images json downloaded!");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.IMAGES_UPDATE));
            }
        } catch (IOException e) {
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
