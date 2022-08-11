package com.example.ruleteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText tietteacherID, tietpassword;
    Button btnLogin;
    TextView tvSignUp;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        getSupportActionBar().hide();


        tietteacherID= findViewById(R.id.loginID);
        tietpassword= findViewById(R.id.loginPW);
        tvSignUp= findViewById(R.id.signUpText);
        progressBar= findViewById(R.id.loginProgress);
        btnLogin= findViewById(R.id.buttonLogin);

        mAuth= FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void userlogin() {
        String strUserid= tietteacherID.getText().toString().trim();
        String strPassword= tietpassword.getText().toString().trim();

        if( strUserid.isEmpty()){
            tietteacherID.setError("Email is Required");
            tietteacherID.requestFocus();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(strUserid).matches()){
            tietteacherID.setError("Please provide valid email");
            tietteacherID.requestFocus();
        }
        if(strPassword.isEmpty()){
            tietpassword.setError("Password is required");
            tietpassword.requestFocus();
        }
        if (strPassword.length()<6){
            tietpassword.setError("Password must be at least 6 characters");
            tietpassword.requestFocus();
        }
        mAuth.signInWithEmailAndPassword(strUserid,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else{
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}