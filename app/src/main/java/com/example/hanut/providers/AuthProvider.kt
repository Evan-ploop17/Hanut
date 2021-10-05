package com.example.socialmediagamer.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthProvider {
    var mAuth: FirebaseAuth

    fun googleLogin(idToken: String?): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return mAuth.signInWithCredential(credential)
    }

    val email: String?
        get() = if (mAuth.currentUser != null) {
            mAuth.currentUser!!.email
        } else {
            null
        }
    val uid: String?
        get() {
            return if (mAuth.currentUser != null) {
                mAuth.currentUser!!.uid
            } else {
                null
            }
        }

    init {
        mAuth = FirebaseAuth.getInstance()
    }
}