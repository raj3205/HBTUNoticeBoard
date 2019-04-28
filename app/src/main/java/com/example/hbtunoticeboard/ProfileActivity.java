package com.example.hbtunoticeboard;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail, tvProfileNumber, tvSRNumber;
    public ImageView ivPrfilePic;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setUpUI();

    }

    private void setUpUI() {

        tvProfileName = (TextView)findViewById(R.id.tv_ProfileNmae);
        tvProfileEmail = (TextView)findViewById(R.id.tv_ProfileEmail);
        tvProfileNumber = (TextView)findViewById(R.id.tv_Profile_Mobile);
        tvSRNumber = (TextView)findViewById(R.id.tv_ProfileSR_Number);
        ivPrfilePic = (ImageView) findViewById(R.id.iv_profile_pic);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(mAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivPrfilePic);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                tvProfileName.setText(userProfile.getName());
                tvProfileEmail.setText(userProfile.getEmail());
                tvProfileNumber.setText(userProfile.getMobile());
                tvSRNumber.setText(userProfile.getSrNumber());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
