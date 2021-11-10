package com.example.hanut.providers;

import com.example.hanut.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
    }

    // Recibe el modelo Post con todos los atributos que este tiene 
    public Task<Void> save(Post post){
        // Con document() sin parametros le damos un ID único a cada publicación
        return mCollection.document().set(post);
    }

}
