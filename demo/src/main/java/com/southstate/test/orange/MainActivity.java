package com.southstate.test.orange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.southstate.orange.OrangeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadJS(View view) {
//        String url = "http://10.0.2.2:8000/pages/MainPage.js";
//        String url = "http://192.168.0.101:8000/pages/MainPage.js";
        String url = "assets://orange.com/StartPage.js";
        OrangeActivity.launch(this, url, null);
    }
}
