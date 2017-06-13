package com.jboc.mapcam;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ztkmk on 2017-06-12.
 */

public class ImageAdapter extends ArrayAdapter<Bitmap> {

    private Context context;

    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    public ImageAdapter(Context context) {

        super(context, 0);

        this.context = context;
    }

    @Override
    public int getCount() {
        return bitmapArrayList.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return bitmapArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bitmapArrayList.get(bitmapArrayList.size() - position - 1));
        //imageView.setImageResource(imageIdList.size() - position);
        return imageView;
    }

    public void AddImageToList(Bitmap bitmap) {

        if (bitmap == null)
            return;

        bitmapArrayList.add(bitmap);
    }
}
