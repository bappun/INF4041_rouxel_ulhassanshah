package org.esiea.rouxel_ulhassanshah.imagein.fragment;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.esiea.rouxel_ulhassanshah.imagein.database.MyDatabase;

public class ExtrasFragment extends Fragment implements SensorEventListener {

    private SensorManager sm;
    private Sensor acc;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private ListView lv;

    public ExtrasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_extras, container, false);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.i("shake", "yeah");
                    MyDatabase db = new MyDatabase(getContext(), "dates.db", null, 1);
                    db.open();
                    db.insertCurrentDate();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_date_item, db.getDates());
                    lv.setAdapter(adapter);
                    db.close();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);

        lv = (ListView) getActivity().findViewById(R.id.listView);

        MyDatabase db = new MyDatabase(getContext(), "dates.db", null, 1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_date_item, db.getDates());

        lv.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
