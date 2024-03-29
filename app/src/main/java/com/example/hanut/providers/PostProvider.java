package com.example.hanut.providers;

import com.example.hanut.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    // Método para hacer consultas en la base de datos usado en HOmeFragment
    // Obtenemos todos los post ordenados por titulos de forma descendiente
    // Con el Timestamp vamos a ordenar las publicaciones por ultima realizada
    public Query getAll(){
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getPostByCategoryAndTimestamp(String category){
        return mCollection.whereEqualTo("category", category).orderBy("timestamp", Query.Direction.DESCENDING);

    }


    // Con este método se hacen busquedas donde el campo coincida con lo metido en el search V62
    public Query getPostByTitle(String title){
        return mCollection.orderBy("title").startAt(title).endAt(title+'\uf8ff');

    }

    // Buscar todos los post donde el id del usuario es igual al id que recibe por parametro
    public Query getPostByUser(String id){
        return mCollection.whereEqualTo("idUser", id);
    }

    // Obtener publicación por ID
    public Task<DocumentSnapshot> getPostById(String id){
        return mCollection.document(id).get();
    }

    public Task<Void> delete(String idDocument){
        return mCollection.document(idDocument).delete();
    }

}
