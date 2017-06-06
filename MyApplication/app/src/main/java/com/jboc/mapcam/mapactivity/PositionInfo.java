package com.jboc.mapcam.mapactivity;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ztkmk on 2017-06-06.
 */

public class PositionInfo {

    private LatLng latLng;
    private Point point;

    public PositionInfo(LatLng latLng) {

        this.latLng = latLng;
        point = TestLoadImageClass.CalcAreaByLatLng(latLng);
    }

    public LatLng GetLatLng() { return latLng; }
    public Point GetPoint() { return point; }

    public void SetLatLng(LatLng latLng) { this.latLng = latLng; }
    public void SetPoint(int x, int y) { this.point.set(x, y); }
}
