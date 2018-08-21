package com.example.jnguyen.limechat;

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

public class LoginActivity extends AppCompatActivity {
    EditText mPassword;
    EditText mEmail;
    Button btnLogin;
    Toolbar mToolbar;
    ProgressBar mProgress;
    private FirebaseAuth mAuth;
    String TAG_LOGIN = "LOGIN";//getResources().getString(R.string.TAG_LOGIN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mPassword = findViewById(R.id.et_login_password);
        mEmail = findViewById(R.id.et_login_email);
        btnLogin = findViewById(R.id.btn_login);
        mProgress = findViewById(R.id.pb_login);

        mToolbar =  findViewById(R.id.login_toolbar);
        mToolbar.setTitle(R.string.action_login);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
                    showWarningToast(view.getContext());

                } else {
                    loginUser(email,password);
                }
            }
        });
    }
    private void showWarningToast(Context context){
        Toast warningToast = new Toast(this);
        warningToast.makeText(context,R.string.message_register_warning,Toast.LENGTH_SHORT).show();
    }

    private void loginUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        startLoading();
                        if (task.isSuccessful()) {
                            mProgress.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_LOGIN, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            stopLoading();
                            Log.w(TAG_LOGIN, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void startLoading(){
        mPassword.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
    }
    private void stopLoading(){
        mPassword.setVisibility(View.VISIBLE);
        mEmail.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
    }
}
