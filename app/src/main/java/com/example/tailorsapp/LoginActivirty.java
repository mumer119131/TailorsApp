package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivirty extends AppCompatActivity {
    private TextView btnToSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activirty);
        btnToSignUp=findViewById(R.id.btnToSignUp);
        
        
        
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivirty.this,SignupActivity.class));
                finish();
            }
        });
    }
}