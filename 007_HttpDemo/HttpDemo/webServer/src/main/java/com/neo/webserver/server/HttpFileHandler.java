package com.neo.webserver.server;


import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.neo.webserver.data.EventBean;
import com.neo.webserver.utils.JsonUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;


public class HttpFileHandler implements HttpRequestHandler {
    private final String TAG = "HttpFileHandler";
    private String webRoot;
    private Handler handler = null;

    private static final String HTTP_OK = "200 OK";
    private static final String HTTP_PARTIALCONTENT = "206 Partial Content";
    private static final String HTTP_RANGE_NOT_SATISFIABLE = "416 Requested Range Not Satisfiable";
    private static final String HTTP_REDIRECT = "301 Moved Permanently";
    private static final String HTTP_FORBIDDEN = "403 Forbidden";
    private static final String HTTP_NOTFOUND = "404 Not Found";
    private static final String HTTP_BADREQUEST = "400 Bad Request";
    private static final String HTTP_INTERNALERROR = "500 Internal Server Error";
    private static final String HTTP_NOTIMPLEMENTED = "501 Not Implemented";
    private static final String HTTP_BADREQUEST2 = "400-2 Bad Request, url is null";
    private int mPageNumber;
    private int mInfoNumber;
    private int mContent;
    private boolean mInfo;
    private BroadcastReceiver mReceiver;
    private ExecutorService mExe;
    static boolean flag = false;
    static boolean isSetReadTime;
    private String mCategoryVideo;

    public HttpFileHandler(final String webRoot, Handler handler) {
        this.webRoot = webRoot;
        this.handler = handler;
    }

    //只要应用发出网络请求，这个服务器就接收到数据
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String responseString = "";

        String target = URLDecoder.decode(request.getRequestLine().getUri(), "UTF-8");//拿到uri,拿到网络请求的uri后面的字段数据


        Log.i(TAG, " ==== " + target+" " +request.getAllHeaders()+" "+request.getParams());//  myPic

        String method = request.getRequestLine().getMethod(); //拿到请求方法get或者post
        if (method.equalsIgnoreCase("POST")) {
            BasicHttpEntityEnclosingRequest r = (BasicHttpEntityEnclosingRequest) request;

            HttpEntity t = r.getEntity();

            int length = (int) t.getContentLength();

            InputStream inputStream = t.getContent();

            if (target.equalsIgnoreCase("/start")) {
                start(inputStream,request,response,context);
                return;
            } else if (target.equalsIgnoreCase("/stop")) {

            }else {
                responseString = HTTP_NOTFOUND;
                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            }
        } else if (method.equalsIgnoreCase("GET")) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("target", target);
            Log.i(TAG, "target=" + target);

            if (target.contains("start")) {
                msg.what = 123;
                msg.setData(bundle);
                handler.sendMessage(msg);
                responseString = "start= 0";
                response.setStatusCode(HttpStatus.SC_OK);
            } else if (target.contains("stop")) {
                responseString = "stop";
                response.setStatusCode(HttpStatus.SC_OK);
            } else {
                responseString = HTTP_NOTFOUND;
                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            }
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            responseString = "options options options";
        }
        setInfoToClient(request, response, responseString);
    }


    private void start(InputStream inputStream, HttpRequest request, HttpResponse response, HttpContext context) {
        try {
            String info = getStrFrom(inputStream);
            String responseString = "";

            if (info == null) {
                responseString = "json error";
                setInfoToClient(request, response, responseString);
            } else {
                EventBean eventBean = JsonUtil.getObject(info,EventBean.class);
                Log.i(TAG, "eventBean =" + eventBean.toString());
                setInfoToClient(request, response, info);
            }
        } catch (Exception e) {

        }
    }


    private String getStrFrom(InputStream inputStream){
        StringBuffer out = new StringBuffer();

        byte[] b = new byte[4096];

        try {
            for (int n; (n = inputStream.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return out.toString();
    }

    private void setInfoToClient(HttpRequest request, HttpResponse response, String responseString) {
        try {
            String target = "";
            try {
                target = URLDecoder.decode(request.getRequestLine().getUri(), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String method = request.getRequestLine().getMethod();

            Header[] header = request.getAllHeaders();
            HttpParams params = request.getParams();
            Log.i(TAG, "params =" + params.toString());

            Log.i(TAG, "target=" + target + " method=" + method);

            StringEntity entity = null;
            try {
                entity = new StringEntity(responseString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (method.equalsIgnoreCase("OPTIONS")) {
                // 通知客户端允许预检请求。并设置缓存时间
                response.addHeader("Access-Control-Allow-Origin", "*");

                response.addHeader("Access-Control-Allow-Headers", "origin, content-type");
            } else {
                response.setHeader("Access-Control-Allow-Origin", "*");
                if (entity != null)
                    response.setEntity(entity);
            }
        } catch (Exception e) {
        }
    }
}
