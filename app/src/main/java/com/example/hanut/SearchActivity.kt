package com.example.hanut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    fun search(v:View){
        Toast.makeText(this, "Vamos a buscar en base de datos", Toast.LENGTH_LONG).show()
    }
}