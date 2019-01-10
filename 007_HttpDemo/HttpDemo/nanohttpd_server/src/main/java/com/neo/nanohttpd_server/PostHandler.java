package com.neo.nanohttpd_server;

import android.os.Environment;

import com.neo.nanohttpd_server.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by Neo on 2019/1/9.
 */

public class PostHandler {

    private static final String TAG = "PostHandler";
    private static PostHandler sPostHandler;
    private PostHandler() {
    }

    public static PostHandler getInstance() {
        if (sPostHandler == null) {
            synchronized (PostHandler.class) {
                if (sPostHandler == null) {
                    sPostHandler = new PostHandler();
                }
            }
        }
        return sPostHandler;
    }


    public NanoHTTPD.Response upload(NanoHTTPD.IHTTPSession session){

        Map<String, String> files = new HashMap<String, String>();
        try {
            session.parseBody(files);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
        String nameFilePath = files.get("filename");
        final File nameFile = new File(nameFilePath);
        String name = FileUtil.read(nameFile);
        if(name == null){
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", "name == null");
        }

        Map<String, String> params = session.getParms();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            final String paramsKey = entry.getKey();
            if (paramsKey.equalsIgnoreCase("file")) {
                final String dataFilePath = files.get(paramsKey);
                final String fileName = ""+System.currentTimeMillis()+"_"+name;
                final File dataFile = new File(dataFilePath);
                final File targetFile = new File(Environment.getExternalStorageDirectory(),fileName);
                FileUtil.copyFile(dataFile,targetFile);
            }
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", "upload success");
    }

    public NanoHTTPD.Response download(NanoHTTPD.IHTTPSession session){
        String path = Environment.getExternalStorageDirectory()+"/1546939853841_logo.png";
        File file = new File(path);
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "image/png", FileUtil.getFileInputStream(path),file.length());
    }
}
