package com.example.hanut.providers;

import com.example.hanut.models.Chat;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatsProvider {

    CollectionReference mCollection;

    public ChatsProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Chats");
    }

    //V67 Una coleción dentro de otra colección
    public void create(Chat chat){
        mCollection.document(chat.getIdUser1()).collection("Users").document(chat.getIdUser2()).set(chat);
        mCollection.document(chat.getIdUser2()).collection("Users").document(chat.getIdUser1()).set(chat);
    }

    public Query getAll(String idUser){
        return mCollection.document(idUser).collection("Users");
    }
}
