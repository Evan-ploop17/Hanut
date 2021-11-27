package com.example.hanut.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanut.R;
import com.example.hanut.adapters.CommentAdapter;
import com.example.hanut.adapters.SliderAdapter;
import com.example.hanut.models.Comment;
import com.example.hanut.models.SliderItem;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.CommentsProvider;
import com.example.hanut.providers.LikesProviders;
import com.example.hanut.providers.NotificationProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.providers.TokenProvider;
import com.example.hanut.providers.UserProvider;
import com.example.hanut.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    RecyclerView mRecyclerView;

    // ADAPTERS
    CommentAdapter mCommentAdapter;

    // Providers
    PostProvider mPostProvider;
    UserProvider mUserProvider;
    CommentsProvider mCommentsProvider;
    AuthProvider mAuthProvider;
    LikesProviders mLikesProvider;
    NotificationProvider mNotificationProvider ;
    TokenProvider mTokenProvider;

    String mExtraPostId;

    // Instancias de la vista
    TextView mTextViewTitle;
    TextView mTextViewDescription;
    TextView mTextViewCategory;
    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewRelativeTime;
    TextView mTextViewLikes;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;
    Button mBtnComprar;
    FloatingActionButton mFabComment;
    Toolbar mToolbar;

    String mIdUser = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mSliderView = findViewById(R.id.imageSlider);
        mTextViewTitle = findViewById(R.id.textViewTitle);
        mTextViewDescription = findViewById(R.id.textViewDescription);
        mTextViewCategory = findViewById(R.id.textViewCategory);
        mTextViewUsername = findViewById(R.id.textViewUserName);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mTextViewRelativeTime = findViewById(R.id.textViewRelativeTime);
        mTextViewLikes = findViewById(R.id.textViewLikes);
        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mButtonShowProfile = findViewById(R.id.btnShowProfile);
        mBtnComprar = findViewById(R.id.btnComprar);
        mFabComment = findViewById(R.id.fabComment);
        mRecyclerView = findViewById(R.id.recyclerViewComments);
        mToolbar = findViewById(R.id.toolbar);
        

        // Este código lo suamos para poder meter el toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // COn este código generamos in linear layout de xml y hacemos que las tarjetas se muestren una sobre la otra
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // PROVIDERS
        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();
        mCommentsProvider = new CommentsProvider();
        mAuthProvider = new AuthProvider();
        mLikesProvider = new LikesProviders();
        mNotificationProvider =  new NotificationProvider();
        mTokenProvider = new TokenProvider();

        mExtraPostId = getIntent().getStringExtra("id");
        mFabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogComment();
            }
        });
        mButtonShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToShowProfile();
                
            }
        });
        mBtnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBuy();
            }
        });

        getPost();
        getNumberLikes();
    }

    private void goToBuy() {
        if(!mIdUser.equals("")){
            Intent intent = new Intent(PostDetailActivity.this, PayActivity.class);
            intent.putExtra("idUser", mIdUser);
            startActivity(intent);
        }else{
            Toast.makeText(PostDetailActivity.this, "El Id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }
    }

    private void getNumberLikes() {
        // A diferencia del método onSucesListener, este método implementa los cambios en tiempo real de la BD
        mLikesProvider.getLikesByPost(mExtraPostId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes = value.size();
                mTextViewLikes.setText(numberLikes + " Me gustas");
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Acá vamos a hacer una consulta a la base de datos
        Query query = mCommentsProvider.getCommentsByPost(mExtraPostId);
        FirestoreRecyclerOptions<Comment> options =
                new FirestoreRecyclerOptions.Builder<Comment>()
                        .setQuery(query, Comment.class)
                        .build();
        mCommentAdapter =  new CommentAdapter( options, PostDetailActivity.this);
        mRecyclerView.setAdapter(mCommentAdapter);
        // DB en tiempo real entonces el metodo listening esta pendiente de esos cambios en tiempor real
        mCommentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCommentAdapter.stopListening();

    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("¡COMENTARIO!");
        alert.setMessage("Ingresa tu comentario");

        final EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Texto");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36, 0, 36, 36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);

        alert.setView(container);


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString();
                if (!value.isEmpty()) {
                    createComment(value);
                }
                else {
                    Toast.makeText(PostDetailActivity.this, "Debe ingresar el comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();
    }

    private void createComment(final String value) {
        Comment comment = new Comment();
        comment.setComment(value);
        comment.setIdPost(mExtraPostId);
        comment.setIdUser(mAuthProvider.getUid());
        comment.setTimestamp(new Date().getTime());
        mCommentsProvider.create(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //sendNotification(value);
                    Toast.makeText(PostDetailActivity.this, "El comentario se creo correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PostDetailActivity.this, "No se pudo crear el comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

/*    private void sendNotification(final String comment) {
        if (mIdUser == null) {
            return;
        }
        mTokenProvider.getToken(mIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("token")) {
                        String token = documentSnapshot.getString("token");
                        Map<String, String> data = new HashMap<>();
                        data.put("title", "NUEVO COMENTARIO");
                        data.put("body", comment);
                        FCMBody body = new FCMBody(token, "high", "4500s", data);
                        mNotificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().getSuccess() == 1) {
                                        Toast.makeText(PostDetailActivity.this, "La notificacion se envio correcatemente", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(PostDetailActivity.this, "La notificacion no se pudo enviar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(PostDetailActivity.this, "La notificacion no se pudo enviar", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(PostDetailActivity.this, "El token de notificaciones del usuario no existe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

 */

    private void goToShowProfile() {
        if(!mIdUser.equals("")){
            Intent intent = new Intent(PostDetailActivity.this, PostDetailActivity.class);
            intent.putExtra("idUser", mIdUser);
            startActivity(intent);
        }else{
            Toast.makeText(PostDetailActivity.this, "El Id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }
    }

    private void instanceSlider(){
        // todo esto viene de la libreria : https://github.com/smarteist/Android-Image-Slider
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        // Con esto se define el tipo de transicion que va a tener las imagenes 45 22.20
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        // Esta es una animación cuando volteamos de derecha a izquierda
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.WHITE);
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);
        // para que salte automaticamente entre cada imágen
        mSliderView.setAutoCycle(true);
        // Hace la transición de imágenes automaticamente. Se van corriendo solas en la pantalla
        mSliderView.startAutoCycle();
    }

    private void getPost(){
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("image1")){
                        String image1 = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image1);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("image2")){
                        String image2 = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image2);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("title")){
                        String title = documentSnapshot.getString("title");
                        mTextViewTitle.setText(title.toUpperCase());
                    }
                    if(documentSnapshot.contains("description")){
                        String description = documentSnapshot.getString("description");
                        mTextViewDescription.setText(description);
                    }
                    if(documentSnapshot.contains("category")){
                        String category = documentSnapshot.getString("category");
                        mTextViewCategory.setText(category.toUpperCase());
                    }
                    if(documentSnapshot.contains("idUser")){
                        mIdUser = documentSnapshot.getString("idUser");
                        getUserInfo(mIdUser);
                    }
                    if(documentSnapshot.contains("timestamp")){
                        long timestamp = documentSnapshot.getLong("timestamp");
                        String relativeTime = RelativeTime.getTimeAgo(timestamp, PostDetailActivity.this);
                        mTextViewRelativeTime.setText("Publicado " + relativeTime);
                    }
                    instanceSlider();
                }
            }
        });
    }

    private void getUserInfo(String idUser) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("userName")){
                        String userName = documentSnapshot.getString("userName");
                        mTextViewUsername.setText(userName);
                    }
                    if(documentSnapshot.contains("phone")){
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhone .setText(phone);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        Picasso.with(PostDetailActivity.this).load(imageProfile).into(mCircleImageViewProfile);
                    }
                }
            }
        });
    }
}