package com.example.tailorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.Database.UserDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivirty extends AppCompatActivity {
    private TextView btnToSignUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button btnLogin;
    private EditText email,password;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activirty);
        btnToSignUp=findViewById(R.id.btnToSignUp);
        btnLogin=findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.etFatherName);
        password=findViewById(R.id.etPassword);
        ProgressDialog progressDialog=new ProgressDialog(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        
        
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivirty.this,SignupActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail=email.getText().toString();
                String strPassword=password.getText().toString();
                if(strEmail.isEmpty()){
                    email.setError("Enter the email");
                    email.requestFocus();
                }
                if(strPassword.isEmpty()){
                    password.setError("Enter the Password");
                    password.requestFocus();
                }
                if(password.length()<6){
                    password.setError("Password length must be greater than 6");
                    password.requestFocus();
                }
                progressDialog.setMessage("Logging In");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(strEmail,strPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    startActivity(new Intent(LoginActivirty.this,MainActivity.class));
                                    Toast.makeText(LoginActivirty.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    getUserName();
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivirty.this, "Login failed please check your  credentials", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });

    }

    private void getUserName() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue(String.class);
                String email=snapshot.child("email").getValue(String.class);
                UserDatabaseHelper db= new UserDatabaseHelper(LoginActivirty.this);
                Cursor cursor = db.getAllData();
                if(cursor.getCount()>0){
                    Toast.makeText(LoginActivirty.this, Integer.toString(cursor.getCount()), Toast.LENGTH_SHORT).show();
                    db.EditIntoTable("1",name,email,"0","0");
                }else{
                    db.InsertIntoTable("1",name,email,"0","0");
                }
                cursor.close();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}