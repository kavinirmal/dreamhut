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
import com.dreamscode.designhut.ui.FragmentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    TextView tv_login;
    EditText et_user_name,et_email,et_password;
    Button btn_sign_up;

    private  String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private  String MobilePattern = "^\\+[0-9]{10,13}$";
    private String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";

    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userID;

    ConstraintLayout constraintLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_login = findViewById(R.id.tv_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        constraintLayout = findViewById(R.id.constraintLayout);

        String text = "<font>Already have an account?</font> <font color=#000000><b>LOGIN</b></font>";
        tv_login.setText(Html.fromHtml(text));

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(SignUpActivity.this, FragmentActivity.class);
            startActivity(intent);
            finish();
        }

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name,email,password;
                user_name = et_user_name.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                validateFields(user_name,email,password);
            }
        });

    }

    private void validateFields(String user_name, String email, String password) {
        if (!(user_name.isEmpty())){
            if (!(email.isEmpty())){
                if (!(password.isEmpty())){
                    if (email.matches(EmailPattern)){
                        if (password.matches(PasswordPattern)){
                            SignUpUser(user_name,email,password);
                        }else {
                            et_password.setError("Length should 8-24!");
                        }

                    }else {
                        et_email.setError("Email Not Valid!");
                    }
                }else {
                    et_password.setError("Required!");
                }
            }else {
                et_email.setError("Required!");
            }
        }else {
            et_user_name.setError("Required!");
        }
    }
    private void SignUpUser(String user_name, String email, String password) {
        snackbar = Snackbar.make(constraintLayout,"Sign in please wait...",Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(Color.CYAN);
        snackbar.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("User").document(userID);
                    Map<String, Object> users = new HashMap<>();
                    users.put("UserName",user_name);
                    users.put("UserEmail",email);
                    users.put("PostCount",0);
                    documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Error occurred User details not set log and update them.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(SignUpActivity.this,"Error occurred Please try again...!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,"Error occurred Please try again...!",Toast.LENGTH_SHORT).show();
            }
        });

    }
}