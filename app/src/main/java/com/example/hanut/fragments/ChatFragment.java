package com.example.hanut.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hanut.R;
import com.example.hanut.adapters.ChatsAdapter;
import com.example.hanut.models.Chat;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.ChatsProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {


    ChatsAdapter mAdapter;
    RecyclerView mRecyclerView;
    View mView;

    ChatsProvider mChatsProvider;
    AuthProvider mAuthProvider;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerViewChats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mChatsProvider = new ChatsProvider();
        mAuthProvider = new AuthProvider();

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mChatsProvider.getAll(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Chat> options =
                new FirestoreRecyclerOptions.Builder<Chat>()
                        .setQuery(query, Chat.class)
                        .build();
        mAdapter = new ChatsAdapter(options, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}