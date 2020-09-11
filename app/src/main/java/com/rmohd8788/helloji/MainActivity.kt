package com.rmohd8788.helloji

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rmohd8788.helloji.fragments.ExploreFragment
import com.rmohd8788.helloji.fragments.HashTagFragment
import com.rmohd8788.helloji.fragments.SavedFragment
import com.rmohd8788.helloji.fragments.TrendingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.app_toolbar.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import java.util.*

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var address: List<Address>
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var getUsername: String
    private lateinit var getEmail: String
    private lateinit var getImageUrl: String
    private lateinit var headerView: View

    companion object {
        const val LOCATION_PERMISSION_RC = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        headerView = navigationView.getHeaderView(0)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        updateUI()


        //setting up location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //tab listener
        tabLayout.addOnTabSelectedListener(this)
        //showing fragment
        showFragment(HashTagFragment())
        //getting user's current location details
        getUsersLocation()

        //navigation view listener
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    showFragment(HashTagFragment())
                }
                R.id.nav_trending -> {
                    showFragment(TrendingFragment())
                }
                R.id.nav_explore -> {
                    showFragment(ExploreFragment())
                }
                R.id.nav_save -> {
                    showFragment(SavedFragment())
                }
                R.id.nav_send -> {
                    sendingEmailIntent()
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        //link drawer with toolbar
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            mainToolbar as Toolbar?,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    //method for showing fragment
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainer.id, fragment)
            .commit()
    }

    //tab will decide what to do when selected position trigger
    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                mainToolbar.tvToolbarName.text = getString(R.string.tag)
                showFragment(HashTagFragment())
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.hashtag_dark)
            }

            1 -> {
                mainToolbar.tvToolbarName.text = getString(R.string.trending_tag)
                showFragment(TrendingFragment())
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.trending_dark)
            }

            2 -> {
                mainToolbar.tvToolbarName.text = getString(R.string.explore_tag)
                showFragment(ExploreFragment())
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.explore_dark)
            }

            3 -> {
                mainToolbar.tvToolbarName.text = getString(R.string.saved_tag)
                showFragment(SavedFragment())
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.heart_dark)
            }

        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.hashtag_light)
            }

            1 -> {
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.trending_light)
            }

            2 -> {
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.explore_light)
            }

            3 -> {
                tab.icon = ContextCompat.getDrawable(applicationContext, R.drawable.heart_light)
            }

        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    //checking for location permission
    private fun getUsersLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                onLocationChanged(it)
            }
        }
    }

    //getting location
    private fun onLocationChanged(location: Location) {
        geocoder = Geocoder(this, Locale.getDefault())
        address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val userAddress = address[0].locality + "," + address[0].adminArea
        tvBarLocationName.text = userAddress
    }

    //handling the drawer back pressed on its active state
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
        Intent(this, RegisterActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun sendingEmailIntent() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            putExtra(Intent.EXTRA_EMAIL, "rmohd8788@gamil.com")
            putExtra(Intent.EXTRA_SUBJECT, "Response")
            putExtra(Intent.EXTRA_TEXT, "Testing email")

            startActivity(Intent.createChooser(this, "Send Email"))
        }
    }

    /*updating the ui if user exist
    it is possible to have a null value so that to avoid these thing, i used multiple checks
    also its possible a user have only email,or only name and cannot have image url
    */
    private fun updateUI() {
        getUsername = R.string.developer.toString()
        getEmail = R.string.developer_email.toString()
        headerView.user_profile.setImageResource(R.mipmap.ic_launcher_round)
        getImageUrl = ""
        if (FirebaseAuth.getInstance().currentUser != null) {
            if (firebaseUser.displayName != null){
                getUsername = firebaseUser.displayName.toString()
            }
            if (firebaseUser.photoUrl != null){
                getImageUrl = firebaseUser.photoUrl.toString()
            }
            if (firebaseUser.email != null) {
                getEmail = firebaseUser.email.toString()
            }
        }
        Glide.with(this).load(getImageUrl).into(headerView.user_profile)
        headerView.user_email.text = getEmail
        headerView.username.text = getUsername
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        askForPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_RC
            )
        }
    }
}