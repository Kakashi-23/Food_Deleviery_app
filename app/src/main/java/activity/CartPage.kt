package activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import Adapters.AdapterCartPage
import com.s.b.s.ideapad.food_delivery_app.R
import database.CartDatabase
import database.CartEntity
import org.json.JSONObject
import kotlin.collections.HashMap

class CartPage : AppCompatActivity() {
    lateinit var toolbarCP: androidx.appcompat.widget.Toolbar
    lateinit var resNameCP:TextView
    lateinit var recyclerCP:RecyclerView
    lateinit var payBtnCP:Button
    var cartList= listOf<CartEntity>()
    lateinit var layoutManager:RecyclerView.LayoutManager


    lateinit var recyclerAdapterCP: AdapterCartPage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_page)
        layoutManager= LinearLayoutManager(this)
        toolbarCP=findViewById(R.id.cartPageToolbar)
        resNameCP=findViewById(R.id.cartPageResName)
        recyclerCP=findViewById(R.id.cartPageRecycler)
        payBtnCP=findViewById(R.id.cartPageBtn)
        val resname=intent.getStringExtra("Resname")
        resNameCP.text=resname
        setUpToolbar()
        var db=RetriveCartItems(this).execute()
        cartList=db.get()
        recyclerAdapterCP= AdapterCartPage(this, cartList)
        recyclerCP.adapter=recyclerAdapterCP
        recyclerCP.layoutManager=layoutManager
        var totalAmount=0
        val userId=getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE).getString("user_id",null)

        for (i in 0..cartList.size-1){
           totalAmount+=cartList[i].itemCost.toInt()


        }


        payBtnCP.text="Proceed to Pay(Rs.$totalAmount)"
        payBtnCP.setOnClickListener {


            // volley request
            var queue=Volley.newRequestQueue(this)
            val url="http://13.235.250.119/v2/place_order/fetch_result/"
            var jsonParams=JSONObject()
            jsonParams.put("user_id", userId)
            jsonParams.put("restaurant_id",cartList[0].resId)
            jsonParams.put("total_cost",totalAmount.toString())
            var jsonparam=JSONObject()
            for (element in cartList)
            {
                jsonparam.put("food_item_id", element.itemId)
            }
            jsonParams.put("food",jsonparam.toString())
            val jsonObjectRequest=object: JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                val data=it.getJSONObject("data")
                val success=data.getBoolean("success")
                println("response is $success")
                if (success){
                    val intent=Intent(this,SplashPayment::class.java)
                    startActivity(intent)


                }
                else{
                    Toast.makeText(this,"Some Error Occured",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {
                Toast.makeText(this,"Some Error Occured",Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    var headers=HashMap<String,String>()
                    headers["Content type"] = "application/json"
                    headers["token"] = "c1635bc1e3000c"
                    return headers

                }

            }

            queue.add(jsonObjectRequest)
        }





    }

    fun setUpToolbar() {

        setSupportActionBar(toolbarCP)
        supportActionBar?.title="Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    class RetriveCartItems(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {

        override fun doInBackground(vararg params: Void?):List<CartEntity> {


            val db = Room.databaseBuilder(context, CartDatabase::class.java, "Cart-DB").build()
            return db.CartDao().getAllItems()
        }


        }

    }


