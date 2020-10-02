package com.example.hanut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val product = intent.getSerializableExtra("product") as Product

        title = product.name

        //supportActionBar.setBackgroundDrawable()

        nameText.text = product.name
        descriptionText.text = product.description
        priceText.text = "$ ${product.price}"
        val imagen = product.image
        imageView.setImageResource(imagen)
    }


}