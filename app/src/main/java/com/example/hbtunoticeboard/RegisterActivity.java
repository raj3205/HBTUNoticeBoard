package com.example.hbtunoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    TextView tvRegistertoLogin;
    EditText etRName,etREmail,etRSR,etPasswordROne,getEtPasswordRTwo,etRegisterMobile;
    Button btnRegister;
    ImageView profilepic;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    String email,password,srnumber,cpassword,name,mobile;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static int PICK_IMAGE=123;
    Uri imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE&&resultCode==RESULT_OK&&data.getData()!=null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profilepic.setImageBitmap(bitmap);
            }catch(IOException e){

                e.printStackTrace();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        pd = new ProgressDialog(this);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");//applcation/* audio/*
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select umage"),PICK_IMAGE);
            }
        });



        tvRegistertoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Please wait");
                pd.show();
                if(validate()){
                    if(confirmpassword()){
                        mAuth.createUserWithEmailAndPassword(etREmail.getText().toString(),etPasswordROne.getText().toString())
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            sendUserData();
                                            sendEmailVerifiaction();
                                            pd.dismiss();
                                            finish();
                                        }else {
                                            pd.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void sendEmailVerifiaction() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser!=null){

            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Successfully Registered, Verification main send",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    private boolean confirmpassword() {
        boolean result = false;
        String password1 = etPasswordROne.getText().toString();
        String password2 = getEtPasswordRTwo.getText().toString();
        if(password1.equals(password2)){
            result = true;
        }else{
            Toast.makeText(RegisterActivity.this,"Both password must be same",Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private boolean validate() {
        boolean result = false;

        email = etREmail.getText().toString();
        password = etPasswordROne.getText().toString();
        cpassword = getEtPasswordRTwo.getText().toString();
        name = etRName.getText().toString();
        srnumber = etRSR.getText().toString();
        mobile = etRegisterMobile.getText().toString();

        if(email.isEmpty()||password.isEmpty()||cpassword.isEmpty()||name.isEmpty()||mobile.isEmpty()||srnumber.isEmpty()||imagePath==null){
            Toast.makeText(RegisterActivity.this,"Fill Up the Form and try again",Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }
        return result;
    }

    private void setupUI() {

        tvRegistertoLogin = (TextView)findViewById(R.id.tvRegistertoLogin);
        etREmail = (EditText) findViewById(R.id.etEmailRegister);
        etRName = (EditText)findViewById(R.id.etNameRegister);
        etRSR = findViewById(R.id.etSRRegister);
        etPasswordROne = (EditText)findViewById(R.id.etPasswordRegisterone);
        getEtPasswordRTwo = (EditText) findViewById(R.id.etPasswordRegisterTwo);
        etRegisterMobile = (EditText)findViewById(R.id.etRegisterMobile);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        profilepic = (ImageView)findViewById(R.id.ivProfile);
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference imageReference = storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic");//Userid / Images/Profilepic
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(RegisterActivity.this,"Upload Success",Toast.LENGTH_SHORT).show();
            }
        });
        UserProfile userProfile = new UserProfile(name,mobile,srnumber,email);
        myRef.setValue(userProfile);
    }
}