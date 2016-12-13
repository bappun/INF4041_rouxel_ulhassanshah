package org.esiea.rouxel_ulhassanshah.imagein.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.esiea.rouxel_ulhassanshah.imagein.R;
import org.esiea.rouxel_ulhassanshah.imagein.activity.ImageActivity;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by bachi on 05/12/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageHolder> {

    private JSONArray gallery;

    public GalleryAdapter(JSONArray gallery) {
        this.gallery = gallery;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lif = LayoutInflater.from(parent.getContext());
        View iv = lif.inflate(R.layout.gallery_thumbnail, parent, false);

        return new ImageHolder(iv);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        try {
            String url = this.gallery.getJSONObject(position).getString("url");
            holder.bind(url, position);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.gallery.length();
    }


    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv;
        public int position;

        public ImageHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.gallery_thumbnail);
            iv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent showImageIntent = new Intent(context, ImageActivity.class);

            Bundle extras = new Bundle();
            extras.putInt("imagePosition", this.position);

            showImageIntent.putExtras(extras);
            context.startActivity(showImageIntent);
        }

        public void bind(String url, int position) {
            this.position = position;
            Glide.with(this.iv.getContext())
                    .load(url)
                    .into(this.iv);
        }
    }
}
