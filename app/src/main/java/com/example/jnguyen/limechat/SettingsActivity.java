package com.example.jnguyen.limechat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;

    private TextView mUsername;
    private TextView mStatus;
    private CircleImageView mImage;
    private Button mChangeStatus;
    private Button mChangeImage;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUsername = findViewById(R.id.tv_settings_displayName);
        mStatus = findViewById(R.id.tv_settings_status);
        mImage = findViewById(R.id.iv_settings_profilePicture);
        mChangeStatus = findViewById(R.id.btn_settings_changeStatus);
        mChangeImage = findViewById(R.id.btn_settings_changeImage);
        mLoading = findViewById(R.id.pb_settings);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uId = mCurrentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(getResources().getString(R.string.db_Users)).child(current_user_uId);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child(getResources().getString(R.string.db_Name)).getValue().toString();
                String status = dataSnapshot.child(getResources().getString(R.string.db_Status)).getValue().toString();
                String image = dataSnapshot.child(getResources().getString(R.string.db_Image)).getValue().toString();
                String thumb_image = dataSnapshot.child(getResources().getString(R.string.db_ThumbImage)).getValue().toString();

                mUsername.setText(displayName);
                mStatus.setText(status);
                Picasso.get().load(image).into(mImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStatusActivity();
            }
        });

        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startImageActivity();
            }
        });

    }
    private void startStatusActivity(){
        Intent statusActivity = new Intent(this,StatusActivity.class);
        statusActivity.putExtra(getResources().getString(R.string.db_Status),mStatus.getText().toString());
        startActivity(statusActivity);
    }

    private void startImageActivity(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                startLoading();
                Uri resultUri = result.getUri();
                StorageReference filepath = mStorageRef.child(getResources().getString(R.string.storage_profile_images)).child(LimeChatUtils.StringHelper.generateRandomString() + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            String downloadLink_url = task.getResult().getDownloadUrl().toString();
                            mDatabaseReference.child(getResources().getString(R.string.db_Image)).setValue(downloadLink_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        stopLoading();
                                        Toast.makeText(SettingsActivity.this,getResources().getString(R.string.message_change_image_success),Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        } else {
                            stopLoading();
                            Toast.makeText(SettingsActivity.this,getResources().getString(R.string.message_change_image_error),Toast.LENGTH_SHORT);
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void startLoading(){
        mUsername.setVisibility(View.INVISIBLE);
        mStatus.setVisibility(View.INVISIBLE);
        mImage.setVisibility(View.INVISIBLE);
        mChangeStatus.setVisibility(View.INVISIBLE);
        mChangeImage.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void stopLoading(){
        mUsername.setVisibility(View.VISIBLE);
        mStatus.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);
        mChangeStatus.setVisibility(View.VISIBLE);
        mChangeImage.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.INVISIBLE);
    }
}
