package com.catnip.covidapp.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.catnip.covidapp.R
import com.catnip.covidapp.data.local.sharedpreference.SessionPreference
import com.catnip.covidapp.databinding.ActivityMainBinding
import com.catnip.covidapp.ui.home.covidinfo.CovidInfoFragment
import com.catnip.covidapp.ui.home.covidnews.CovidNewsFragment
import com.catnip.covidapp.ui.login.LoginActivity
import com.catnip.covidapp.utils.Constants

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var covidInfoFragment: CovidInfoFragment = CovidInfoFragment()
    private var covidNewsFragment: CovidNewsFragment = CovidNewsFragment()
    private var activeFragment: Fragment = covidInfoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFragment()
    }

    private fun setupFragment() {
        //remove all fragment if exist
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        //add to fragment manager
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fl_fragment, covidInfoFragment, Constants.FRAGMENT_TAG_INFO)
            add(R.id.fl_fragment, covidNewsFragment, Constants.FRAGMENT_TAG_NEWS).hide(covidNewsFragment)
        }.commit()
        //set initial fragment title
        supportActionBar?.title = getString(R.string.title_toolbar_menu_covid_news)
        //set selection for fragment by menu
        binding.bottomNavViewHome.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_news -> {
                    supportActionBar?.title = getString(R.string.title_toolbar_menu_covid_news)
                    showFragment(covidNewsFragment)
                    true
                }
                else -> {
                    supportActionBar?.title = getString(R.string.title_toolbar_menu_covid_info)
                    showFragment(covidInfoFragment)
                    true
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().hide(activeFragment)
            .show(fragment).commit()
        activeFragment = fragment
    }


    //to add event click when menu clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_logout -> {
                showDialogLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //to inflate layout menu to activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_homepage, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showDialogLogout() {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.text_logout_dialog))
                setPositiveButton(R.string.text_dialog_logout_task_positive) { dialog, id ->
                    logout()
                    dialog?.dismiss()
                }
                setNegativeButton(R.string.text_dialog_logout_task_negative) { dialog, id ->
                    // User cancelled the dialog
                    dialog?.dismiss()
                }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun logout() {
        SessionPreference(this).deleteSession()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}