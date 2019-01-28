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
import java.util.Objects;

import butterknife.ButterKnife;
import mysop.pia.com.R;

public class Firebase extends Activity {


    private static final String TAG = "firebase";

    public static final int RC_SIGN_IN = 1;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        // Initialize Firebase components
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.user));
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

//        FIREBASE SIGN IN
        mAuthStateListener = firebaseAuth -> {
            if (user != null) {
                com.google.firebase.database.Query newUserMatch = mUsersDatabaseReference.orderByChild(getString(R.string.username)).equalTo(user.getDisplayName());

                newUserMatch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0){
//                            DO THIS IF THEY HAVE AN ACCOUNT
                            Intent ShareWithUser = new Intent(Firebase.this, mysop.pia.com.Firebase.ShareWithUser.class);
                            startActivity(ShareWithUser);
                            finish();
                            Toast.makeText(Firebase.this, getString(R.string.alreadysignedin) + user.getDisplayName(), Toast.LENGTH_SHORT).show();
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
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                    Toast.makeText(this, R.string.signedin, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, R.string.signincanceled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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

                    com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild(getString(R.string.username)).equalTo(username);

                    userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            IF A USERNAME MATCHES ANOTHER IT IS GREATER THAN 0
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Toast.makeText(Firebase.this, R.string.differentname, Toast.LENGTH_LONG).show();
                            } else {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                Objects.requireNonNull(user).updateProfile(profileUpdates)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, getString(R.string.usernameset));
                                            }
                                        });

                                Map neuUser = new HashMap();
                                neuUser.put(getString(R.string.username), username);
                                neuUser.put(getString(R.string.email), user.getEmail());
                                mUsersDatabaseReference.push().setValue(neuUser);

                                Toast.makeText(Firebase.this, R.string.userupdated, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(this, R.string.nospaces, Toast.LENGTH_SHORT).show();
                }
            });
    }
}
