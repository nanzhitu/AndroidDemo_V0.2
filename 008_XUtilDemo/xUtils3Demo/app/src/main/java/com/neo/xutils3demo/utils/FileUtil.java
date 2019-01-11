package com.neo.xutils3demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Neo on 2019/1/8.
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return void
     */
    public static void copyFile(String oldPath, String newPath) {

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        copyFile(oldFile,newFile);
    }

    /**
     * 拷贝文件
     *
     * @param oldFile
     * @param newFile
     */
    public static void copyFile(File oldFile, File newFile) {

        int bytesum = 0;
        int byteread = 0;
        InputStream inStream = null;
        FileOutputStream fs = null;

        try {
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            if (oldFile.exists()) { //文件存在时
                inStream = new FileInputStream(oldFile); //读入原文件
                fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[2048];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                if(inStream != null){
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读文件
     *
     * @param file
     */
    public static String read(File file){
        if(file == null || !file.exists()){
            return null;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte temp[] = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = inputStream.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            if(inputStream != null) {
                inputStream.close();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写文件
     *
     * @param targetFile
     * @param data
     */
    public static void write(File targetFile, String data){

        if(targetFile == null){
            return;
        }

        // 获取文件的输出流对象
        FileOutputStream outStream = null;
        try {
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            outStream = new FileOutputStream(targetFile);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(data.getBytes());
            // 最后关闭文件输出流
            if(outStream != null) {
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件流
     *
     * @param path
     */
    public static InputStream getFileInputStream(String path){
        if(path == null ){
            return null;
        }
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 获取文件大小
     *
     * @param f
     */
    public static long getFileSizes(File f){
        long size = 0;
        FileInputStream fis = null;
        try {
            if (f.exists()) {
                fis = new FileInputStream(f);
                size = fis.available();
            } else {
                f.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 创建文件
     *
     * @param file
     */
    public static File createNewFile(File file) {

        try {
            if (file.exists()) {
                return file;
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param path
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        return createNewFile(file);
    }


    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

}
