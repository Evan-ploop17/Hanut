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
import com.example.hanut.models.Comment;
import com.example.hanut.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder > {

    Context context;
    UserProvider mUserProvider;

    public CommentAdapter(FirestoreRecyclerOptions<Comment> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
    }

    // En este método establecemos el contenido que queremos que se muestre en cada una de lass vistas. V51
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String commentId = document.getId();
        String userId = document.getString("idUser");

        holder.textViewComment.setText(comment.getComment());
        getUserInfo(userId, holder);
    }

    private void getUserInfo(String idUser, ViewHolder holder) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    if(documentSnapshot.contains("userName")){
                        String userName = documentSnapshot.getString("userName");
                        holder.textViewUserName.setText(userName.toUpperCase());
                    }

                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile =  documentSnapshot.getString("image_profile");
                        if(imageProfile != null){
                            if( !imageProfile.isEmpty() ){
                                Picasso.with(context).load(imageProfile).into(holder.circleImageComment);
                            }
                        }
                    }

                }
            }
        });
    }

    // Acá instanciamos la vista que queremos usar
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comment, parent, false );
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserName;
        TextView textViewComment;
        CircleImageView circleImageComment;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textViewUserName = view.findViewById(R.id.textViewUsername);
            textViewComment = view.findViewById(R.id.textViewComment);
            circleImageComment = view.findViewById(R.id.circleImageComment);
            viewHolder = view;
        }

    }

}
