package com.ucsb.cs48.spotcheck.SCFirebaseInterface;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ucsb.cs48.spotcheck.SCLocalObjects.SpotCheckUser;


public class SCFirebaseAuth {

    private FirebaseAuth scAuth;

    public SCFirebaseAuth() {
        scAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> signIn(String email, String password) {
        return scAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return scAuth.createUserWithEmailAndPassword(email, password);
    }

    public void addAuthListener(FirebaseAuth.AuthStateListener listener) {
        scAuth.addAuthStateListener(listener);
    }

    public void removeAuthListener(FirebaseAuth.AuthStateListener listener) {
        scAuth.removeAuthStateListener(listener);
    }

    public FirebaseUser getCurrentUser() {
        return scAuth.getCurrentUser();
    }

}
