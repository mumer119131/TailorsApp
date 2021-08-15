package com.example.tailorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.Database.UserDatabaseHelper;
import com.example.tailorsapp.PersonModel.PersonModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private TextView btnToLogin;
    private EditText name,email,pass,confirmPass;
    private Button signupBtn;
    private FirebaseAuth mAUTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnToLogin=findViewById(R.id.btnToLogin);
        name=findViewById(R.id.etName);
        email=findViewById(R.id.etSignupEmail);
        pass=findViewById(R.id.etSignupPassword);
        confirmPass=findViewById(R.id.etSignUpConfirmPassword);
        signupBtn=findViewById(R.id.btnSignup);
        mAUTH=FirebaseAuth.getInstance();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkConnected(SignupActivity.this)) {
                    Toast.makeText(SignupActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!isOnline()) {
//                    Toast.makeText(SignupActivity.this, "Internet not working", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                String strName = name.getText().toString().trim();
                String strEmail=email.getText().toString().trim();
                String strPassword=pass.getText().toString().trim();
                String strConfirmPassword=confirmPass.getText().toString().trim();

                if(TextUtils.isEmpty(strEmail)){
                    name.setError("Please enter your name");
                    name.requestFocus();
                }
                if(TextUtils.isEmpty(strEmail)){
                    email.setError("Please enter your email");
                    email.requestFocus();
                }
                if(!(Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())){
                    email.setError("Please enter valid email address");
                    email.requestFocus();
                }
                if(TextUtils.isEmpty(strPassword)){
                    pass.setError("Please enter your password");
                    pass.requestFocus();
                }
                if(TextUtils.isEmpty(strConfirmPassword)){
                    confirmPass.setError("Please enter your confirm password");
                    confirmPass.requestFocus();
                }
                if(!(strPassword.equals(strConfirmPassword))){
                    Toast.makeText(SignupActivity.this, "Password does not matches", Toast.LENGTH_SHORT).show();
                }
                InsertDataBase(strName,strEmail,strPassword);

             }
        });


        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivirty.class));
                finish();
            }
        });
    }

    private void InsertDataBase(String userName,String userEmail,String userPassword){
        mAUTH.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            PersonModel values=new PersonModel(userName,userEmail,userPassword);
                            FirebaseDatabase.getInstance().getReference("User").child(mAUTH.getCurrentUser().getUid()).setValue(values)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                startActivity(new Intent(SignupActivity.this, LoginActivirty.class));
                                                login(userEmail,userPassword);
                                                Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();

                                                finish();
                                            }
                                            else
                                            Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                            Toast.makeText(SignupActivity.this, "User Already Exist with this Email", Toast.LENGTH_SHORT).show();
                    }

                    private void login(String userEmail, String userPassword) {
                        mAUTH.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    LoginActivirty obj = new LoginActivirty();
                                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
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

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {

            return false;
        }
    }


}