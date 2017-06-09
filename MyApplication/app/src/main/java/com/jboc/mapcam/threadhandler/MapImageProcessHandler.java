package com.jboc.mapcam.threadhandler;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;
import com.jboc.mapcam.mapactivity.ImageInfo;
import com.jboc.mapcam.mapactivity.PositionInfo;
import com.jboc.mapcam.mapactivity.TestLoadImageClass;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ztkmk on 2017-06-06.
 */

public class MapImageProcessHandler extends Handler {

    final public static int GET_IMAGE = 1;
    final public static int CHECK_LATLNG = 2;

    final private HashMap<Point, HashMap<LatLng, ImageInfo>> imageHashMap = new HashMap<>();
    final private static MapImageProcessHandler instance = new MapImageProcessHandler();
    private MapImageProcessHandler() {}
    public static MapImageProcessHandler GetInstance() { return instance; }



    @Override
    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        switch (msg.what) {

            case GET_IMAGE: {

                PositionInfo positionInfo = (PositionInfo) msg.obj;
                Point[] points = TestLoadImageClass.GetInstance().GetAccessiblePoint(positionInfo.GetLatLng());
                positionInfo.SetPoint(points[4].x, points[4].y);

                for (int i = 0; i< points.length; i++) {

                    if (points[i] == null)
                        continue;

                    HashMap<LatLng, ImageInfo> infoHashMap;
                    if (imageHashMap.containsKey(points[i])) {

                        infoHashMap = imageHashMap.get(points[i]);

                    } else {
                        infoHashMap = new HashMap<>();
                        imageHashMap.put(points[i], infoHashMap);
                    }

                    GetImageFromServer(points[i], infoHashMap);
                }

                UpdateImageByPoint(positionInfo.GetPoint());
                break;
            }

            case CHECK_LATLNG: {

                PositionInfo positionInfo = (PositionInfo) msg.obj;
                int nResult = TestLoadImageClass.IsSameArea(positionInfo.GetLatLng(), positionInfo.GetPoint());
                if (nResult == 5) {

                    // Do Nothing
                } else {

                }
                break;
            }

            default:
                break;
        }
    }

    private void GetImageFromServer(final Point point, final HashMap<LatLng, ImageInfo> infoHashMap) {

        ArrayList<ImageInfo> imageInfos = TestLoadImageClass.GetInstance().GetArrayImageByPoint(point);
        if (imageInfos == null)
            return;

        for (int j = 0; j < imageInfos.size(); j++) {

            ImageInfo info = imageInfos.get(j);
            if (infoHashMap.containsKey(info.GetLatLng())) {

                //If Duplicated change to new one
                ImageInfo imageInfo = infoHashMap.get(info.GetLatLng());
                imageInfo = info;
            } else {

                infoHashMap.put(info.GetLatLng(), info);
            }
        }
    }

    private void UpdateImageByPoint(Point point) {

        if(!imageHashMap.containsKey(point)) {

            return;
        }

        HashMap<LatLng, ImageInfo> infoHashMap = imageHashMap.get(point);
        for (HashMap.Entry<LatLng, ImageInfo> entry : infoHashMap.entrySet()) {

            Message message = new Message();
            message.what = MapAction.SHOW_IMAGE;
            message.obj = entry.getValue();

            MapAction.GetInstance().sendMessage(message);
        }
    }
}
