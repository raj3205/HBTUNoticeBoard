package com.example.hbtunoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvForgotPassword, tvRegister;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupUI();
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        pd = new ProgressDialog(this);
        if(user!=null){
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String passwod = etPassword.getText().toString();
                pd.setMessage("Please Wait");
                pd.show();
                if(validate()){
                    mAuth.signInWithEmailAndPassword(email,passwod)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        checkEmailVerification();
                                    }else{
                                        Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void checkEmailVerification() {

        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag) {
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else {
            Toast.makeText(LoginActivity.this, "Verify your Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    private boolean validate() {
        boolean result = false;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(LoginActivity.this,"Fill up the all entries",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        }else{
            result = true;
        }
        return  result;
    }


    private void setupUI() {

        etEmail = (EditText)findViewById(R.id.etLoginEmail);
        etPassword = (EditText)findViewById(R.id.etLoginPassword);
        btnRegister = (Button)findViewById(R.id.btnLogin);
        tvForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        tvRegister = (TextView)findViewById(R.id.tvLogintoRegister);
    }
}

