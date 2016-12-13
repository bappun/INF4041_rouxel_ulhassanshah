package org.esiea.rouxel_ulhassanshah.imagein.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.esiea.rouxel_ulhassanshah.imagein.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goToMainActivity();
                        }
                    });

                } catch(Exception pokemon) {

                }
            }
        }.start();
    }

    protected void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}