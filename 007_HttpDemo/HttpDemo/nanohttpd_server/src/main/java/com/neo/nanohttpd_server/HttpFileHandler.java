package com.neo.nanohttpd_server;

import android.os.Environment;

import com.neo.nanohttpd_server.data.CommonTarget;
import com.neo.nanohttpd_server.utils.FileUtil;
import com.neo.nanohttpd_server.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by Neo on 2019/1/9.
 */

public class HttpFileHandler implements IHttpFileHandler{

    private static final String TAG = "HttpFileHandler";

    @Override
    public NanoHTTPD.Response doGet(NanoHTTPD.IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("Sorry, Can't Found the page!");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }

    @Override
    public NanoHTTPD.Response doPost(NanoHTTPD.IHTTPSession session) {
        String target = null;
        try {
            target = URLDecoder.decode(session.getUri(), "UTF-8");
            LogUtils.d(TAG,"target = "+target);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(target == null){
            return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/html", "Error 404: File not found");
        }

        /*获取header信息，NanoHttp的header不仅仅是HTTP的header，还包括其他信息。*/
        Map<String, String> header = session.getHeaders();

        if(target.equalsIgnoreCase(CommonTarget.UPLOAD)){
          return PostHandler.getInstance().upload(session);
        } else if(target.equalsIgnoreCase(CommonTarget.DOWNLOAD)){
            return PostHandler.getInstance().download(session);
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", "Post request");
    }
}
