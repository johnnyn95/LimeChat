package com.example.jnguyen.limechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mStatusInput;
    private Button mSaveChanges;

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mToolbar = findViewById(R.id.status_appBar);
        mStatusInput = findViewById(R.id.et_status_input);
        mSaveChanges = findViewById(R.id.btn_status_save_changes);
        mLoading = findViewById(R.id.pb_status);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.changeStatus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user_Uid = mUser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(getResources().getString(R.string.db_Users)).child(current_user_Uid);

        String currentStatus = getIntent().getStringExtra(getResources().getString(R.string.db_Status));
        if(!TextUtils.isEmpty(currentStatus)){
            mStatusInput.setText(currentStatus);
        }

        mSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoading();
                String statusInput = mStatusInput.getEditableText().toString().trim();
                if(!TextUtils.isEmpty(statusInput)){
                    mDatabaseReference.child(getResources().getString(R.string.db_Status)).setValue(statusInput)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                stopLoading();
                                Toast.makeText(StatusActivity.this,R.string.message_status_success,Toast.LENGTH_SHORT);
                                finish();
                            } else {
                                stopLoading();
                                Toast.makeText(StatusActivity.this,R.string.message_status_error,Toast.LENGTH_LONG);
                            }
                        }
                    });

                }
            }

        });

    }

    private void startLoading(){
        mStatusInput.setVisibility(View.INVISIBLE);
        mSaveChanges.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void stopLoading(){
        mStatusInput.setVisibility(View.VISIBLE);
        mSaveChanges.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.INVISIBLE);
    }

}
