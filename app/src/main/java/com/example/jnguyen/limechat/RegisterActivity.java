package com.example.jnguyen.limechat;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mPassword;
    EditText mEmail;
    Button btnRegister;
    Toolbar mToolbar;
    private FirebaseAuth mAuth;
    ProgressBar mProgress;
    String TAG_REGISTER = "REGISTER";//getResources().getString(R.string.TAG_REGISTER);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mUsername = findViewById(R.id.et_register_username);
        mPassword = findViewById(R.id.et_register_password);
        mEmail = findViewById(R.id.et_register_email);
        btnRegister = findViewById(R.id.btn_register);
        mProgress = findViewById(R.id.pb_register);

        mToolbar =  findViewById(R.id.register_toolbar);
        mToolbar.setTitle(R.string.action_register);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                Log.d("regInput",userName + " " + password + " " +email);

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
                    showWarningToast(view.getContext());

                } else {
                    registerUser(userName, password, email);
                }
            }
        });
//        if(showWarningToast){showWarningToast(this);}
    }

    private void showWarningToast(Context context){
        Toast warningToast = new Toast(this);
        warningToast.makeText(context,R.string.message_register_warning,Toast.LENGTH_SHORT).show();
    }

    private void registerUser(String username,String password,String email){
        mProgress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgress.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_REGISTER, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgress.setVisibility(View.INVISIBLE);
                            Log.w(TAG_REGISTER, "createUserWith Email:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });

    }
}

