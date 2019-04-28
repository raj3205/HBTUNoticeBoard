package com.example.hbtunoticeboard;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private Button btnSignout;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    private ListView listView;
    private CardView cvTimeTable, cvStaff, cvNewsNotification, cvProfile, cvEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uisetUp();
        onClickCardView();

    }

    private void onClickCardView() {

        cvTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TimeTable.class);
                startActivity(intent);
            }
        });

        cvStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StaffActivity.class);
                startActivity(intent);
            }
        });

        cvNewsNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewsNotification.class);
                startActivity(intent);
            }
        });

        cvEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EventActivity.class);
                startActivity(intent);
            }
        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uisetUp() {

        cvTimeTable = (CardView)findViewById(R.id.cvTimeTable);
        cvStaff = (CardView)findViewById(R.id.cvStaff);
        cvNewsNotification = (CardView)findViewById(R.id.cvNewsNotification);
        cvProfile = (CardView)findViewById(R.id.cvProfile);
        cvEvent = (CardView)findViewById(R.id.cvEvents);

    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutmenu: {
                signout();
            }
            case R.id.profilegmenu: {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        }
        return true;
    }


}