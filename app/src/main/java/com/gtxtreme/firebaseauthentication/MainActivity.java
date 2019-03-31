package com.gtxtreme.firebaseauthentication;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton;
    private EditText editEmail, editPass;
    private FirebaseAuth firebaseAuth;
    private Button signupbtn;
    private Switch aSwitch;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref=new SharedPref(this);
        if(sharedPref.loadNightmodestate()){
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.siginbtn);
        editEmail = findViewById(R.id.emailedittext);
        editPass = findViewById(R.id.pwdedittext);
        signupbtn = findViewById(R.id.signupbtn);
        loginButton.setOnClickListener(this);
        signupbtn.setOnClickListener(this);
        aSwitch = findViewById(R.id.switch1);

        firebaseAuth = FirebaseAuth.getInstance();

        if (sharedPref.loadNightmodestate())
            aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPref.setNightModestate(true);
                    restartApp();
                } else {
                    sharedPref.setNightModestate(false);
                    restartApp();
                }
            }
        });

    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();


    }

    protected void registerUser() {
        if (TextUtils.isEmpty(editEmail.getText().toString().trim())) {
            Toast.makeText(this, "Email ID cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(editPass.getText().toString().trim())) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(editEmail.getText().toString().trim(), editPass.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.signupbtn:
                registerUser();
                break;
            case R.id.siginbtn:
                signinuser();
                break;
        }
    }

    private void signinuser() {
        if (TextUtils.isEmpty(editEmail.getText().toString().trim())) {
            Toast.makeText(this, "Email ID cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(editPass.getText().toString().trim())) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }firebaseAuth.signInWithEmailAndPassword(editEmail.getText().toString().trim(),editPass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Signing you in... Please wait",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}