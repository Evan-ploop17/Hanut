package com.example.hanut.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanut.R;
import com.example.hanut.activities.PostDetailActivity;
import com.example.hanut.models.Like;
import com.example.hanut.models.Post;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.LikesProviders;
import com.example.hanut.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder > {

    Context context;
    UserProvider mUserProvider;
    LikesProviders mLikesProvider;
    AuthProvider mAuthProvider;
    TextView mTextViewNumberFilter;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        this.mUserProvider = new UserProvider();
        this.mLikesProvider = new LikesProviders();
        this.mAuthProvider = new AuthProvider();
    }

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context, TextView textView){
        super(options);
        this.context = context;
        this.mUserProvider = new UserProvider();
        this.mLikesProvider = new LikesProviders();
        this.mAuthProvider = new AuthProvider();
        this.mTextViewNumberFilter = textView;
    }


    // En este método establecemos el contenido que queremos que se muestre en cada una de lass vistas. V38
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        if(mTextViewNumberFilter != null){
            int numberFilter = getSnapshots().size();
            mTextViewNumberFilter.setText(String.valueOf(numberFilter));
        }

        // Almacenamos en documento Todo el documento de la base de datos
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();
        holder.textViewTitle.setText(post.getTitle().toUpperCase());
        holder.textViewDescription.setText(post.getDescription());
        if(post.getImage1() != null){
            if( !post.getImage1().isEmpty() ){
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);
            }
        }
        // Con esto podemos hacer que al dar clic en una publicacion se habrá la actividad de detalle V45
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Le damos funcionalidad cuando el usuario le de tap a cualquier parte de la tarjeta (publicación)
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id",postId);
                context.startActivity(intent);
            }
        });

        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like like = new Like();
                like.setIdPost(postId);
                like.setIdUser(mAuthProvider.getUid());
                like.setTimestamp(new Date().getTime());

                like(like, holder); 
            }
        });

        getUserInfo(post.getIdUser(), holder);
        getNumberLikesByPost(postId, holder);
        checkIfExistLike(postId, mAuthProvider.getUid(), holder);
    }

    private void like(Like like, ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();

                if(numberDocuments > 0 ){
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLike.setImageResource(R.drawable.ic_like_grey_96);
                    mLikesProvider.delete(idLike);
                }else{
                    holder.imageViewLike.setImageResource(R.drawable.ic_like_blue_96);
                    mLikesProvider.create(like);
                }
            }
        });
    }

    private void checkIfExistLike(String isPost, String idUser , ViewHolder holder) {

        mLikesProvider.getLikeByPostAndUser(isPost, idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();

                if(numberDocuments > 0 ){
                    holder.imageViewLike.setImageResource(R.drawable.ic_like_blue_96);
                }else{
                    holder.imageViewLike.setImageResource(R.drawable.ic_like_grey_96);
                }
            }
        });
    }

    private void getNumberLikesByPost(String idPost, ViewHolder holder){
        mLikesProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes =  value.size();
                holder.textViewLikes.setText(String.valueOf(numberLikes)+" Me gustas");
            }
        });
    }


    private void getUserInfo(String idUser, ViewHolder holder) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("userName")){
                        String userName = documentSnapshot.getString("userName");
                        holder.textViewUserNamePostCard.setText("Por: " + userName.toUpperCase());
                    }
                }
            }
        });
    }


    // Acá instanciamos la vista que queremos usar
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false );
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewUserNamePostCard;
        TextView textViewLikes;
        ImageView imageViewPost;
        ImageView imageViewLike;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            textViewUserNamePostCard = view.findViewById(R.id.textViewUserNamePostCard);
            textViewLikes = view.findViewById(R.id.textViewLikes);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            viewHolder = view;
        }

    }

}
