package com.example.tailorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class LoginActivirty extends AppCompatActivity {
    private TextView btnToSignUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button btnLogin;
    private EditText email,password;
    private SharedPreferences sharedPreferences;
    private UserDatabaseHelper user_db;
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
        user_db =new UserDatabaseHelper(LoginActivirty.this);
        
        
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
                if (!networkConnected(LoginActivirty.this)) {
                    Toast.makeText(LoginActivirty.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                                    setUserName();
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    startActivity(new Intent(LoginActivirty.this,MainActivity.class));
                                    Toast.makeText(LoginActivirty.this, "Login Successful", Toast.LENGTH_SHORT).show();
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

    private boolean networkConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected() && wifiConn.isAvailable()) || (mobileConn != null && mobileConn.isConnected() && mobileConn.isAvailable())) {
            return true;
        } else {
            return false;
        }

    }
    public void setUserName() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);

                if (!(user_db == null)) {
                    Cursor cursor = user_db.getAllData();
                    if (cursor.getCount() > 0) {
                        user_db.EditIntoTable("1", name, email, "0", "0");
                    } else {
                        user_db.InsertIntoTable("1", name, email, "0", "0");
                    }
                    cursor.close();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivirty.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}