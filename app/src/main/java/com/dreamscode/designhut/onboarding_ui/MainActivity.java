package com.dreamscode.designhut.onboarding_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.ui.FragmentActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView img_logo;
    TextView tv_powered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        img_logo = findViewById(R.id.img_logo);
        tv_powered = findViewById(R.id.tv_powered);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img_logo.setAnimation(topAnim);
        tv_powered.setAnimation(bottomAnim);

        final Intent intent= new Intent(this, SignUpActivity.class);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();

                }
            }
        };
        timer.start();

    }

}