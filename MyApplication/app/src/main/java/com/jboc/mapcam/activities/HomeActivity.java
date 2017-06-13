package com.jboc.mapcam.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.jboc.mapcam.ImageAdapter;
import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.R;
import com.jboc.mapcam.googleservice.GpsCallbackInterface;
import com.jboc.mapcam.googleservice.GpsClient;
import com.jboc.mapcam.http.RestHttpClient;
import com.jboc.mapcam.threadhandler.HomeProcessHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ztkmk on 2017-06-04.
 */

public class HomeActivity extends Activity {

    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SetLayout();

        GetMyImageFromServer();
    }

    private void GetMyImageFromServer() {

        //TODO - Get Lat and Lng From GpsCLient When we send to server
        //GpsClient.GetInstance().GetLastLatLng();
        //String url = String.format("image_list/my/get?latitude=%d&longitude=%d", latLng.latitude, latLng.longitude);
        String url = String.format("image_list/my/");
        RestHttpClient.Get(url, new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JSONArray jsonArray;
                try {

                    String response = new String(responseBody);
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {

                    e.printStackTrace();
                    return;
                }

                for(int i=0; i < jsonArray.length(); i++){
                    try {
                        Integer jObject = (Integer)jsonArray.getInt(0);  // JSONObject 추출
                        Message msg = new Message();
                        msg.what = HomeProcessHandler.GET_IMAGE_FROM_SERVER;
                        msg.obj = new Long(jObject);
                        HomeProcessHandler.GetInstance().sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                int errorCode = statusCode;

                for (int i = 0; i < 1000; i++) {

                    Message msg = new Message();
                    msg.what = HomeProcessHandler.GET_IMAGE_FROM_SERVER;
                    msg.obj = i;
                    HomeProcessHandler.GetInstance().sendMessage(msg);
                }
            }
        });
    }

    private void SetLayout() {

        HomeProcessHandler.GetInstance().SetHomeActivity(this);
        setContentView(R.layout.activity_home);
        SetGridView();
        MainActivity.SetButton(HomeActivity.this, R.id.home_button, R.id.album_button, R.id.map_button);
    }

    private void SetGridView() {

        gridView = (GridView) findViewById(R.id.image_grid_view);
        imageAdapter = new ImageAdapter(HomeActivity.this);
        gridView.setAdapter(imageAdapter);

        UpdateGridView(MainActivity.icon);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(HomeActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateGridView(Bitmap bitmap) {

        imageAdapter.notifyDataSetChanged();
        imageAdapter.AddImageToList(bitmap);
    }
}
