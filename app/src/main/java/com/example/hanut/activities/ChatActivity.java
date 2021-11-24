package com.example.hanut.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hanut.R;
import com.example.hanut.models.Chat;
import com.example.hanut.providers.ChatsProvider;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {


    String mExtraIdUSer1;
    String mExtraIdUSer2;

    ChatsProvider mChatsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mExtraIdUSer1 = getIntent().getStringExtra("idUser1");
        mExtraIdUSer2 = getIntent().getStringExtra("idUser2");

        mChatsProvider = new ChatsProvider();

        createChat();

    }

    private void createChat(){
        Chat chat = new Chat();
        // Usuario1  o el dueño de la sesión, usuario2 la persona con la que va a chatear
        chat.setIdUser1(mExtraIdUSer1);
        chat.setIdUser2 (mExtraIdUSer2);
        chat.setWriting(false);
        chat.setTimestamp(new Date().getTime());
        mChatsProvider.create(chat);
    }





    // fin de clase activity
}