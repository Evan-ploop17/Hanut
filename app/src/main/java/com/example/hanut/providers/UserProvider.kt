package com.example.hanut.providers


import com.example.hanut.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap


class UserProvider {
    private val mCollection: CollectionReference
    fun getUser(id: String?): Task<DocumentSnapshot>? {
        return id?.let { mCollection.document(it).get() }
    }

    fun create(user: User): Task<Void>? {
        return user.id?.let { mCollection.document(it).set(user) }
    }

    fun update(user: User): Task<Void?>? {
        val map: MutableMap<String, Any> = HashMap()
        user.userName?.let { map.put("name", it) }
        return user.id?.let { mCollection.document(it).update(map) }
    }

    init {
        mCollection = FirebaseFirestore.getInstance().collection("Users")
    }
}
