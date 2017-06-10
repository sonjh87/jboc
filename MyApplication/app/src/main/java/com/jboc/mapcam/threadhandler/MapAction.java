package com.jboc.mapcam.threadhandler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jboc.mapcam.mapactivity.ImageInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ztkmk on 2017-05-27.
 */

public class MapAction extends Handler {

    public static final int DEFAULT_HANDLER_MESSAGE = 1;
    public static final int CHECK_USER_LATLNG = 2;
    public static final int SHOW_IMAGE = 3;

    private static MapAction instance;// = new MapAction();
    private MapAction() {}
    public static MapAction GetInstance() { return instance; }

    private GoogleMap googleMap;
    private final ConcurrentHashMap<LatLng, Bitmap> userImageHashMap
            = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<LatLng, Marker> mapMarkerMap
            = new ConcurrentHashMap<>();

    public void SetGoogleMap(GoogleMap map) {

        if (googleMap == null) {

            googleMap = map;

        } else {

            Log.d("TAG", "Duplication Request");
        }
    }

    /**
     * 현재 화면 중심 기준으로 주위에 있는 모든 사진 위치를 변경시키고 새로운 장소를 업데이트 해온다?
     */

    @Override
    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        switch (msg.what) {
            case DEFAULT_HANDLER_MESSAGE: {

                LatLng latLng = (LatLng) msg.obj;
                break;
            }

            case SHOW_IMAGE: {

                ImageInfo imageInfo = (ImageInfo) msg.obj;
                AddNewIamageMarkerToGoogleMap(imageInfo.GetLatLng(), imageInfo.GetBitmap());

                break;
            }

            default:
                break;
        }
    }

    private void AddNewIamageMarkerToGoogleMap(LatLng latLng, Bitmap bitmap) {

        //좌료로만 체크한다 일단
        if (mapMarkerMap.contains(latLng))
            return;

        MarkerOptions options = new MarkerOptions();
        options
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        Marker photoMarker = googleMap.addMarker(options);
        photoMarker.setDraggable(false);

        mapMarkerMap.put(latLng, photoMarker);
    }

    private void HideImageMarker(LatLng latLng) {

        if (mapMarkerMap.contains(latLng)) {

            Marker marker = mapMarkerMap.get(latLng);
            marker.setVisible(false);
        }
    }
}
