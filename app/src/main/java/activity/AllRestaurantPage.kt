package activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import Adapters.Adapter
import android.view.Menu
import android.view.MenuInflater
import com.s.b.s.ideapad.food_delivery_app.R
import fragments.*
import model.Reslist
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class AllRestaurantPage : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem: MenuItem? = null
    lateinit var userName: TextView
    lateinit var mobileno: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_restaurant_page)
        drawerLayout = findViewById<DrawerLayout>(R.id.arpDrawerLayout)
        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.arpCoordinatorLayout)
        toolbar = findViewById<Toolbar>(R.id.arpToolBar)
        frameLayout = findViewById<FrameLayout>(R.id.arpFrameLayout)
        navigationView = findViewById<NavigationView>(R.id.arpNavigationView)


        setUpToolbar()
        Arp()


// navigation drawer setup
        val actionBarDrawerToggle = ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.open_drawer,
                R.string.closed_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        userName = navigationView.getHeaderView(0).findViewById(R.id.navigationDLUsername)
        userName.text = getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE).getString("name", "whatever")
        mobileno = navigationView.getHeaderView(0).findViewById(R.id.navigationDLmobile)
        mobileno.text = getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE).getString("mobileNo", "whatever")
        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.menuHome -> {
                  Arp()
                    drawerLayout.closeDrawers()
                }
                R.id.menuFaq ->{
                    supportActionBar?.title = "FAQ"
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    val fragment = FAQ()
                    transaction.replace(R.id.arpFrameLayout, fragment)
                    transaction.commit()
                    drawerLayout.closeDrawers()
                }
                R.id.menuFavorites -> {
                    supportActionBar?.title = "Favorites"
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    val fragment = Favorites()
                    transaction.replace(R.id.arpFrameLayout, fragment)
                    transaction.commit()
                    drawerLayout.closeDrawers()
                }
                R.id.menuOrderhistory -> {

                    supportActionBar?.title = "Order History"
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    val fragment = OrderHistory()

                    transaction.replace(R.id.arpFrameLayout, fragment)
                    transaction.commit()

                    drawerLayout.closeDrawers()

                }
                R.id.menuProfile -> {
                    supportActionBar?.title = "My Profile"
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    val fragment = MyProfile()
                    transaction.replace(R.id.arpFrameLayout, fragment)
                    transaction.commit()
                    drawerLayout.closeDrawers()
                }
                R.id.menulogout -> {
                    val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                    dialog.setTitle("Log Out")
                    dialog.setMessage("Do you want to log out")
                    dialog.setPositiveButton("Yes") { text, listener ->
                        val sharedpref = getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE)
                        sharedpref.edit().clear().apply()

                        val intent = Intent(this, Login::class.java)
                        AppCompatActivity().finishAffinity()
                        startActivity(intent)
                       finishAffinity()

                    }
                    dialog.setNegativeButton("No") { text, listener ->

                        drawerLayout.closeDrawers()


                    }
                    dialog.create()
                    dialog.show()
                }

            }


            return@setNavigationItemSelectedListener true
        }

    }


    fun setUpToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id ==android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.arpFrameLayout)) {
            !is ARPfragment-> Arp()
            else->super.onBackPressed()

            }
    }



    fun Arp() {
        supportActionBar?.title="All Restaurants"
        navigationView.setCheckedItem(R.id.menuHome)
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = ARPfragment()
        transaction.replace(R.id.arpFrameLayout, fragment)
        transaction.commit()

    }


}