package com.butul0ve.urbanslang

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.FragmentCallback
import com.butul0ve.urbanslang.mvp.cache.CacheFragment
import com.butul0ve.urbanslang.mvp.detail.DetailFragment
import com.butul0ve.urbanslang.mvp.favorites.FavoritesFragment
import com.butul0ve.urbanslang.mvp.main.MainFragment
import com.butul0ve.urbanslang.mvp.trends.TrendsFragment
import com.butul0ve.urbanslang.utils.convertToFragment
import com.butul0ve.urbanslang.utils.hideKeyboard

private const val FRAGMENT_KEY = "fragment_extra_key"
private const val ARGS_KEY = "arguments_extra_key"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TrendsFragment.Callback, FragmentCallback {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()

        if (savedInstanceState == null) {
            openFragment(MainFragment())

            val isUserChoice = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                .getBoolean(IS_USER_CHOICE, false)

            if (!isUserChoice) {
                showPolicyDialogFragment()
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

    override fun onBackPressed() {
        super.onBackPressed()
        navigationView.hideKeyboard(this)
        closeDrawer()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.hideKeyboard(applicationContext)
        closeDrawer()

        when (item.itemId) {
            R.id.trend_item -> {
                openFragment(TrendsFragment())
                return true
            }
            R.id.cached_item -> {
                openFragment(CacheFragment())
                return true
            }
            R.id.favorites_item -> {
                openFragment(FavoritesFragment())
                return true
            }
            R.id.random_item -> {
                openFragment(MainFragment())
                return true
            }
            R.id.search_item -> {
                clearBackStack()
                return true
            }
            R.id.policy_privacy_item -> {
                tryOpenPrivacyPolicy()
                return true
            }
        }
        return false
    }

    override fun onWordClick(word: String) {
        val fragment = MainFragment.newInstance(word)
        openFragment(fragment)
    }

    override fun onDefinitionClick(definition: Definition) {
        navigationView.hideKeyboard(this)
        val fragment = DetailFragment.newInstance(definition)
        openFragment(fragment)
    }

    override fun onMenuToolbarClick() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun openFragment(fragment: Fragment) {
        if (fragment is MainFragment) {
            clearBackStack()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, fragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                commit()
            }

            return
        }

        if (fragment !is DetailFragment) {
            supportFragmentManager.popBackStack()
        }

        val tag = fragment::class.java.simpleName

        supportFragmentManager.beginTransaction().apply {
            addToBackStack(tag)
            replace(R.id.frame_layout, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
    }

    private fun initUI() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        navigationView.setNavigationItemSelectedListener(this)
        toggle.syncState()
    }

    private fun showPolicyDialogFragment() {
        PrivacyPolicyFragmentDialog().show(supportFragmentManager, "policy")
    }

    private fun clearBackStack() {
        val count = supportFragmentManager.backStackEntryCount
        if (count <= 0) return
        for (i in 0 until count) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun closeDrawer() {
        val handler = Handler()
        handler.postDelayed({ drawerLayout.closeDrawer(GravityCompat.START) }, 100)
    }

    private fun tryOpenPrivacyPolicy() {
        val url = getString(R.string.policy_link)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}