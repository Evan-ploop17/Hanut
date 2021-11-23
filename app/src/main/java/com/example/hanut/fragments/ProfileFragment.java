package com.example.hanut.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hanut.R;
import com.example.hanut.activities.EditProfileActivity;
import com.example.hanut.adapters.MyPostsAdapter;
import com.example.hanut.models.Post;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    LinearLayout mLinearLayoutEditProfile;
    View mView;
    // PROVIDERS
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    // ELEMENTOS VISUALES
    TextView mTextViewUserName;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    TextView mTextViewPostExist;
    ImageView mImageCover;
    CircleImageView mImageViewProfile;
    RecyclerView mRecyclerViewMyPost;
    // Adapter
    MyPostsAdapter mAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mTextViewUserName = mView.findViewById(R.id.textViewUserName);
        mTextViewPhone = mView.findViewById(R.id.textViewPhone);
        mTextViewEmail = mView.findViewById(R.id.textViewEmail);
        mTextViewPostNumber = mView.findViewById(R.id.textViewPostNumber);
        mTextViewPostExist = mView.findViewById(R.id.textViewPostExist);
        mImageCover = mView.findViewById(R.id.imageViewCover);
        mImageViewProfile = mView.findViewById(R.id.circleImageProfile);

        mRecyclerViewMyPost = mView.findViewById(R.id.recyclerViewMyPost);
        // COn este código generamos in linear layout de xml y hacemos que las tarjetas se muestren una sobre la otra
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewMyPost.setLayoutManager(linearLayoutManager);

        // PROVIDERS
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();



        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEdirProfile);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProfile();
            }
        });
        // CARGAR LOS DATOS DEL USUARIO EN SU PERFIL
        getUser();
        // Obtener numero de publicaciones
        getPostNumber();
        // método para saber si el usaurio tiene alguna poblicación
        checkIfExistPost();
        return mView;
    }

    private void checkIfExistPost() {
        mPostProvider.getPostByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        Query query = mPostProvider.getPostByUser(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mAdapter = new MyPostsAdapter(options, getContext());
        mRecyclerViewMyPost.setAdapter(mAdapter);
        mAdapter .startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getPostNumber(){
        mPostProvider .getPostByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // Esto devuelve la cantidad de elementos que se obtienen de la consulta
                int numberPost = value.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }
    
     private void getUser(){
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                Picasso.with(getContext()).load(imageProfile).into(mImageViewProfile);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        String imageCover = documentSnapshot.getString("image_cover");
                        if( imageCover != null){
                            if(!imageCover.isEmpty()){
                                Picasso.with(getContext()).load(imageCover).into(mImageCover);
                            }
                        }
                    }
                }
            }
        });
     }
}