package com.tuproject.foodeluxe

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.tuproject.foodeluxe.commons.extensions.replaceFragmentInActivity
import com.tuproject.foodeluxe.features.about.AboutFragment
import com.tuproject.foodeluxe.features.recipefavourites.RecipeFavouritesFragment
import com.tuproject.foodeluxe.features.recipesearch.RecipeSearchFragment
import com.tuproject.foodeluxe.features.startpage.StartpageFragment
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var startpageFragmentProvider: Lazy<StartpageFragment>

    @Inject
    lateinit var recipeSearchFragmentProvider: Lazy<RecipeSearchFragment>

    @Inject
    lateinit var recipeFavouritesFragmentProvider: Lazy<RecipeFavouritesFragment>

    @Inject
    lateinit var aboutFragmentProvider: Lazy<AboutFragment>

    /**
     * bundle for search query
     */
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        setupDrawerMenu()
        val ab: android.support.v7.app.ActionBar = supportActionBar!!
        ab.apply {
            setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        if (savedInstanceState == null) {
            changeToFragment(startpageFragmentProvider as Lazy<Fragment>)
        }

        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        setupSearchMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.search -> {
                Log.v("MainActivity", "Search button pressed")
                true
            }
            android.R.id.home -> {
                Log.v("MainActivity", "Home button pressed")
                activity_main.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            (R.id.nav_search) -> {
                changeToFragment(startpageFragmentProvider as Lazy<Fragment>)
                activity_main.closeDrawer(GravityCompat.START)
            }
            (R.id.nav_favourites) -> {
                changeToFragment(recipeFavouritesFragmentProvider as Lazy<Fragment>)
                activity_main.closeDrawer(GravityCompat.START)
            }
            (R.id.about) -> {
                changeToFragment(aboutFragmentProvider as Lazy<Fragment>)
                activity_main.closeDrawer(GravityCompat.START)
            }
        }
        return false

    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    /**
     * Finish activity when reaching the last fragment.
     */
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStackImmediate()
        } else {
            finish()
        }
    }

    private fun handleIntent(intent: Intent) {
        /**
         * Verify if search intent, get the query and change fragment.
         */
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                bundle.putString("query", query)
                Log.v("MainActivity", "Search Intent Received")
                changeToFragment(recipeSearchFragmentProvider as Lazy<Fragment>)
            }
        }
    }

    private fun changeToFragment(f: Lazy<Fragment>) {
        val fragment = f.get()
        fragment.arguments = bundle
        val ft = supportFragmentManager.beginTransaction()

        ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit)

        ft.replace(R.id.activity_base_content, fragment)
        //detach-attach to refresh if already in searchfragment
        ft.detach(fragment)
        ft.attach(fragment)
        ft.addToBackStack(null)
        ft.commit()
        Log.v("MainActivity", "Fragment changed")
    }

    private fun setupSearchMenu(menu: Menu) {

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)

        (searchMenuItem.actionView as SearchView).apply {

            //set destination search result class
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        //pop fragment if arrow back in searchview pressed
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                Log.v("MainActivity", "Search open button pressed")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                Log.v("MainActivity", "Search back button pressed")
                /*val fragmentManager = supportFragmentManager
                if (fragmentManager.backStackEntryCount > 1) {
                    fragmentManager.popBackStackImmediate()
                }*/
                changeToFragment(startpageFragmentProvider as Lazy<Fragment>)
                return true
            }
        })

    }

    private fun setupDrawerMenu() {

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener(this)
    }
}
