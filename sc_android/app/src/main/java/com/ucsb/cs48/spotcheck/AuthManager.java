package com.ucsb.cs48.spotcheck;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Nathan Inge on 2/5/18.
 */

public class AuthManager {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListen;

    public AuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password);
//            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//
//                    // If sign in fails, display a message to the user. If sign in succeeds
//                    // the auth state listener will be notified and logic to handle the
//                    // signed in user can be handled in the listener.
//                    if (!task.isSuccessful()) {
//                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
//                            Toast.LENGTH_SHORT).show();
//                    }
//
//                    // ...
//                }
//            });
    }

    public void signIn(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password);
//            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//
//                    // If sign in fails, display a message to the user. If sign in succeeds
//                    // the auth state listener will be notified and logic to handle the
//                    // signed in user can be handled in the listener.
//                    if (!task.isSuccessful()) {
//                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
//                            Toast.LENGTH_SHORT).show();
//                    }
//
//                    // ...
//                }
//            });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getDisplayName();
    }


}
