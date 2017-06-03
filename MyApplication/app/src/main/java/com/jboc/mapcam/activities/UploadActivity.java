package com.jboc.mapcam.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jboc.mapcam.MainActivity;
import com.jboc.mapcam.http.Config;
import com.jboc.mapcam.http.HttpConnector;
import com.jboc.ztkmk.myapplication.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class UploadActivity extends AppCompatActivity {
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private ProgressBar progressBar;
	private String filePath = null;
	private TextView txtPercentage;
	private ImageView imgPreview;
	private Button btnUpload;
	long totalSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		txtPercentage = (TextView) findViewById(R.id.txtPercentage);
		btnUpload = (Button) findViewById(R.id.btnUpload);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);

		// MainActivity에서 넘겨진 인텐트를 받아온다
		Intent i = getIntent();

		// 인텐트에서 filePath 를 받아온다.
		filePath = i.getStringExtra("filePath");

		// isImage 값을 받아와 true인지 확인한다.(안전)
		boolean isImage = i.getBooleanExtra("isImage", true);

		if (filePath != null) {
			// 찍혀진 사진 미리보기(썸네일) - 현재로는 가로로만 저장된다;;
			previewMedia(isImage);
		} else {
			// 사진 데이터가 없다.
			Toast.makeText(getApplicationContext(),
					"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
		}

		//업로드 버튼 이벤트 등록
		btnUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// uploading the file to server
				new UploadFileToServer().execute();
			}
		});

	}

	// 사진 미리보기(썸네일)
	private void previewMedia(boolean isImage) {
		// 이미지인지 체크
		if (isImage) {
			imgPreview.setVisibility(View.VISIBLE);
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// decode를 할 때 얼마만큼 줄일지 지정
            // 1보다 작은 값일때는 무조건 1로 세팅이 되며, 1보다 큰 값, n일때는 1/n 만큼 이미지를 줄임
            // 2의 지수만큼 비례 권장(2, 4, 8, 16, ....)
			// 파일 압축이 아니라 미리보기 시 보여지는 이미지를 줄이는 용도
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			imgPreview.setImageBitmap(bitmap);
		} else {
			imgPreview.setVisibility(View.GONE);
		}
	}

	// UploadFileToServer 클래스 지정
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// 프로그래스바 0으로 셋팅
			progressBar.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// 프로그래스바 뷰
			progressBar.setVisibility(View.VISIBLE);

			// 프로그래스바 값
			progressBar.setProgress(progress[0]);

			// 몇% 글자
			txtPercentage.setText(String.valueOf(progress[0]) + "%");
		}

		// 백그라운드 실행
		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {

			String responseString = null;
			try {

				HttpConnector httpConnector = new HttpConnector(Config.FILE_UPLOAD_URL);

				/**
				 * 압축하기
				 * Convert Bitmap to ByteBuffer example
				 * byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
				 for (int i = 0; i < bitmap.getWidth(); ++i) {
				 for (int j = 0; j < bitmap.getHeight(); ++j) {
				 //we're interested only in the MSB of the first byte,
				 //since the other 3 bytes are identical for B&W images
				 pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
				 }
				 }

				 * 여기서 이미지를 압축하고 byte로 변환까지 해야함
				 */
				// 압축 파일 경로 지정
				String gzFilePath = filePath + ".gz";

				// 압축 실행(원본 파일 경로, 압축 파일 경로)
				compressGzipFile(filePath, gzFilePath); // gzFilePath 경로로 gz파일 생성

				File sourceFile = new File(gzFilePath);	// gz파일 객체 생성

				int fileSize = (int) sourceFile.length();
				byte[] tempBytes = new byte[fileSize];
				try	{

					BufferedInputStream buf = new BufferedInputStream(new FileInputStream(sourceFile));
					buf.read(tempBytes, 0, tempBytes.length);
					buf.close();

				} catch (FileNotFoundException e) {

					Log.d("TAG", e.getMessage());

				} catch (IOException e) {

					Log.d("TAG", e.getMessage());
				}


				httpConnector.Post(tempBytes, responseString);

			} catch (IOException e) {

				Log.d("TAG", e.getMessage());
			}

			return responseString;
		}

		// doInbackground()가 끝나면 실행된다. responseString을 result로 리턴받는다.
		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Response from server: " + result);

			// 서버에서 받는 결과 Alert 띄우기
			showAlert(result);

			super.onPostExecute(result);
		}

	}

	// Alert 띄우는 함수
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private static void compressGzipFile(String file, String gzipFile) {
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(gzipFile);
			GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
			byte[] buffer = new byte[1024];
			int len;
			while((len=fis.read(buffer)) != -1){
				gzipOS.write(buffer, 0, len);
			}
			//close resources
			gzipOS.close();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}