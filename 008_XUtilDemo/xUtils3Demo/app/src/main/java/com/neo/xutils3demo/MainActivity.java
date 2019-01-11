package com.neo.xutils3demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.xutils3demo.data.EventBean;
import com.neo.xutils3demo.utils.FileUtil;
import com.neo.xutils3demo.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.table.TableEntity;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_main)//加载布局xml
public class MainActivity extends AppCompatActivity {


    String[] title = {"播天气","播天气", "看电影"};
    String[] mEvent = {"天气怎么样","深圳明天的天气","播放如懿传"};
    int[] eventType = {0,0,1};
    boolean[] enable = {true,true,true};
    boolean[] enableTTS = {true,true,true};
    String[] extend = {"adasasa","qqqdsadsdsds","dsdsds"};
    private String time;
    private int opt;

    // view注解要求必须提供id，以使代码混淆不受影响。
    // 代价是增加了一次反射，每个控件都会。而反射是比较牺牲性能的做法
    @ViewInject(R.id.text)
    private TextView text;

    //Event事件注解 ，点击button后弹出button测试
    @Event(value = {R.id.button},type = View.OnClickListener.class)
    private void onTestclick(View view){//方法需要用private

        Toast.makeText(this,"button测试",Toast.LENGTH_LONG).show();
    }

    //Event事件注解 ，点击button后弹出button测试
    @Event(value = {R.id.image},type = View.OnClickListener.class)
    private void onImageclick(View view){//方法需要用private

        Intent intent = new Intent(this,ImageActivity.class);
        startActivity(intent);
    }

    //Event事件注解 ，点击button后弹出button测试
    @Event(value = {R.id.db},type = View.OnClickListener.class)
    private void onDbclick(View view){//方法需要用private

        Intent intent = new Intent(this,DbActivity.class);
        startActivity(intent);
    }

    @Event(value = {R.id.get},type = View.OnClickListener.class)
    private void onGetclick(View view){//方法需要用private

        String url ="http://172.20.5.130:8899/queryClock";
        RequestParams params = new RequestParams(url);
        //params.setSslSocketFactory(...); // 设置ssl

//        params.addQueryStringParameter("username","abc");
//        params.addQueryStringParameter("password","123");
        x.http().get(params, new Callback.CommonCallback<String>() {

            public void onSuccess(String result) {
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                Log.i("ygj", "onSuccess result:" + result);

            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {

            }
        });
    }

    @Event(value = {R.id.requestPost},type = View.OnClickListener.class)
    private void onRequestPostClick(View view){
        String url ="http://172.20.5.130:8899/setClock";
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(getPostString());
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ygj", "onSuccess result:" + result);
                Toast.makeText(x.app(),result,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    @Event(value = {R.id.post},type = View.OnClickListener.class)
    private void onPostClick(View view){
        String url ="http://172.20.5.130:8899/setClock";
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(getPostString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ygj", "onSuccess result:" + result);
                Toast.makeText(x.app(),result,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    @Event(value = {R.id.cache},type = View.OnClickListener.class)
    private void onCacheClick(View view){
        String url ="http://172.20.5.130:8899/queryClock";
        RequestParams params = new RequestParams(url);
        params.setCacheMaxAge(1000*20); //为请求添加缓存时间

        Callback.Cancelable cancelable = x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ygj","onSuccess："+result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
            //result：缓存内容
            @Override
            public boolean onCache(String result) {
                //在setCacheMaxAge设置范围（上面设置的是60秒）内，如果再次调用GET请求，
                //返回true：缓存内容被返回，相信本地缓存，适用于重新进入网页，如浏览器网页互相切换
                //返回false：不相信本地缓存，会请求网络,适用于用户主动刷新网页
                Log.i("ygj","cache："+result);
                return true;
            }
        });
    }

    @Event(value = {R.id.download},type = View.OnClickListener.class)
    private void onDownloadClick(View view){
        String url ="http://172.20.5.130:8080/download";
        RequestParams params = new RequestParams(url);
        //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(Environment.getExternalStorageDirectory()+"/myapp/");
        //自动为文件命名
        params.setAutoRename(true);
        x.http().post(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {

                File file = new File(params.getSaveFilePath(),""+System.currentTimeMillis()+"_logo.png");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileUtil.copyFile(result,file);
                //apk下载完成后，调用系统的安装方法
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
//                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                Log.i("ygj","current："+ current +"，total："+total);
            }
        });
    }

    @Event(value = {R.id.upload},type = View.OnClickListener.class)
    private void onUploadClick(View view){
        String url ="http://172.20.5.130:8080/upload";
        String path="/mnt/sdcard/Download/logo.png";
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter("filename","logo.png");
        params.addBodyParameter("file",new File(path));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ygj", "onSuccess result:" + result);
                Toast.makeText(x.app(),result,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }


    private String getPostString(){

        time = ""+(System.currentTimeMillis()/1000 + 60*60*10);
        opt = 1;

        final EventBean eventBean = new EventBean();
        eventBean.setTitle("就是要啊");
        eventBean.setExtend("testtest");
        List<EventBean.Event> eventList = new ArrayList<>();
        for(int i=0; i< 3; i++) {
            EventBean.Event event = new EventBean.Event();
            event.setMultselectable(enable[i]);
            event.setTitle(title[i]);
            event.setEvent(mEvent[i]);
            event.setEventType(eventType[i]);
            event.setNeedTTS(enableTTS[i]);
            event.setExtend(extend[i]);
            eventList.add(event);
        }

        eventBean.setEventList(eventList);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event", JsonUtil.toJson(eventBean));
            jsonObject.put("trig_time",time);
            jsonObject.put("opt",opt);

            jsonObject.put("mIsOpen",true);
            jsonObject.put("clock_type",2);
            jsonObject.put("repeat_type",1);

            jsonObject.put("repeat_interval","1");
            jsonObject.put("volume",4);
            jsonObject.put("clock_id",12);
            jsonObject.put("event_extend",1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("ygj","jsonObject = "+jsonObject.toString());
        return jsonObject.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); //绑定注解
    }
}
