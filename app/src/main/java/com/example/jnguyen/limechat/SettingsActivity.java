package com.example.jnguyen.limechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;

    private TextView mUsername;
    private TextView mStatus;
    private CircleImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUsername = findViewById(R.id.tv_settings_displayName);
        mStatus = findViewById(R.id.tv_settings_status);
        mImage = findViewById(R.id.iv_settings_profilePicture);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uId = mCurrentUser.getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.db_Users)).child(current_user_uId);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child(getResources().getString(R.string.db_Name)).getValue().toString();
                String status = dataSnapshot.child(getResources().getString(R.string.db_Status)).getValue().toString();
                String image = dataSnapshot.child(getResources().getString(R.string.db_Image)).getValue().toString();
                String thumb_image = dataSnapshot.child(getResources().getString(R.string.db_ThumbImage)).getValue().toString();

                mUsername.setText(displayName);
                mStatus.setText(status);
//                Log.d("datasnapshot",dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
