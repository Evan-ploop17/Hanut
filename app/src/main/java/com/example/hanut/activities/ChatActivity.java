package com.example.hanut.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hanut.R;
import com.example.hanut.models.Chat;
import com.example.hanut.models.Message;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.ChatsProvider;
import com.example.hanut.providers.MessageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {


    String mExtraIdUSer1;
    String mExtraIdUSer2;
    String mExtraIdChat;

    ChatsProvider mChatsProvider;
    MessageProvider mMessagesProvider;
    AuthProvider mAuthProvider;

    EditText mEditTextMessage;
    ImageView mImageViewSendMessage;

    View mActionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        showCustomToolbar(R.layout.custom_chat_toolbar);

        mExtraIdUSer1 = getIntent().getStringExtra("idUser1");
        mExtraIdUSer2 = getIntent().getStringExtra("idUser2");
        mExtraIdChat = getIntent().getStringExtra("idChat");

        mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSendMessage = findViewById(R.id.imageViewSendMessage);

        mImageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        mChatsProvider = new ChatsProvider();
        mMessagesProvider = new MessageProvider();
        mAuthProvider = new AuthProvider();

        checkIfChatExist();

    }

    private void sendMessage() {
        String textMessage = mEditTextMessage.getText().toString();
        if(!textMessage.isEmpty()){
            Message message= new Message();
            if(mAuthProvider.getUid().equals(mExtraIdUSer1)){
                message.setIdSender(mExtraIdUSer1);
                message.setIdReceiver(mExtraIdUSer2);
            } else{
                message.setIdSender(mExtraIdUSer2);
                message.setIdReceiver(mExtraIdUSer1);
            }
            message.setViewed(false);
            message.setTimestamp(new Date().getTime());
            message.setMessage(textMessage);
            message.setIdChat(mExtraIdChat);

            mMessagesProvider.create(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        mEditTextMessage.setText("");
                        Toast.makeText(ChatActivity.this, "SI", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ChatActivity.this, "NO", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showCustomToolbar(int resource) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarView = inflater.inflate(resource, null);
        actionBar.setCustomView(mActionBarView );
    }

    private void checkIfChatExist(){
        mChatsProvider.getChatByUSer1AndUser2(mExtraIdUSer1, mExtraIdUSer2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                if(size==0){
                    Toast.makeText(ChatActivity.this, "El chat no Existia", Toast.LENGTH_SHORT).show();
                    createChat();
                }
                else {
                    Toast.makeText(ChatActivity.this, "El chat Existe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createChat(){
        Chat chat = new Chat();
        // Usuario1  o el dueño de la sesión, usuario2 la persona con la que va a chatear
        chat.setIdUser1(mExtraIdUSer1);
        chat.setIdUser2 (mExtraIdUSer2);
        chat.setWriting(false);
        chat.setTimestamp(new Date().getTime());
        chat.setId(mExtraIdUSer1 + mExtraIdUSer2);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mExtraIdUSer1);
        ids.add(mExtraIdUSer2);
        chat.setIds(ids);
        mChatsProvider.create(chat);
    }





    // fin de clase activity
}