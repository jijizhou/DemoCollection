package com.example.homedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.homedemo.widget.FlowLayoutActivity;

public class DemoEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_enter);
    }

    public void flowLayout(View view) {
        startActivity(new Intent(DemoEnterActivity.this, FlowLayoutActivity.class));
    }

}