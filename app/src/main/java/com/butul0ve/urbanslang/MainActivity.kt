package com.butul0ve.urbanslang

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.butul0ve.urbanslang.mvp.main.MainFragment
import com.butul0ve.urbanslang.utils.convertToFragment

private const val FRAGMENT_KEY = "fragment_extra_key"
private const val ARGS_KEY = "arguments_extra_key"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction().apply {
                    replace(R.id.frame_layout, MainFragment())
                    commit()
                }
        } else if (savedInstanceState.containsKey(FRAGMENT_KEY) && savedInstanceState.containsKey(ARGS_KEY)) {
            val className = savedInstanceState.getString(FRAGMENT_KEY)
            val fragment = className.convertToFragment()
            fragment.arguments = savedInstanceState.getBundle(ARGS_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val manager = supportFragmentManager
        val currentFragment = manager.findFragmentById(R.id.frame_layout)
        if (currentFragment != null) {
            val args = currentFragment.arguments
            outState?.putString(FRAGMENT_KEY, currentFragment::class.java.simpleName)
            outState?.putBundle(ARGS_KEY, args)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        TODO("hide the keyboard, add the fragment to the backstack and open it")
        return false
    }

    private fun initUI() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        navigationView.setNavigationItemSelectedListener(this)
        toggle.syncState()
    }
}
