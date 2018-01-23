package com.example.neo.utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by neo on 2017/12/14.
 */

public class SystemUtil {

    private static String getMac() {
        String mac = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStream fis = p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line;
            //直到读完为止
            while ((line = br.readLine()) != null) {
                if(mac == null)
                    mac = line;
                else
                    mac += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mac;
    }
}
