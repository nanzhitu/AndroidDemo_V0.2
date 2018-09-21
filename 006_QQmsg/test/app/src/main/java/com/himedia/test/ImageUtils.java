package com.himedia.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Neo on 2018/9/12.
 */

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    /**
     * 获取网络图片
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public static Bitmap GetImageInputStream(String imageurl){
        URL url;
        HttpURLConnection connection=null;
        Bitmap bitmap=null;
        try {
            url = new URL(imageurl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream=connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Drawable getDrawable(String urlpath, String name) {
        Drawable drawable = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            drawable = Drawable.createFromResourceStream(Utils.getAppContext().getResources(), null, in, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static Bitmap getBitmap(String name) {
        String path = null;
        File file = Utils.getAppContext().getExternalFilesDir(null);
        if(file != null){
           file = new File(file.getAbsolutePath()+"/"+name+".png");
        }
        if(file != null){
            path = file.getPath();
        }else{
            file = new File(Utils.getAppContext().getFilesDir()+"/"+name+".png");
            if(file != null){
                path = file.getPath();
            }
        }
        if(path != null){
            return BitmapFactory.decodeFile(path);
        }else{
            return null;
        }
    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public static void SavaImage(Bitmap bitmap, String path, String name) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        File image_file = new File(path + "/" + name + ".png");
        if(image_file.exists()){
            Log.d(TAG,"image_file = "+image_file.getPath());
            return;
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + name + ".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Bitmap toRoundBitmap(Bitmap bitmap) {
        return toRoundBitmap(bitmap,-1);
    }

    public static synchronized Bitmap toRoundBitmap(Bitmap bitmap, float round) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        if(round > 0) {
            roundPx = round;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
        return output;
    }
}
