package com.example.ztkmk.myapplication.http;

public class Config {
	// File upload url : localhost 나 127.0.0.1로 하면 refused 됨. cmd창에서 ipconfig로 ip 주소 알아내어 변경하자.
	public static final String FILE_UPLOAD_URL = "http://192.168.219.102/AndroidFileUpload/fileUpload.php";
	
	// Directory name
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
}
