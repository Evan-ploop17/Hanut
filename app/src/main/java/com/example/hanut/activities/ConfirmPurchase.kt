package com.example.hanut.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.hanut.R
import kotlinx.android.synthetic.main.activity_confirm_purchase.*

class ConfirmPurchase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_purchase)

        val  window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        address.text
        phone.text
        city.text
        quantity.text

        send.setOnClickListener{
            Toast.makeText(this, "Información almacenada en base de datos", Toast.LENGTH_LONG).show()
            //val savePurchase = com.example.hanut.dao.Purchase
        }
    }

}