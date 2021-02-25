package activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
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
import Adapters.AdapterRDF
import com.s.b.s.ideapad.food_delivery_app.R
import fragments.Favorites
import fragments.MyProfile
import fragments.OrderHistory
import model.ResMenu
import org.json.JSONObject

class ResMenuRdf : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var recyclerrdf: RecyclerView
    lateinit var recyclerAdapter: AdapterRDF
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var navigationView: NavigationView
   lateinit var goToBtn: Button
    var previousMenuItem: MenuItem?=null
    val menuItemList= arrayListOf<ResMenu>()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_menu_rdf)
        goToBtn=findViewById(R.id.rdfGoTo)
        goToBtn.visibility=View.GONE
        drawerLayout =findViewById<DrawerLayout>(R.id.rdfDrawerLayout)
        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.rdfCoordinatorLayout)
        toolbar = findViewById<Toolbar>(R.id.rdfToolBar)
        frameLayout = findViewById<FrameLayout>(R.id.rdfFrameLayout)
        recyclerrdf = findViewById(R.id.rdfRecycler)
        navigationView=findViewById(R.id.rdfNavigationView)


        val rdfResid=intent.getStringExtra("id")
        var rdfResName=intent.getStringExtra("resname")
        setUpToolbar(rdfResName)



        //volley request
        deleteDatabase("Cart-DB")
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$rdfResid "
        val jsonparams = JSONObject()
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener{
            val detailsObject1=it.getJSONObject("data")
            val success=detailsObject1.getBoolean("success")
            if (success){

                val detailsobject2=detailsObject1.getJSONArray("data")
                for (i in 0 until detailsobject2.length()){
                    var detailsobject3=detailsobject2.getJSONObject(i)
                        var itemlist=ResMenu(
                                detailsobject3.getString("id"),
                        detailsobject3.getString("name"),
                                detailsobject3.getString("cost_for_one"),
                                detailsobject3.getString("restaurant_id")

                        )

                        menuItemList.add(itemlist)

                    recyclerAdapter= AdapterRDF(this, menuItemList, goToBtn, rdfResName)
                    recyclerrdf.adapter=recyclerAdapter
                    recyclerrdf.layoutManager=layoutManager
                    recyclerrdf.addItemDecoration(
                            androidx.recyclerview.widget.DividerItemDecoration(recyclerrdf.context,
                                    (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).orientation)
                            )


                }



            }




            else {Toast.makeText(this,"Some error occured",Toast.LENGTH_SHORT).show()}




        },
                Response.ErrorListener {

                    Toast.makeText(this@ResMenuRdf,"Some Error Occured",Toast.LENGTH_SHORT).show() }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content type"] = "application/json"
                headers["token"] = "c1635bc1e3000c"
                return headers


            }
        }


        queue.add(jsonObjectRequest)
        layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)




    }

    fun setUpToolbar(resname:String?) {

        setSupportActionBar(toolbar)
        supportActionBar?.title=resname
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


}
