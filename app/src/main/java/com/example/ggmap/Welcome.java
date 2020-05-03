package com.example.ggmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {

    private static int SLASH_SCREEEN = 4000;
    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView logoName, nameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom);
        imageView = (ImageView) findViewById(R.id.logo_welcome);
        logoName = (TextView) findViewById(R.id.textView);
        nameApp = (TextView) findViewById(R.id.textView2);

        imageView.setAnimation(topAnim);
        logoName.setAnimation(bottomAnim);
        nameApp.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, SLASH_SCREEEN);
    }
}
