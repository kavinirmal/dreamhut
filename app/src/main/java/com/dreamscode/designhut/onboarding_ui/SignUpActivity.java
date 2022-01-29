package com.dreamscode.designhut.onboarding_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dreamscode.designhut.R;

public class SignUpActivity extends AppCompatActivity {
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_login = findViewById(R.id.tv_login);

        String text = "<font>Already have an account?</font> <font color=#000000><b>LOGIN</b></font>";
        tv_login.setText(Html.fromHtml(text));

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
}