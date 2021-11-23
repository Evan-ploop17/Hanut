package com.example.hanut.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.hanut.providers.PostProvider;
import com.example.hanut.providers.UserProvider;
import com.example.hanut.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostsAdapter extends FirestoreRecyclerAdapter<Post, MyPostsAdapter.ViewHolder > {

    Context context;
    UserProvider mUserProvider;
    LikesProviders mLikesProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    public MyPostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        this.mUserProvider = new UserProvider();
        this.mLikesProvider = new LikesProviders();
        this.mAuthProvider = new AuthProvider();
        this.mPostProvider = new PostProvider();
    }




    // En este método establecemos el contenido que queremos que se muestre en cada una de lass vistas. V38
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        // Almacenamos en documento Todo el documento de la base de datos
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();
        String relativeTime = RelativeTime.getTimeAgo(post.getTimestamp(), context);
        holder.textViewRelativeTime.setText(relativeTime);
        holder.textViewTitleMyPost.setText(post.getTitle().toUpperCase());
        if(post.getIdUser().equals(mAuthProvider.getUid())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if(post.getImage1() != null){
            if( !post.getImage1().isEmpty() ){
                Picasso.with(context).load(post.getImage1()).into(holder.circleImageMyPost);
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

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDelete(postId);
                // deletePost(postId);
            }
        });
    }

    private void showConfirmDelete(String postId) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_warning_grey_50)
                .setTitle("Eliminar publicación")
                .setMessage("¿Estas seguro de eliminar la publicación?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePost(postId);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deletePost(String postId) {
        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Se elimino correctamente", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "NOO se elimino correctamente", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    // Acá instanciamos la vista que queremos usar
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false );
        return new ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitleMyPost;
        TextView textViewRelativeTime;
        ImageView imageViewDelete;
        CircleImageView circleImageMyPost;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textViewTitleMyPost = view.findViewById(R.id.textViewTitleMyPost);
            textViewRelativeTime = view.findViewById(R.id.textViewRelativeTimeMyPost);
            circleImageMyPost = view.findViewById(R.id.circleImageMyPost);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteMyPost);
            viewHolder = view;
        }

    }

}
