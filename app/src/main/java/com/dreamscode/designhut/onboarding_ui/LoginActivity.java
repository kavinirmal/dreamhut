package com.dreamscode.designhut.onboarding_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.ui.FragmentActivity;

public class LoginActivity extends AppCompatActivity {
    TextView tv_sign_up;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_sign_up = findViewById(R.id.tv_sign_up);
        btn_login = findViewById(R.id.btn_login);

        String text = "<font>Don't have an account?</font> <font color=#000000><b>SIGNUP</b></font>";
        tv_sign_up.setText(Html.fromHtml(text));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
            }
        });
    }
}