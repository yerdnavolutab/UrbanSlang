package com.butul0ve.urbanslang

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.FragmentCallback
import com.butul0ve.urbanslang.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityWithBottomBar: AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_with_bottom_bar)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val controller = bottomNavigationView.setupWithNavController(
            listOf(
                R.navigation.random1,
                R.navigation.search2,
                R.navigation.fav3,
                R.navigation.cache4
            ),
            supportFragmentManager,
            R.id.nav_fragment,
            Intent()
        )

        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })

        currentNavController = controller
    }
}