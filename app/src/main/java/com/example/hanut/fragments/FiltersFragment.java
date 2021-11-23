package com.example.hanut.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hanut.R;
import com.example.hanut.activities.FiltersActivity;
import com.google.android.material.textfield.TextInputEditText;

public class FiltersFragment extends Fragment {

    View mView;
    TextInputEditText mTextInputFilter;
    Button mButtonFilter;

    public FiltersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_filters, container, false);

        mTextInputFilter = mView.findViewById(R.id.textInputFilter);
        mButtonFilter = mView.findViewById(R.id.btnSearchFilter);

        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mCategory = mTextInputFilter.getText().toString();
                goToFilterActivity(mCategory);
            }
        });

        return mView;
    }

    private void goToFilterActivity(String category){
        Intent intent = new Intent(getContext(), FiltersActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}