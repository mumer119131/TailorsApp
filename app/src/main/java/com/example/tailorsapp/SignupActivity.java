package com.example.tailorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.PersonModel.PersonModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
                                                Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(SignupActivity.this, "Login to your account", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else
                                            Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                            Toast.makeText(SignupActivity.this, "Unable to signUp.\n Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}