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

    private LatLng minLatLng;
    private LatLng maxLatLng;

    private Point rangeLat = new Point();
    private Point rangeLng = new Point();

    private boolean bInit = false;

    final private double range = 2d;
    private static TestLoadImageClass instance;// = new TestLoadImageClass();
    final private HashMap<Point, ArrayList<ImageInfo>> imageHashMap = new HashMap<>();

    private TestLoadImageClass() {}
    public static TestLoadImageClass GetInstance() { return instance; }

    public void Init(LatLng latLng, Resources resources, int nImageId) {

        if (bInit)
            return;

        bInit = true;

        //TODO - Between Min and Max Have Some Problem.
        minLatLng = new LatLng(latLng.latitude - range, latLng.longitude - range);
        maxLatLng = new LatLng(latLng.latitude + range, latLng.longitude + range);

        rangeLat.set(
                (int)((double) -180 / separateRange),
                (int)((double) 180 / separateRange));

        rangeLng.set(
                (int)((double) -90 / separateRange),
                (int)((double) 90 / separateRange));


        Bitmap bitmap = BitmapFactory.decodeResource(resources, nImageId);
        for (int i = 0; i < 100; i++) {

            double latitude = ThreadLocalRandom.current().nextDouble(minLatLng.latitude, maxLatLng.latitude);
            double longitude = ThreadLocalRandom.current().nextDouble(minLatLng.longitude, maxLatLng.longitude);
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

    public void AddImage(Bitmap bitmap, LatLng latLng) {


    }

    /**
     * Separate the Area By 0.1d of LatLng
     */
    final static private double separateRange = 1d;
    public static Point  CalcAreaByLatLng(LatLng latLng) {

        int nLatIndex = (int)(latLng.latitude / separateRange);
        int nLngIndex = (int)(latLng.longitude / separateRange);

        return new Point(nLatIndex, nLngIndex);
    }

    /**
     * 5 is Same Point
     */
    public static int IsSameArea(LatLng latLng, Point point) {

        int rangeX = (int)(latLng.longitude / separateRange);
        int rangeY = (int)(latLng.latitude / separateRange);

        int sameX = (rangeX > point.x) ? 1 : ((rangeX == point.x) ? 0 : -1);
        int sameY = (rangeY > point.y) ? 1 : ((rangeY == point.y) ? 0 : -1);

        return (5 + sameX + sameY * 3);
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
