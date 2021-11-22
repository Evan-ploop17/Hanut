package com.example.hanut.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanut.R;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    // ELEMENTOS VISUALES
    TextView mTextViewUserName;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    // PROVIDERS
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    // parametro para recibir el id de la persona que publico el post, no del que esta logueado
    String mExtraIdUser;

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
        mImageCover = findViewById(R.id.imageViewCover);
        mImageViewProfile = findViewById(R.id.circleImageProfile);
        // PROVIDERS
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        // obtenermos el ID del usaurio que posteo la ublicación
        mExtraIdUser = getIntent().getStringExtra("idUser");

        getUser();
        getPostNumber();
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
}