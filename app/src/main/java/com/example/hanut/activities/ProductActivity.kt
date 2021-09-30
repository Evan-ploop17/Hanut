package com.example.hanut.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.hanut.R
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.topAppBar

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val  window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        val product = intent.getSerializableExtra("product") as Product
        title = product.name

        nameText.text = product.name
        descriptionText.text = product.description
        priceText.text = "$ ${product.price}"

        val imagen = product.image
        imageView.setImageResource(imagen)

        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            Toast.makeText(this, "Un mensaje jeje", Toast.LENGTH_LONG).show()
        }

        // Llamamos funcionalidad del ActionBar
        val actionToolBar: ActionBarHanut = ActionBarHanut()
        actionToolBar.callActionBar(topAppBar, this)


        buyBtn.setOnClickListener{
            val confirmPurchase = Intent(this, ConfirmPurchase::class.java)
            startActivity(confirmPurchase)
        }

    }








}