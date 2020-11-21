package com.example.hanut

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        val product = Product(R.drawable.ropa, "Ropa", 100.0, "Algodon suave" )
        val product1 = Product(R.drawable.hanut, "Hanut", 100.0, "Algodon suave" )

        var listProduct = listOf<Product>(product, product1)
        val adapter = ProductAdapter(this, listProduct)
        listHome.adapter = adapter

        listHome.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, ProductActivity::class.java)
            intent.putExtra("product" , listProduct[i])
            startActivity(intent)
        }

        // Lllamamos la funcionalidad del action bar
        val actionToolBar: com.example.hanut.ActionBarHanut = com.example.hanut.ActionBarHanut()
        actionToolBar.callActionBar(topAppBar, this)
    }
}

