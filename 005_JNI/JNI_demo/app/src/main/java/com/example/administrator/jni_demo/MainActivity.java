package com.example.administrator.jni_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  //  static {
   //     System.loadLibrary("helloJni");//加载so文件，不要带上前缀lib和后缀.so
  //  }
  //  public native String helloJni();//定义本地方法接口，这个方法类似虚方法，实现是用c或者c++实现的

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.view);
     //   textView.setText(helloJni());
    }
}
