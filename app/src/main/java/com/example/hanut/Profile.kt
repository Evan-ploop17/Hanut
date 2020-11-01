package com.example.hanut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_porfile.*

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_porfile)

        val  window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        val product = Product(R.drawable.ropa, "Ropa", 100.0, "Algodon suave" )
        val product1 = Product(R.drawable.hanut, "Hanut", 100.0, "Algodon suave" )

        var listProduct = listOf<Product>(product, product1)
        val adapter = ProductAdapter(this, listProduct)
        listHome.adapter = adapter

    }
}