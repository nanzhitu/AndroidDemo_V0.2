package com.neo.webserver.server;

import android.util.Log;


import com.neo.webserver.data.CommonDefine;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

import java.io.IOException;

/**
 * Created by Neo on 2018/12/5.
 */

public class WorkerThread extends Thread{
    private final String TAG="WorkerThread";
    private final HttpService httpservice;
    private final HttpServerConnection conn;


    public WorkerThread(final HttpService httpservice, final HttpServerConnection conn) {
        super();
        this.httpservice = httpservice;
        this.conn = conn;
    }

    @Override
    public void run() {
        HttpContext context = new BasicHttpContext();

        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                CommonDefine.count ++;
                Log.i(TAG, "workerThread run !!!!! count = "+CommonDefine.count);


                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException ex) {
            //System.err.println("Client closed connection");
        } catch (IOException ex) {
            //ex.printStackTrace();
        } catch (HttpException ex) {
            //ex.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                Log.i(TAG,"conn.shutdown");
                this.conn.shutdown();
            } catch (IOException ignore) {
            }
        }

    }
}
