package com.example.hanut.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hanut.R
import com.example.hanut.fragments.FiltersFragment
import com.example.hanut.fragments.HomeFragment
import com.example.hanut.fragments.ProfileFragment
import com.example.hanut.providers.AuthProvider
import com.example.hanut.providers.TokenProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.auth_activity.*

class HomeActivity : AppCompatActivity() {


    enum class ProviderType{
        BASIC,
        GOOGLE
    }

    val mTokenProvider = TokenProvider();
    val mAuthProvider = AuthProvider();

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // Action bar inferior
        var bottomNavigation: BottomNavigationView?


        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        openFragment(HomeFragment())


        createToken()
    }


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
                    openFragment(FiltersFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemChat -> {
                    //openFragment(ChatFragment())
                    //return@OnNavigationItemSelectedListener true
                }
                R.id.itemProfile -> {
                    openFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            // Este true me permite mostrar el nombre del fragmento en bottom menu
            true
        }

    private fun createToken(){
        mTokenProvider.create(mAuthProvider.uid)
    }

}

