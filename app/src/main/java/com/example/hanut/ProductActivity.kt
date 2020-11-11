package com.example.hanut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
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


        val actionToolBar: com.example.hanut.ActionBarHanut = com.example.hanut.ActionBarHanut()
        actionToolBar.callActionBar(topAppBar, this)


//        topAppBar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.search -> {
//                    // Handle search icon press
//
//                    val intent = Intent(this, SearchActivity::class.java).apply {
//                        putExtra("search", "" )
//                    }
//                    startActivity(intent)
//                    true
//                }
//
//                R.id.Porfile -> {
//                    // Handle search icon press
//
//                    val intent = Intent(this, Profile::class.java)
//                    startActivity(intent)
//                    true
//                }
//
//                else -> false
//            }
//        }

        buyBtn.setOnClickListener{
            val confirmPurchase = Intent(this, ConfirmPurchase::class.java)
            startActivity(confirmPurchase)
        }

    }








}