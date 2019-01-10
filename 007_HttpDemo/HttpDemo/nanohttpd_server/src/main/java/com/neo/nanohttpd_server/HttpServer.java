package com.neo.nanohttpd_server;

import android.os.Environment;

import com.neo.nanohttpd_server.utils.FileUtil;
import com.neo.nanohttpd_server.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Neo on 2018/12/6.
 */

public class HttpServer extends NanoHTTPD{

    private static final String TAG = "HttpServer";
    private IHttpFileHandler mHttpFileHandler;

    public HttpServer(int port) {
        super(port);
        mHttpFileHandler = new HttpFileHandler();
    }

    @Override
    public Response serve(IHTTPSession session) {

        //POST
        if (Method.POST.equals(session.getMethod())) {
          return mHttpFileHandler.doPost(session);
        }
        //GET
        else if(Method.GET.equals(session.getMethod())) {
            return mHttpFileHandler.doGet(session);
        }else {
            StringBuilder builder = new StringBuilder();
            builder.append("<!DOCTYPE html><html><body>");
            builder.append("Sorry, Can't Found the page!");
            builder.append("</body></html>\n");
            return newFixedLengthResponse(builder.toString());
        }
    }
}
