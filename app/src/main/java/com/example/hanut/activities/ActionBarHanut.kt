package com.example.hanut.activities

import android.app.Activity
import android.content.Intent
import com.example.hanut.R
import com.google.android.material.appbar.MaterialToolbar

class ActionBarHanut {

    fun callActionBar(actionBar: MaterialToolbar, context: Activity){

        actionBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press
                    //val intent = Intent(context, SearchActivity::class.java)
                    //context.startActivity(intent)
                    //startActivity(intent)
                    true
                }

                R.id.Porfile -> {
                    // Handle search icon press
                    val intent = Intent(context, Profile::class.java)
                    context.startActivity(intent)
                    //startActivity(intent)
                    true
                }
                else -> false
            }
        }


    }

}