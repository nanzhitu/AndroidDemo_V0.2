package com.neo.nanohttpd_server;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Neo on 2019/1/9.
 */

public interface IHttpFileHandler {

    NanoHTTPD.Response doGet(NanoHTTPD.IHTTPSession session);

    NanoHTTPD.Response doPost(NanoHTTPD.IHTTPSession session);
}
