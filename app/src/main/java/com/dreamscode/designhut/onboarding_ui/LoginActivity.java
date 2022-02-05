package com.dreamscode.designhut.onboarding_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.ui.ForgetPasswordActivity;
import com.dreamscode.designhut.ui.FragmentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView tv_sign_up,tv_forget_password;
    Button btn_login;
    EditText et_email,et_password;

    private  String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";
    FirebaseAuth mAuth;

    Snackbar snackbar;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_sign_up = findViewById(R.id.tv_sign_up);
        btn_login = findViewById(R.id.btn_login);
        constraintLayout = findViewById(R.id.constraintLayout);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        mAuth = FirebaseAuth.getInstance();

        String text = "<font>Don't have an account?</font> <font color=#000000><b>SIGNUP</b></font>";
        tv_sign_up.setText(Html.fromHtml(text));

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                validateFields(email,password);
            }
        });
    }

    private void validateFields(String email, String password) {
        if (!(email.isEmpty())){
            if (!(password.isEmpty())){
                if (email.matches(EmailPattern)){
                    if (password.matches(PasswordPattern)){
                        logUser(email,password);
                    }else {
                        et_password.setError("Password must have 8 digit");
                    }
                }else {
                    et_email.setError("Email invalid!");
                }

            }else {
                et_password.setError("Password id Empty!");
            }
        }else{
            et_email.setError("Email is Empty!");
        }
    }
    private void logUser(String email, String password) {
        snackbar = Snackbar.make(constraintLayout,"Login please wait...",Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(Color.CYAN);
        snackbar.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,FragmentActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,"Error occurred please try again...!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Error occurred please try again...!",Toast.LENGTH_SHORT).show();
            }
        });

    }
}