package com.example.neo.recordpcm;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private MediaPlayer mediaPlayer;
    private PlayTask player;


    private static final String recordFile = "/var/rec.pcm";//麦录制的声音，包括电视以及人声
    private static final String ech0File = "/var/ech0.pcm";//节点采集的声音，只有电视的声音
    private static final String mergeFile = "/var/merge.pcm";//上面两路声音按LLRRLLRR合并
    private static final int SAMPLERATE = 16000;//16k采样率
    private static final int channelConfig_in = AudioFormat.CHANNEL_IN_MONO;//单通道
    private static final int channelConfig_out = AudioFormat.CHANNEL_OUT_MONO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;//16位采样
    private static final int BUFFERSIZE = 3200;
    private static final int BYTEDELAY = 960;//两个通道的时延，这里内采超前130ms，大致是960byte

    private boolean isRecording=true;
    private boolean isPlaying=false; //标记



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.abd);
                mediaPlayer.start();
            }
        }).start();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void startRecordEcho( int rate,int size);

    public native void readEcho(byte[] buffer);

    public native void callback(MainActivity mainActivity);

    public native void stopRecordEcho();

    public native void stopRecord();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void recordStart(View view) {
        new RecordThread().start();
    }
    public void recordFinish(View view) {
        isRecording = false;
        mediaPlayer.stop();
    }

    public void tinycapRecord(View view){
        stringFromJNI();
    }
    public void tinycapFinish(View view){
        stopRecord();
    }
    public void test(View view){
    }


    public void test1(View view){
    }

    private class RecordThread extends Thread {
        private int minBufferSize;
        private AudioRecord mAudioRecord;
        private byte[] recBuffer;
        private byte[] echoBuffer;

        private byte[] reco_buffer_pool_l = new byte[BUFFERSIZE];
        private byte[] reco_buffer_pool_m = new byte[BUFFERSIZE];
        private byte[] reco_buffer_pool_r = new byte[BUFFERSIZE];
        private byte[] ech0_buffer_pool_l = new byte[BUFFERSIZE];
        private byte[] ech0_buffer_pool_m = new byte[BUFFERSIZE];
        private byte[] ech0_buffer_pool_r = new byte[BUFFERSIZE];
        FileOutputStream rec_out = openRecordFile(recordFile);
        FileOutputStream ech0_out = openRecordFile(ech0File);
        FileOutputStream merge_out = openRecordFile(mergeFile);

        @Override
        public void run() {
            //minBufferSize = AudioRecord.getMinBufferSize(SAMPLERATE,channelConfig_in, audioFormat);
            minBufferSize = BUFFERSIZE;
            Log.i(TAG,"minBufferSize = "+minBufferSize);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLERATE, channelConfig_in, audioFormat,
                    minBufferSize);
            recBuffer = new byte[minBufferSize];
            echoBuffer = new byte[minBufferSize];

            if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                isRecording = true;
                mAudioRecord.startRecording();
                startRecordEcho(SAMPLERATE,BUFFERSIZE);

                while (isRecording) {
                    int read = mAudioRecord.read(recBuffer, 0, minBufferSize);
                    if (read==AudioRecord.ERROR_BAD_VALUE){
                        continue;
                    }

                    readEcho(echoBuffer);
                    byte[] mergerBuffer = mergerBuffer(recBuffer, echoBuffer);
                    write2File(rec_out,recBuffer);
                    write2File(ech0_out,echoBuffer);
                    write2File(merge_out,mergerBuffer);
                }

                mAudioRecord.stop();
                stopRecordEcho();

                fileClose();
            }
        }

        byte[] mergerBuffer(byte[] recBuffer,byte[] echoBuffer){

            syncBuffer(recBuffer,echoBuffer);

            byte[] buffer = new byte[recBuffer.length + echoBuffer.length];
            for (int i =0;i<recBuffer.length/2;i++){//合并通道，按LLRRLLRR
                buffer[i*4] = recBuffer[i*2];
                buffer[i*4+1] = recBuffer[i*2+1];
                buffer[i*4+2] = echoBuffer[i*2];
                buffer[i*4+3] = echoBuffer[i*2+1];
            }
            return buffer;
        }

        void syncBuffer(byte[] recBuffer,byte[] echoBuffer){

            for(int i=0; i<recBuffer.length;i++) {
                reco_buffer_pool_l[i] = reco_buffer_pool_m[i];
                reco_buffer_pool_m[i] = reco_buffer_pool_r[i];
                reco_buffer_pool_r[i] = recBuffer[i];
            }

            for(int i=0; i<echoBuffer.length;i++) {
                ech0_buffer_pool_l[i] = ech0_buffer_pool_m[i];
                ech0_buffer_pool_m[i] = ech0_buffer_pool_r[i];
                ech0_buffer_pool_r[i] = echoBuffer[i];
            }

            for(int i=0; i<echoBuffer.length;i++)
                echoBuffer[i] = (byte)(ech0_buffer_pool_l[i]*0.921);//0.921是经验调幅系数

            for(int i = 0; i< BUFFERSIZE-BYTEDELAY; i++){
                recBuffer[i] = reco_buffer_pool_m[i+BYTEDELAY];
            }
            for(int i = 0; i< BYTEDELAY; i++){
                recBuffer[i+BUFFERSIZE-BYTEDELAY] = reco_buffer_pool_r[i];
            }
        }

        private void write2File(FileOutputStream out,byte[] buffer){
            try {
                out.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void fileClose(){
            try {
                ech0_out.close();
                rec_out.close();
                merge_out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private FileOutputStream  openRecordFile(String filepath){

        File f = new File(filepath);
        FileOutputStream out = null;
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create " + f.toString());
        }
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }

    public void playRecord(View view){
        player = new PlayTask();
        player.execute();
    }


    public void finish(View view){
        this.isPlaying = false;
    }

    class PlayTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            isPlaying = true;
            //int bufferSize = 3200;
            int bufferSize = AudioTrack.getMinBufferSize(SAMPLERATE, channelConfig_out, audioFormat);
            short[] buffer = new short[bufferSize];
            Log.i(TAG,"bufferSize = "+bufferSize);
            try {
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(recordFile)));
                //实例AudioTrack
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLERATE, channelConfig_out, audioFormat, bufferSize, AudioTrack.MODE_STREAM);
                //开始播放
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    float maxVol = AudioTrack.getMaxVolume();
                    track.setStereoVolume(maxVol,maxVol);
                }
                track.play();
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                while(isPlaying && dis.available()>0){
                    int i = 0;
                    while(dis.available()>0 && i<buffer.length){
                        buffer[i] = dis.readShort();
                        i++;
                    }

                    //然后将数据写入到AudioTrack中
                    track.write(buffer, 0, buffer.length);
                }

                //播放结束
                track.stop();
                dis.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        protected void onPostExecute(Void result){
        }

        protected void onPreExecute(){
        }

    }
}
