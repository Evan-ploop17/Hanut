package com.example.hanut.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider {

    FirebaseAuth mAuth;

    public Task<AuthResult> register(String mail, String password){
        return mAuth.createUserWithEmailAndPassword(mail, password);
    }

    public AuthProvider(){
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(String email, String password){
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> googleLogin(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return mAuth.signInWithCredential(credential);
    }

    public String getEmail(){
        if(mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getEmail();
        }
        else{
            return null;
        }
    }

    public String getUid(){
        if(mAuth.getCurrentUser() != null ){
            return mAuth.getCurrentUser().getUid();
        }else {
            return null;
        }
    }

    public FirebaseUser getUserSession(){
        if(mAuth.getCurrentUser() != null ){
            return mAuth.getCurrentUser();
        }else {
            return null;
        }
    }

    public void logout(){
        if( mAuth != null){
            mAuth.signOut();
        }
    }
}
