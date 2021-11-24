package com.example.hanut.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanut.R;
import com.example.hanut.adapters.MyPostsAdapter;
import com.example.hanut.models.Post;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    // ELEMENTOS VISUALES
    TextView mTextViewUserName;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    TextView mTextViewPostExist;
    Toolbar mToolbar;
    FloatingActionButton mFabChat;

    // PROVIDERS
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    // parametro para recibir el id de la persona que publico el post, no del que esta logueado
    String mExtraIdUser;

    RecyclerView mRecyclerViewMyPost;
    // Adapter
    MyPostsAdapter mAdapter;

    ImageView mImageCover;
    CircleImageView mImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // ELEMENTOS VISUALES
        mTextViewUserName = findViewById(R.id.textViewUserName);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mTextViewEmail = findViewById(R.id.textViewEmail);
        mTextViewPostNumber = findViewById(R.id.textViewPostNumber);
        mTextViewPostExist = findViewById(R.id.textViewPostExist);
        mToolbar = findViewById(R.id.toolbar);
        mFabChat = findViewById(R.id.fabChat);


        mFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChatActivity();
            }
        });

        // Este código lo suamos para poder meter el toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mImageCover = findViewById(R.id.imageViewCover);
        mImageViewProfile = findViewById(R.id.circleImageProfile);
        // PROVIDERS
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        // obtenermos el ID del usaurio que posteo la ublicación
        mExtraIdUser = getIntent().getStringExtra("idUser");

        mRecyclerViewMyPost = findViewById(R.id.recyclerViewMyPost);
        // COn este código generamos in linear layout de xml y hacemos que las tarjetas se muestren una sobre la otra
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecyclerViewMyPost.setLayoutManager(linearLayoutManager);


        if(mAuthProvider.getUid().equals(mExtraIdUser)){
            // Cualquiera de los dos métodos
            mFabChat.setVisibility(View.GONE);
            // mFabChat.setEnabled(false);
        }

        getUser();
        getPostNumber();
        checkIfExistPost();
    }







    private void goToChatActivity() {
        Intent intent = new Intent(UserProfileActivity.this, ChatActivity.class);
        intent.putExtra("idUser1", mAuthProvider.getUid());
        intent.putExtra("idUser2", mExtraIdUser );
        startActivity(intent);

    }

    private void checkIfExistPost() {
        mPostProvider.getPostByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberPost = value.size();
                if(numberPost > 0){
                    mTextViewPostExist.setText("Publicaciones");
                    mTextViewPostExist.setTextColor(Color.RED);
                }
                else{
                    mTextViewPostExist.setText("No hay publicaciones");
                    mTextViewPostExist.setTextColor(Color.GRAY);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mExtraIdUser);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mAdapter = new MyPostsAdapter(options, UserProfileActivity.this);
        mRecyclerViewMyPost.setAdapter(mAdapter);
        mAdapter .startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void getPostNumber(){
        mPostProvider .getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Esto devuelve la cantidad de elementos que se obtienen de la consulta
                int numberPost = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }

    private void getUser(){
        mUserProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("email")){
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if(documentSnapshot.contains("phone")){
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhone.setText(phone);
                    }
                    if(documentSnapshot.contains("userName")){
                        String userName = documentSnapshot.getString("userName");
                        mTextViewUserName.setText(userName);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if( imageProfile != null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(UserProfileActivity.this).load(imageProfile).into(mImageViewProfile);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        String imageCover = documentSnapshot.getString("image_cover");
                        if( imageCover != null){
                            if(!imageCover.isEmpty()){
                                Picasso.with(UserProfileActivity.this).load(imageCover).into(mImageCover);
                            }
                        }
                    }
                }
            }
        });
    }


    // Este método se usa para la flecha que "back que no ha sido implementada"
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}