package com.example.hanut.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanut.R;
import com.example.hanut.models.Message;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.UserProvider;
import com.example.hanut.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ViewHolder > {

    Context context;
    UserProvider mUsersProvider;
    AuthProvider mAuthProvider;


    public MessageAdapter(FirestoreRecyclerOptions<Message> options, Context context) {
        super(options);
        this.context = context;
        mUsersProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String messageId = document.getId();
        holder.textViewMessage.setText(message.getMessage());
        String relativeTime = RelativeTime.getTimeAgo(message.getTimestamp(), context);
        holder.textViewDate.setText(relativeTime);

        if(message.getIdSender().equals(mAuthProvider.getUid())){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(150, 0, 0, 0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(30, 20, 25, 20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.rounded_linear_layout));
            holder.imageViewViewed.setVisibility(View.VISIBLE);
            holder.textViewMessage.setTextColor(Color.WHITE);
            holder.textViewDate.setTextColor(Color.LTGRAY);
        }else{

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0, 0, 150, 0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(30, 20, 30, 20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.rounded_linear_layout_gray));
            holder.imageViewViewed.setVisibility(View.GONE);
            holder.textViewMessage.setTextColor(Color.DKGRAY);
            holder.textViewDate.setTextColor(Color.DKGRAY);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewDate;
        ImageView imageViewViewed;
        LinearLayout linearLayoutMessage;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDateMessage);
            imageViewViewed = view.findViewById(R.id.textViewViewedMessage);
            linearLayoutMessage = view.findViewById(R.id.linearLayoutMessage);
            viewHolder = view;
        }
    }

}
