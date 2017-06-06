package com.jboc.mapcam.mapactivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ztkmk on 2017-06-06.
 */

public class TestLoadImageClass {

    //latitude range (-90, 90)
    //longitude range (-180, 180)
    double dMinX = 0d;
    double dMaxX = 0d;
    double dMinY = 0d;
    double dMaxY = 0d;

    private Point rangeLat = new Point();
    private Point rangeLng = new Point();

    final double range = 10d;

    final private HashMap<Point, ArrayList<ImageInfo>> imageHashMap = new HashMap<>();

    public TestLoadImageClass(LatLng latLng, Resources resources, int nImageId) {

        dMinX = latLng.longitude - range;
        if (dMinX < -180) dMinX += 360;

        dMaxX = latLng.longitude + range;
        if (dMaxX > 180) dMaxX -= 360d;

        dMinY = latLng.latitude - range;
        if (dMinY < -90) dMinY += 180d;

        dMaxY = latLng.latitude + range;
        if (dMaxY > 90) dMaxY -= 180d;

        rangeLat.set(
                (int)((double) -180 / separateRange),
                (int)((double) 180 / separateRange));

        rangeLng.set(
                (int)((double) -90 / separateRange),
                (int)((double) 90 / separateRange));


        Bitmap bitmap = BitmapFactory.decodeResource(resources, nImageId);
        for (int i = 0; i < 100; i++) {

            double latitude = ThreadLocalRandom.current().nextDouble(dMinY, dMaxY);
            double longitude = ThreadLocalRandom.current().nextDouble(dMinX, dMaxX);
            LatLng imageLatLng = new LatLng(latitude, longitude);
            ImageInfo info = new ImageInfo(bitmap, imageLatLng);
            Point point = CalcAreaByLatLng(imageLatLng);

            if(imageHashMap.containsKey(point)) {

                ArrayList<ImageInfo> arrayList = imageHashMap.get(point);
                arrayList.add(info);

            } else {

                ArrayList<ImageInfo> arrayList = new ArrayList<>();
                arrayList.add(info);
                imageHashMap.put(point, arrayList);
            }
        }
    }

    public Point[] GetAccessiblePoint(LatLng latLng) {

        Point point = CalcAreaByLatLng(latLng);

        return GetAdjacentPoints(point);
    }

    public ArrayList<ImageInfo> GetArrayImageByPoint(Point point) {

        if (imageHashMap.containsKey(point)) {

            ArrayList<ImageInfo> arrayList = imageHashMap.get(point);
            return arrayList;

        } else {

            return null;
        }
    }

    /**
     * Separate the Area By 0.1d of LatLng
     */
    final private double separateRange = 0.1d;
    private Point CalcAreaByLatLng(LatLng latLng) {

        int rangeX = (int)(latLng.longitude / separateRange);
        int rangeY = (int)(latLng.latitude / separateRange);

        return new Point(rangeX, rangeY);
    }

    /**
     * 00 01 02
     * 03 04 05
     * 06 07 08
     */
    private Point[] GetAdjacentPoints(Point point) {

        //new point array 9 or 6 or 4 ===> new 9 and set nothing if impossible
        Point[] points = new Point[9];
        points[4] = point;

        boolean bN = (point.y + 1 <= rangeLng.y);
        boolean bE = (point.x + 1 <= rangeLat.y);
        boolean bS = (point.y - 1 >= rangeLng.x);
        boolean bW = (point.x - 1 >= rangeLat.x);

        if (bN) points[1] = new Point(point.x, point.y + 1);
        if (bE) points[5] = new Point(point.x + 1, point.y);
        if (bS) points[7] = new Point(point.x, point.y - 1);
        if (bW) points[3] = new Point(point.x - 1, point.y);

        if (bN && bE)  points[2] = new Point(point.x + 1, point.y + 1);
        if (bN && bW)  points[0] = new Point(point.x - 1, point.y + 1);
        if (bS && bE)  points[8] = new Point(point.x + 1, point.y - 1);
        if (bS && bW)  points[6] = new Point(point.x - 1, point.y - 1);

        return points;
    }
}
