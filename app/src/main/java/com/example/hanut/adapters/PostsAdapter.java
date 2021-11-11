package com.example.hanut.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanut.R;
import com.example.hanut.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder > {

    Context context;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
    }

    // En este método establecemos el contenido que queremos que se muestre en cada una de lass vistas. Video 38
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        holder.textViewTitle.setText(post.getTitle());
        holder.textViewDescription.setText(post.getDescription());

        // Las validaciones se hacen en caso de que las imágenes vengan nulas no se rompa la app
        if(post.getImage1() != null){
            if( !post.getImage1().isEmpty() ){
                // Para mostrar las imagenes que guarda firebase necesitamos usar picaso
                // Picasso.with recibe un contexto como no lo tenemos debemos crear un contexto y un constructor para asignarlo
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);
            }
        }
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

        public ViewHolder(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
        }

    }

}
