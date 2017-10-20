package com.teamenigma.factthisshoot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Handler to wait 1 second.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                gotoMainActivity();
            }
        }, 1000);
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
