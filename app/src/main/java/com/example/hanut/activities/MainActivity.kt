package com.example.hanut.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hanut.R
import com.example.hanut.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.auth_activity.*

class MainActivity : AppCompatActivity() {
    enum class ProviderType{
        BASIC,
        GOOGLE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Action bar inferior
        var bottomNavigation: BottomNavigationView?
        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        openFragment(HomeFragment())

        /*val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()*/

        // Lista por defecto
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
        // val actionToolBar: ActionBarHanut = ActionBarHanut()
        // actionToolBar.callActionBar(topAppBar, this)
    }

    /*
    private fun setUp(email:String, provider:String){
        // Borrar datos en caso de que se cierren las preferencias:
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
     */

    fun openFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    var navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemHome -> {
                    openFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemFilters -> {
                    //openFragment(FilterFragment())
                    //return@OnNavigationItemSelectedListener true
                }
                R.id.itemChat -> {
                    //openFragment(ChatFragment())
                    //return@OnNavigationItemSelectedListener true
                }
                R.id.itemProfile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                    return@OnNavigationItemSelectedListener true
                }
            }
            // Este true me permite mostrar el nombre del fragmento en bottom menu
            true
        }
}

