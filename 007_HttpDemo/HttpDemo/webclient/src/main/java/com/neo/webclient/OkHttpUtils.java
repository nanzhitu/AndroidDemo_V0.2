package com.neo.webclient;

import android.os.Handler;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by neo-gj.ye on 2017/9/19.
 */

public class OkHttpUtils {

    private Handler mDelivery;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private static OkHttpUtils mOkHttpUtils = new OkHttpUtils();

    private OkHttpUtils() {}

    public static OkHttpUtils getInstance() {
        return mOkHttpUtils;
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * @biref sync get and return String
     *
     * @param url
     * @return Response
     */
    public String getSyncString(String url) throws IOException {

        String body = null;
        Response response = getSync(url);
        if(response.isSuccessful())
            body =  response.body().string();
        return body;
    }



    /**
     * @biref sync get
     *
     * @param url
     * @return Response
     */
    public Response getSync(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * @biref Async get
     *
     * @param url
     * @param callback
     */
    public void getAsyn(String url, Callback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    /**
     * @biref sync post and return String
     *
     * @param url
     * @param json
     * @return String
     */
    public String postSyncString(String url, String json) throws IOException
    {
        Response response = postSync(url, json);
        return response.body().string();
    }
    /**
     * @biref sync post
     *
     * @param url
     * @param json
     * @return Response
     */
    public Response postSync(String url, String json) throws IOException
    {
        Request request = buildPostRequest(url, json);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * @biref Async post
     *
     * @param url
     * @param callback
     * @param json
     */
    public  void postAsyn(String url, Callback callback, String json)
    {
        Request request = buildPostRequest(url, json);
        deliveryResult(callback, request);
    }

    private Request buildPostRequest(String url, String json)
    {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }


    /**
     * @biref Async post file
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     */
    public void postAsynFile(String url, Callback callback, File file, String fileKey) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null,null);
        deliveryResult(callback, request);
    }

    /**
     * @biref Async post file
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param json
     */
    public void postAsynFile(String url, Callback callback, File file, String fileKey,String jsonKey,String json) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey},jsonKey, json);
        deliveryResult(callback, request);
    }

    /**
     * @biref Async post file
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @param json
     */
    public void postAsynFile(String url, Callback callback, File[] files, String[] fileKeys,String jsonKey, String json) throws IOException
    {
        Request request = buildMultipartFormRequest(url, files, fileKeys,jsonKey, json);
        deliveryResult(callback, request);
    }
    private void deliveryResult(Callback callback, Request request)
    {
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, String jsonKey,String json)
    {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        if(jsonKey != null && json != null) {
            builder.addFormDataPart(jsonKey, json);
        }

        if (files != null)
        {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                Log.d("All","filekey is :   "+ fileKeys[i]+ "fileName is:" + fileName);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
                //builder.addFormDataPart(fileKeys[i],fileName,fileBody); //equal  builder.addPart(Headers.of())
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        Log.d("contentTypeFor","contentTypeFor is :"+contentTypeFor);
        return contentTypeFor;
    }

}
