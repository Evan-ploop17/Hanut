package com.example.hanut.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanut.R;
import com.example.hanut.adapters.MyPostsAdapter;
import com.example.hanut.adapters.PostsAdapter;
import com.example.hanut.models.Post;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class FiltersActivity extends AppCompatActivity {


    String mExtraCategory;

    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mAdapter;

    TextView mTextViewNumberFilter;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        // para configurar el regreso en el toolbar
        mTextViewNumberFilter = findViewById(R.id.textViewNumberFilter);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Filtrado por categoria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerViewFilter);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FiltersActivity.this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(FiltersActivity.this, 2));

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        // Valor que se paso para la categoria
        mExtraCategory = getIntent().getStringExtra("category") ;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = mPostProvider.getPostByCategoryAndTimestamp(mExtraCategory.toUpperCase());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mAdapter =  new PostsAdapter(options, FiltersActivity.this, mTextViewNumberFilter );
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    // Con esto la  flecha de voler me deja exactamente en el ultimo fragmen seleccionado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

}