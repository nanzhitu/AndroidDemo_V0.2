package com.neo.webserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neo.webserver.service.WebService;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntent = new Intent(this, WebService.class);
        startService(mIntent);
    }


    @Override
    public void onBackPressed() {
        if (mIntent != null) {
            stopService(mIntent);
        }
        super.onBackPressed();
    }
}
