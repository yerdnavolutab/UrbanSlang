package com.butul0ve.urbanslang

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.butul0ve.urbanslang.adapter.IS_USER_CHOICE
import com.butul0ve.urbanslang.adapter.PrivacyPolicyFragment
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.mvp.FragmentCallback
import com.butul0ve.urbanslang.mvp.cache.CacheFragment
import com.butul0ve.urbanslang.mvp.detail.DetailFragment
import com.butul0ve.urbanslang.mvp.favorites.FavoritesFragment
import com.butul0ve.urbanslang.mvp.main.MainFragment
import com.butul0ve.urbanslang.mvp.trends.TrendsFragment
import com.butul0ve.urbanslang.utils.convertToFragment

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
        supportFragmentManager.popBackStack()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        TODO("hide the keyboard, add the fragment to the backstack and open it")
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
        }
        return false
    }

    override fun onWordClick(word: String) {
        TODO("open the MainFragment with the query")
    }

    override fun onDefinitionClick(definition: Definition) {
        val fragment = DetailFragment.newInstance(definition)
        openFragment(fragment)
    }

    private fun openFragment(fragment: Fragment) {
        val isNeedToAddToBackStack = fragment::class.java.simpleName == DetailFragment::class.java.simpleName
        if (isNeedToAddToBackStack) {
            supportFragmentManager
                .beginTransaction().apply {
                    addToBackStack(null)
                    replace(R.id.frame_layout, fragment)
                    commit()
                }
        } else {
            for (item in 0..supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
            supportFragmentManager
                .beginTransaction().apply {
                    replace(R.id.frame_layout, fragment)
                    commit()
                }
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
        PrivacyPolicyFragment().show(supportFragmentManager, "policy")
    }
}
