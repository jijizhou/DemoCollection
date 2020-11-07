package com.example.homedemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.Son;

public class MainActivity extends AppCompatActivity {


    BroadcastReceiver receiver;
    private ImageView image_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
//        Son s1 = new Son();
//        System.out.println();
//        Son s2 = new Son();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        final TextView text_btn = (TextView) findViewById(R.id.text_btn);
        image_loading = (ImageView) findViewById(R.id.image_loading);
        text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    ComponentName name = new ComponentName("com.sgm.buick"
                            ,"com.sgm.cadillac.function.home.activity.HomeActivity");
                    intent.setComponent(name);

                    Bundle bundle = new Bundle();
                    bundle.putString("data","app-lifeservice-2019");
                    intent.putExtra("SGM_APP",bundle);

                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"请先安装该app",Toast.LENGTH_LONG).show();
                }
            }
        });


        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter("com.sgm.buick");
        //注册广播
        registerReceiver(receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    Bundle sgm = intent.getBundleExtra("SGM_APP");
                    if (sgm != null) {
                        String data = sgm.getString("data");
                        Log.d("MainActivity", data);
                        text_btn.setText(data);
                    }
                }
            }
        },intentFilter);


        RotateAnimation rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1200);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        rotate.setStartOffset(10);//执行前的等待时间
        image_loading.setAnimation(rotate);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        image_loading.clearAnimation();
    }
}
