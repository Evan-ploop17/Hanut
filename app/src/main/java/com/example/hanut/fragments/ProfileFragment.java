package com.example.hanut.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hanut.R;
import com.example.hanut.activities.EditProfileActivity;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
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

    ImageView mImageCover;
    CircleImageView mImageViewProfile;

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
        mImageCover = mView.findViewById(R.id.imageViewCover);
        mImageViewProfile = mView.findViewById(R.id.circleImageProfile);

        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEdirProfile);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProfile();
            }
        });
        // PROVIDERS
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        // CARGAR LOS DATOS DEL USUARIO EN SU PERFIL
        getUser();
        // Obtener numero de publicaciones
        getPostNumber();
        return mView;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getPostNumber(){
        mPostProvider .getPostByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Esto devuelve la cantidad de elementos que se obtienen de la consulta
                int numberPost = queryDocumentSnapshots.size();
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