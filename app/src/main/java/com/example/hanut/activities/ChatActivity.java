package com.example.hanut.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanut.R;
import com.example.hanut.adapters.MessageAdapter;
import com.example.hanut.models.Chat;
import com.example.hanut.models.Message;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.ChatsProvider;
import com.example.hanut.providers.MessageProvider;
import com.example.hanut.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    String mExtraIdUSer1;
    String mExtraIdUSer2;
    String mExtraIdChat;

    ChatsProvider mChatsProvider;
    MessageProvider mMessagesProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    EditText mEditTextMessage;
    ImageView mImageViewSendMessage;
    CircleImageView mCircleImageVIew;
    TextView mTextViewUserName;
    TextView mTextViewRelativeTime;
    ImageView mImageViewBack;

    RecyclerView mRecyclerMessage;
    MessageAdapter mAdapter;

    View mActionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatsProvider = new ChatsProvider();
        mMessagesProvider = new MessageProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mExtraIdUSer1 = getIntent().getStringExtra("idUser1");
        mExtraIdUSer2 = getIntent().getStringExtra("idUser2");
        mExtraIdChat = getIntent().getStringExtra("idChat");


        showCustomToolbar(R.layout.custom_chat_toolbar);

        mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        mRecyclerMessage = findViewById(R.id.recyclerViewMessage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerMessage.setLayoutManager(linearLayoutManager);

        mImageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });



        checkIfChatExist();

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mMessagesProvider.getMessageByChat(mExtraIdChat);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        mAdapter = new MessageAdapter(options, ChatActivity.this);
        mRecyclerMessage.setAdapter(mAdapter);
        mAdapter .startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
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

        mCircleImageVIew = mActionBarView.findViewById(R.id.circleImageProfile);
        mTextViewUserName = mActionBarView.findViewById(R.id.textViewUserName);
        mTextViewRelativeTime = mActionBarView.findViewById(R.id.customRelativeTime);
        mImageViewBack = mActionBarView.findViewById(R.id.imageViewBack);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUserInfo();

    }

    private void getUserInfo() {
        String idUserInfo = "";
        if(mAuthProvider.getUid().equals(mExtraIdUSer1)){
            idUserInfo = mExtraIdUSer2;
        }
        else{
            idUserInfo = mExtraIdUSer1;
        }
        mUserProvider.getUser(idUserInfo).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if (documentSnapshot.contains("userName")){
                        String userName = documentSnapshot.getString("userName");
                        mTextViewUserName.setText(userName);
                    }

                    if (documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if(imageProfile != null){
                            if(!imageProfile.equals("")){
                                Picasso.with(ChatActivity.this).load(imageProfile).into(mCircleImageVIew);
                            }
                        }
                    }
                }
            }
        });
    }

    private void checkIfChatExist(){
        mChatsProvider.getChatByUSer1AndUser2(mExtraIdUSer1, mExtraIdUSer2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                if(size==0){
                    createChat();
                }
                else {
                    mExtraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
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