package com.example.stegano;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageInfoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageInfo> images;

    public ImageInfoAdapter(Context context, ArrayList<ImageInfo> images){
        super();
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public Object getItem(int position) {
        return this.images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);

        final Context context = this.context;
        final ViewGroup parentF = parent;

        View v = inflater.inflate(R.layout.image_item_layout, parent, false);
        ImageView imageView = v.findViewById(R.id.imageView);
        TextView textView = v.findViewById(R.id.url_text);
        TextView descriptionTextView = v.findViewById(R.id.description_text);

        ImageInfo imageInfo = (ImageInfo)getItem(position);

        imageView.setImageBitmap(imageInfo.getImage());

        textView.setText(imageInfo.getUrl());

        descriptionTextView.setText(imageInfo.getDescription());

        if(position % 2 == 1)
            v.setBackgroundColor(Color.rgb(184, 104, 147));

        return v;
    }
}
