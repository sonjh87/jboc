package com.jboc.mapcam.mapactivity;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ztkmk on 2017-06-06.
 */

public class ImageInfo {

    final private Bitmap bitmap;
    final private LatLng latLng;

    public ImageInfo(Bitmap bitmap, LatLng latLng) {

        this.bitmap = bitmap;
        this.latLng = latLng;
    }
}
