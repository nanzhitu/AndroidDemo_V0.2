package com.neo.webserver.server;

import android.os.Handler;
import android.util.Log;

import com.neo.webserver.data.CommonDefine;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class WebServer extends Thread {
    private final static String TAG="WebServer";
	static final String SUFFIX_ZIP = "..zip";
	static final String SUFFIX_DEL = "..del";

	private int port;
	private String webRoot;
	private boolean isLoop = false;
	private Handler handler = null;
	private HttpParams mParams;
	private int readTime=0;
	private HttpService mHttpService;

	public WebServer(int port, final String webRoot, Handler handler) {
		super();
		this.port = port;
		this.webRoot = webRoot;
		this.handler = handler;
	}

	@Override
	public void run() {
		setWebServer(readTime);
	}

	private void setWebServer(int readTime) {
		Log.i(TAG,"run run run!!");
		ServerSocket serverSocket = null;
		try {
			// 创建服务器套接字
			serverSocket = new ServerSocket(port);

			// 创建HTTP协议处理器
			BasicHttpProcessor httpproc = new BasicHttpProcessor();
			// 增加HTTP协议拦截器
			httpproc.addInterceptor(new ResponseDate());
			httpproc.addInterceptor(new ResponseServer());
			httpproc.addInterceptor(new ResponseContent());
			httpproc.addInterceptor(new ResponseConnControl());

			// 创建HTTP服务
			mHttpService = new HttpService(httpproc,
					new DefaultConnectionReuseStrategy(),
					new DefaultHttpResponseFactory());
			// 创建HTTP参数
			mParams = new BasicHttpParams();
			//setReadTime(readTime);
			if (readTime==0){ //自己添加的读取时间
				readTime=5000;
			}
			mParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, readTime)
					.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
							8 * 1024)
					.setBooleanParameter(
							CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
					.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
					.setParameter(CoreProtocolPNames.ORIGIN_SERVER,"WebServer/1.1");
			// 设置HTTP参数
			mHttpService.setParams(mParams);

			// 创建HTTP请求执行器注册表
			HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
			// 增加HTTP请求执行器
			//reqistry.register("*" + SUFFIX_ZIP, new HttpZipHandler(webRoot));
			//reqistry.register("*" + SUFFIX_DEL, new HttpDelHandler(webRoot));
			reqistry.register("*", new HttpFileHandler(webRoot, handler));
			//reqistry.register("/key/*", new HttpKeyHandler(webRoot, handler));

			// 设置HTTP请求执行器
			mHttpService.setHandlerResolver(reqistry);

			/* 循环接收各客户端 */
			isLoop = true;
			while (isLoop && !Thread.interrupted()) {
				// 接收客户端套接字
				Socket socket = serverSocket.accept();
				SocketAddress s_addr = socket.getRemoteSocketAddress();
				Log.i(TAG, "s_addr="+s_addr);

				if (s_addr != null)
				{
					String addr= s_addr.toString();
					CommonDefine.remoteIP = addr.substring(1);
					Log.i(TAG, "addr = "+addr);
				}
				// 绑定至服务器端HTTP连接
				DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
				conn.bind(socket, mParams);
				// 派送至WorkerThread处理请求
				Thread t = new WorkerThread(mHttpService, conn);
				t.setDaemon(true); // 设为守护线程
				t.start();
			}

		} catch (IOException e) {
			isLoop = false;
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public void close() {
		isLoop = false;
	}

	public void setTime(int i) {
		this.readTime=i;
	}

}
