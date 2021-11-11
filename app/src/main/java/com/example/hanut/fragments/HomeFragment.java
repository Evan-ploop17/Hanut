package com.example.hanut.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hanut.R;
import com.example.hanut.activities.AuthActivity;
import com.example.hanut.activities.MainActivity;
import com.example.hanut.activities.PostActivity;
import com.example.hanut.adapters.PostsAdapter;
import com.example.hanut.models.Post;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    View mView;
    FloatingActionButton mFab;
    Toolbar mToolbar;
    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab = mView.findViewById(R.id.fab);
        mToolbar = mView.findViewById(R.id.toolbar);
        mRecyclerView = mView.findViewById(R.id.recyclerViewHome );

        // COn este código generamos in linear layout de xml y hacemos que las tarjetas se muestren una sobre la otra
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Este código lo suamos para poder meter el toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.publicaciones);
        setHasOptionsMenu(true);

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Acá vamos a hacer una consulta a la base de datos
        // La logica de las consultas va en el pauqete de los providers
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mPostsAdapter =  new PostsAdapter(options, getContext());
        mRecyclerView.setAdapter(mPostsAdapter);
        // DB en tiempo real entonces el metodo listening esta pendiente de esos cambios en tiempor real
        mPostsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Para que deje de escuchar los cambios de la BD. cuando este en segundo plano
        mPostsAdapter.stopListening();
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), PostActivity.class );
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemLogout){
            logOut();
        }
        return true;
    }

    private void logOut() {
        mAuthProvider.logout();
        Intent intent = new Intent(getContext(), AuthActivity.class);
        intent.setFlags( intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }
}