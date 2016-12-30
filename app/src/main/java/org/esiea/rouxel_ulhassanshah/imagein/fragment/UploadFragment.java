package org.esiea.rouxel_ulhassanshah.imagein.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.esiea.rouxel_ulhassanshah.imagein.service.ImageService;

public class UploadFragment extends Fragment {

    public UploadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b = (Button) getActivity().findViewById(R.id.upload_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) getActivity().findViewById(R.id.input_link);
                ImageService.startActionUpImage(getActivity(), tv.getText().toString());
            }
        });
    }
}
