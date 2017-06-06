package com.jboc.mapcam.http;

/**
 * Created by Ztkmk on 2017-06-06.
 */

//https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C http Status
public enum  HttpStatus {

    //200(성공): 서버가 요청을 제대로 처리했다는 뜻이다. 이는 주로 서버가 요청한 페이지를 제공했다는 의미로 쓰인다.
    SUCCESS(200),
    //415(지원되지 않는 미디어 유형): 요청이 요청한 페이지에서 지원하지 않는 형식으로 되어 있다.
    RESOURCE_ERROR(415);

    HttpStatus(int nValue) { this.nValue = nValue; }
    private final int nValue;

    public int GetValue() {return nValue; }
}
