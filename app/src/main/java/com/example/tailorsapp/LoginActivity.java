package com.example.tailorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.RoomDataBase.User;
import com.example.tailorsapp.RoomDataBase.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView btnToSignUp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button btnLogin;
    private EditText email,password;
    private UserViewModel userViewModel;
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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        
        
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Login failed please check your  credentials", Toast.LENGTH_SHORT).show();
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

                userViewModel.insertOrder(new User(mAuth.getCurrentUser().getUid(),name,email,"0","0"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}