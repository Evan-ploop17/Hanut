package com.example.hanut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val product = intent.getSerializableExtra("product") as Product

        title = product.name
        val  window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)



        //supportActionBar.setBackgroundDrawable()

        nameText.text = product.name
        descriptionText.text = product.description
        priceText.text = "$ ${product.price}"
        val imagen = product.image
        imageView.setImageResource(imagen)

        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            Toast.makeText(this, "Un mensaje jeje", Toast.LENGTH_LONG).show()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press

                    val intent = Intent(this, SearchActivity::class.java).apply {
                        putExtra("search", "" )
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }








}