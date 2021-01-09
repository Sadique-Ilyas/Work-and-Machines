package com.example.workmachines;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    TextView welcomeText1, welcomeText2, welcomeText3, welcomeText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        welcomeText1 = findViewById(R.id.welcomeText1);
        welcomeText2 = findViewById(R.id.welcomeText2);
        welcomeText3 = findViewById(R.id.welcomeText3);
        welcomeText4 = findViewById(R.id.welcomeText4);

        YoYo.with(Techniques.FadeIn).duration(2000).playOn(welcomeText1);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(welcomeText2);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(welcomeText3);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(welcomeText4);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}