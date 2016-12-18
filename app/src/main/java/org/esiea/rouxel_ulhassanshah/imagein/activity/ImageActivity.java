package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bachi on 13/12/16.
 */

public class ImageActivity extends AppCompatActivity {

    private FloatingActionButton share;
    private FloatingActionButton download;
    private String imageURL;

    private final int WRITE_PERM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fullscreen);

        try {
            JSONArray jArray = MainActivity.updateTask.getJArray();

            Bundle extras = getIntent().getExtras();
            int imagePosition = extras.getInt("imagePosition");

            WebView wv = (WebView) findViewById(R.id.image_fullscreen_webview);

            imageURL = jArray.getJSONObject(imagePosition).getString("url");
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            String html = "<style>body { margin: 0; background: black url('" +
                    imageURL +
                    "') no-repeat center center; background-size: contain; }</style>";

            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setDisplayZoomControls(false);

            wv.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    findViewById(R.id.progress).setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    findViewById(R.id.progress).setVisibility(View.GONE);
                }
            });

            wv.loadDataWithBaseURL("", html, mimeType, encoding, "");

            share = (FloatingActionButton) findViewById(R.id.fab_share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Uri> imageUris = new ArrayList<>();

                    Uri.Builder b = Uri.parse(imageURL).buildUpon();
                    imageUris.add(b.build());

                    Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.setType("image/*");

                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));

                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
                }
            });

            download = (FloatingActionButton) findViewById(R.id.fab_download);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(ImageActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(ImageActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        WRITE_PERM);
                        } else {
                            new ImageDownloadAsyncTask().execute();
                        }
                    } else {
                        new ImageDownloadAsyncTask().execute();
                    }
                }
            });

            wv.setOnTouchListener(new View.OnTouchListener() {
                private final float MOVE_THRESHOLD_DP = 20 * getResources().getDisplayMetrics().density;

                private boolean mMoveOccurred;
                private float mDownPosX;
                private float mDownPosY;

                private boolean visible = true;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final int action = motionEvent.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            mMoveOccurred = false;
                            mDownPosX = motionEvent.getX();
                            mDownPosY = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!mMoveOccurred) {
                                if (visible) {
                                    share.hide();
                                    download.hide();
                                    visible = false;
                                } else {
                                    share.show();
                                    download.show();
                                    visible = true;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (Math.abs(motionEvent.getX() - mDownPosX) > MOVE_THRESHOLD_DP ||
                                    Math.abs(motionEvent.getY() - mDownPosY) > MOVE_THRESHOLD_DP) {
                                mMoveOccurred = true;
                            }
                            break;
                    }
                    return false;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case WRITE_PERM: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ImageDownloadAsyncTask().execute();
                } else {
                    Toast.makeText(ImageActivity.this, R.string.write_perm_denied_msg, Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
    }

    class ImageDownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ImageActivity.this);
            dialog.setTitle(getString(R.string.image_download_dialog_title));
            dialog.setMessage(getString(R.string.image_download_dialog_text));
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                URL imURL = new URL(imageURL);

                InputStream is = new BufferedInputStream(imURL.openStream());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File dir = new File(root + "/ImageIn");

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                String date = format.format(new Date());
                String name = date + ".jpg";
                File file = new File(dir, name);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                out.flush();
                out.close();

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());

                ContentResolver cr = getContentResolver();
                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.coordinator_layout), R.string.download_completed_snackbar, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
