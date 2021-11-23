package com.example.hanut.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import android.widget.Toast;

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
import com.mancj.materialsearchbar.MaterialSearchBar;

public class HomeFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener{

    View mView;
    FloatingActionButton mFab;
    MaterialSearchBar mSearchBar;
    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;
    PostsAdapter mPostsAdapterSearch;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab = mView.findViewById(R.id.fab);

        mSearchBar =  mView.findViewById(R.id.searchBar);

        mRecyclerView = mView.findViewById(R.id.recyclerViewHome );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.itemLogout){
                    logOut();
                }
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });
        return mView;
    }

    private void searchByTitle(String title){
        Query query = mPostProvider.getPostByTitle(title);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mPostsAdapterSearch =  new PostsAdapter(options, getContext());
        mPostsAdapterSearch.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostsAdapterSearch);
        // DB en tiempo real entonces el metodo listening esta pendiente de esos cambios en tiempor real
        mPostsAdapterSearch.startListening();
    }

    private void getAllPost(){
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mPostsAdapter =  new PostsAdapter(options, getContext());
        mPostsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostsAdapter);
        mPostsAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPost();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Para que deje de escuchar los cambios de la BD. cuando este en segundo plano
        mPostsAdapter.stopListening();

        if(mPostsAdapterSearch != null){
            mPostsAdapterSearch.stopListening();
        }
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), PostActivity.class );
        startActivity(intent);
    }


    private void logOut() {
        mAuthProvider.logout();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags( intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        // con esta validacion hacemos que al dar clic en la flecha de atras vuelva a traer todos los post
        if(!enabled){
            getAllPost();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString().toUpperCase() );
        Toast.makeText(getContext(), "Tu busqueda fue: " + text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}