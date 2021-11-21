package com.example.hanut.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanut.R;
import com.example.hanut.activities.PostDetailActivity;
import com.example.hanut.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder > {

    Context context;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
    }

    // En este método establecemos el contenido que queremos que se muestre en cada una de lass vistas. V38
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        holder.textViewTitle.setText(post.getTitle());
        holder.textViewDescription.setText(post.getDescription());

        if(post.getImage1() != null){
            if( !post.getImage1().isEmpty() ){
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);
            }
        }
        // Almacenamos en documento Todo el documento de la base de datos
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();
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
        ImageView imageViewPost;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            viewHolder = view;
        }

    }

}
