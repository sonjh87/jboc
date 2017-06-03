package com.jboc.mapcam.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jboc.ztkmk.myapplication.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ztkmk on 2017-05-24.
 */

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {

    static int REQUEST_PICTURE=1;
    static int REQUEST_PHOTO_ALBUM=2;

    //카메라찍어 올릴경우 첫 기본이미지
    static String SAMPLEIMG="";

    private TextView mView;
    ImageView iv;
    Dialog dialog;

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        //여기에 일단 기본적인 이미지파일 하나를 가져온다.
        iv=(ImageView) findViewById(R.id.imgView);

        //사진올리기 버튼클릭
        findViewById(R.id.getCustom).setOnClickListener(this);

        //GoogleMap Activity 불러오기 위한 작업
        constraintLayout = (ConstraintLayout) findViewById(R.id.albumlayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(AlbumActivity.this, GoogleMapActivity.class);
                    startActivity(intent);
                    AlbumActivity.this.finish();

                } catch (Exception e){

                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v){

        //사진올리기 버튼클릭했을때
        if(v.getId()==R.id.getCustom){

            //다이어로그 빌더를 먼저만들어낸다.
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            //현재 빌더에 레이아웃뷰 설정
            View customLayout=View.inflate(this,R.layout.button_pic,null);
            builder.setView(customLayout);
            //다이어로그의 버튼에  카메라와 사진앨범의 클릭 기능
            customLayout.findViewById(R.id.camera).setOnClickListener(this);
            customLayout.findViewById(R.id.photoAlbum).setOnClickListener(this);

            //다이어로그 만들고 보여주기
            dialog=builder.create();
            dialog.show();

            //Intent intent = new Intent(this, GoogleMapActivity.class);
            //startActivity(intent);
            //AlbumActivity.this.finish();

        }else if(v.getId()==R.id.camera){

            dialog.dismiss();
            takePicture();

        }else if(v.getId()==R.id.photoAlbum){

            dialog.dismiss();
            photoAlbum();

        }
    }

    void takePicture() {

        //사진을 찍는 인텐트를 가져온다. 그인텐트는 MediaStore에있는
        //ACTION_IMAGE_CAPTURE를 활용해서 가져온다.
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //그후 파일을 지정해야하는데 File의 앞부분 매개변수에는 파일의 절대경로를 붙여야
        //한다. 그러나 직접 경로를 써넣으면 sdcard접근이 안되므로 ,
        //Environment.getExternalStorageDirectory()로 접근해서 경로를 가져오고 두번째
        //매개 변수에 파일이름을 넣어서 활용 하도록하자!!
        File file=new File(Environment.getExternalStorageDirectory(),"");

        //그다음에 사진을 찍을대 그파일을 현재 우리가 갖고있는 SAMPLEIMG로 저장해야
        //한다. 그래서 경로를 putExtra를 이용해서 넣도록 한다. 파일형태로 말이다.
        //그리고 실제로 이파일이 가리키는 경로는 /mnt/sdcard/ic_launcher)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        //그럼이제사진찍기 인텐트를 불러온다.
        startActivityForResult(intent,REQUEST_PICTURE);

    }

    void photoAlbum() {

        //sdcard의 사진목록 선택할수 있게 인텐트
        Intent intent=new Intent(Intent.ACTION_PICK);

        // 선택사진 타입 , URI
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,REQUEST_PHOTO_ALBUM);

    }

    Bitmap loadPicture(){

//사진찍은 것을 로드 해오는데 사이즈를 조절하도록하자!!일단 파일을 가져오고
        File file=new File(Environment.getExternalStorageDirectory(),SAMPLEIMG);

//현재사진찍은 것을 조절하구이해서 조절하는 클래스를 만들자.
        BitmapFactory.Options options=new BitmapFactory.Options();

//이제 사이즈를 설정한다.
        options.inSampleSize=4;

//그후에 사진 조정한것을 다시 돌려보낸다.
        return BitmapFactory.decodeFile(file.getAbsolutePath(),options);

    }

    //선택된사진(보낸인텐트) 처리부분
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){

            if(requestCode==REQUEST_PICTURE){

                //사진을 찍은경우 그사진을 로드해온다.
                iv.setImageBitmap(loadPicture());
            }

            if(requestCode==REQUEST_PHOTO_ALBUM){

                //앨범에서 호출한경우 data는 이전인텐트(사진갤러리)에서 선택한 영역을 가져오게된다.

                // 선택사진 재설정
                iv.setImageURI(data.getData());

                ///////안되는부분.....................
                // String filename = Environment.getExternalStorageDirectory().getPath();
                String filename = getPathFromUri(data.getData());
                //mView.setText(filename);

                mView = (TextView) findViewById(R.id.textView);

                String s = "짜증난다.";

                //String filename = getPathFromUri(data.getData());
                //mView.setText(filename);
                //mView.setText(filename);

                try {
                    mView.setText(s);

                    ExifInterface exif = new ExifInterface(filename);

                    mView.setText(exif.getAttribute(ExifInterface.TAG_DATETIME));

                    //showExif(exif);

                } catch (IOException e) {

                    e.printStackTrace();

                    Toast.makeText(this, "캐치!", Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    private void showExif(ExifInterface exif) {

        String myAttribute = "[Exif information] \n\n";

        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif);
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif);
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif);

        mView.setText(myAttribute);

    }

    private String getTagString(String tag, ExifInterface exif) {

        return (tag + " : " + exif.getAttribute(tag) + "\n");

    }

    public String getPathFromUri(Uri uri){

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;

    }
}
