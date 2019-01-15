package mysop.pia.com.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import mysop.pia.com.R;

public class Firebase extends Activity {


    private static final String TAG = "firebase";

    public static final int RC_SIGN_IN = 1;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    boolean newUser = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        // Initialize Firebase components
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mFirebaseAuth = FirebaseAuth.getInstance();

//        FIREBASE SIGN IN
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
//                    USER ALREADY HAS ACCOUNT
//                onSignedInInitialize(user.getDisplayName());

                com.google.firebase.database.Query newUserMatch = mUsersDatabaseReference.orderByChild("userName").equalTo(user.getDisplayName());

                newUserMatch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0){
//                            DO THIS IF THEY HAVE AN ACCOUNT
                            Intent ShareWithUser = new Intent(Firebase.this, mysop.pia.com.Firebase.ShareWithUser.class);
                            startActivity(ShareWithUser);
                            finish();
                            Toast.makeText(Firebase.this, "Already Signed In as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            DO THIS IF THEY ARE NEW USERS
                            chooseUserName();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//                CHECK TO SEE IF ITS THEIR FIRST TIME SIGNING IN
//                if (!newUser) {
////                    IF THEY ARE A USER THIS GOES TO SHARE CLASS
//                    Intent ShareWithUser = new Intent(Firebase.this, mysop.pia.com.Firebase.ShareWithUser.class);
//                    startActivity(ShareWithUser);
//                    finish();
//                    Toast.makeText(this, "Already Signed In as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
//                }
//                else {
////                    IF IT IS THEIR FIRST TIME SIGNING IN THEY CREATE A USERNAME
//                    chooseUserName();
//                }
            } else {
//                    USER IS SIGNED OUT
//                onSignedOutCleanup();
//                FIREBASE CREATE ACCOUNT
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        RC_SIGN_IN);

//                USER CREATES ACCOUNT
//                newUser = true;
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                    Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedInInitialize(String username) {
//        mUsername = username;
//        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
//        mUsername = ANONYMOUS;
//        mMessageAdapter.clear();
//        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public void chooseUserName() {
        setContentView(R.layout.firebase);

        Button buttonAddUserName = findViewById(R.id.button_share);
        EditText etUserName = findViewById(R.id.edittext_firebase_username);

            buttonAddUserName.setOnClickListener(v -> {
                String username = etUserName.getText().toString().toLowerCase();

                if (!username.contains(" ") || username.equals("")) {

                    com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild("userName").equalTo(username);

                    userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            IF A USERNAME MATCHES ANOTHER IT IS GREATER THAN 0
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Toast.makeText(Firebase.this, "Choose a different user name", Toast.LENGTH_SHORT).show();
                            } else {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User nickname is set!");
                                            }
                                        });

                                Map neuUser = new HashMap();
                                neuUser.put("userName", username);
                                mUsersDatabaseReference.push().setValue(neuUser);

                                Toast.makeText(Firebase.this, "User name updated", Toast.LENGTH_SHORT).show();

                                Intent ShareWithUser = new Intent(Firebase.this, mysop.pia.com.Firebase.ShareWithUser.class);
                                startActivity(ShareWithUser);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(this, "No spaces allowed", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
