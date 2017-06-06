package com.jboc.mapcam.mapactivity;

import android.graphics.Point;
import android.location.Location;
import android.os.Message;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ztkmk on 2017-05-27.
 */

public class MapClient implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private final int CAMERA_UPDATE_MIN_TIME = 1000;

    private GoogleMap googleMap;
    private PositionInfo positionInfo;

    private Long lastClickedTime;
    private final MapFragment mapFragment;

    private final MapAction mapAction;
    private final MapImageProcessHandler mapImageProcessHandler;

    public MapClient(LatLng latLng, MapFragment mapFragment, TestLoadImageClass testLoadImageClass) {

        positionInfo = new PositionInfo(latLng);
        lastClickedTime = (new Date()).getTime();
        this.mapFragment = mapFragment;
        this.mapFragment.getMapAsync(this);

        mapAction = new MapAction();
        mapImageProcessHandler = new MapImageProcessHandler(mapAction, testLoadImageClass);

        InitImageFromServer();
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        googleMap = map;
        mapAction.SetGoogleMap(googleMap);
        MoveCamera();
    }

    public void LocationChanged(final Location location) {

        positionInfo.SetLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        MoveCamera();
        //UpdateImage(positionInfo.GetLatLng());
    }

    private void MoveCamera() {

        //the desired zoom level, in the range of 2.0 to 21.0.
        //Values below this range are set to 2.0, and values above it are set to 21.0
        //Increase the value to zoom in. Not all areas have titles at the largest zoom levels.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionInfo.GetLatLng(), 10.0f));
    }

    @Override
    public void onCameraMove() {

        Long timeDiff = (new Date()).getTime() - lastClickedTime;
        //잦은 이동에 항상 업데이트 안 하도록 시간 설정
        if (timeDiff < CAMERA_UPDATE_MIN_TIME) {

            return;
        }

        UpdateImage(googleMap.getCameraPosition().target);
    }

    private void UpdateImage(LatLng latLng) {

        Message message = new Message();
        message.what = MapAction.DEFAULT_HANDLER_MESSAGE;
        message.obj = latLng;
        mapAction.sendMessage(message);
    }

    private void InitImageFromServer() {

        //TODO - Change Here Get From Server By Http
        Message message = new Message();
        message.obj= positionInfo;
        message.what = MapImageProcessHandler.GET_IMAGE;

        mapImageProcessHandler.sendMessage(message);
    }
}
