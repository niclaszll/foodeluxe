package com.tuproject.foodeluxe.features.recipedetail

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.NavUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.tuproject.foodeluxe.R
import com.tuproject.foodeluxe.data.RecipeItem
import dagger.android.support.DaggerAppCompatActivity


class RecipeDetailActivity : DaggerAppCompatActivity() {

    /**
     * bundle for search query
     */
    private val bundle = Bundle()

    private var detail: RecipeItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipedetail)

        detail = intent.extras.getParcelable("recipe_detail")
        bundle.putParcelable("detail", detail)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val ab: android.support.v7.app.ActionBar = supportActionBar!!
        ab.apply {
            setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        }

        if (savedInstanceState == null) {
            changeFragment(RecipeDetailFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                Log.v("RecipeDetail", "Back button pressed")
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, detail!!.url)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Share to"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Finish activity when reaching the last fragment.
     */
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    private fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        f.arguments = bundle
        val ft = supportFragmentManager.beginTransaction()

        if (cleanStack) clearBackStack()

        ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit)

        ft.replace(R.id.activity_base_content, f)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}
