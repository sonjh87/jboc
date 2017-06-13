package com.jboc.mapcam;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ztkmk on 2017-06-12.
 */

public class ImageList {

    final private List<Bitmap> imageList = Collections.synchronizedList(new ArrayList<Bitmap>());

    final private static ImageList instance = new ImageList();
    private ImageList() {}
    public static ImageList GetInstance() { return instance; }

    public List<Bitmap> GetImageList() { return imageList; }

    public void AddImageToList(Bitmap bitmap) {

        imageList.add(bitmap);
    }
}
