package fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import Adapters.AdapterOH

import com.s.b.s.ideapad.food_delivery_app.R
import model.OHItemName
import org.json.JSONObject


class OrderHistory : Fragment() {

    lateinit var recyclerOH: RecyclerView
    lateinit var adapterOH: AdapterOH
    lateinit var layoutManager: RecyclerView.LayoutManager
    var historyListOH = arrayListOf<OHItemName>()

    class getObject(
            val data: JSONObject
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        recyclerOH = view.findViewById(R.id.ohRecycler)
        val userid = activity?.getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE)?.getString("user_id", null)
        //volley request
        val queue = Volley.newRequestQueue(activity)
        val url = " http://13.235.250.119/v2/orders/fetch_result/$userid "
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            val data = getObject(it.getJSONObject("data"))
            val success = data.data.getBoolean("success")
            if (success) {
                val getdata=data.data.getJSONArray("data")
                for (i in 0 until getdata.length()){
                    val jsonObject=getdata.getJSONObject(i)
                    val jsonObjectlist=OHItemName(
                            jsonObject.getString("order_id"),
                            jsonObject.getString("restaurant_name"),
                            jsonObject.getString("total_cost"),
                            jsonObject.getString("order_placed_at"),
                            jsonObject.getJSONArray("food_items")

                    )
                    historyListOH.add(jsonObjectlist)
                  adapterOH= AdapterOH(activity as Context, historyListOH)
                    recyclerOH.adapter=adapterOH
                    recyclerOH.layoutManager=layoutManager
                    recyclerOH.addItemDecoration(
                            androidx.recyclerview.widget.DividerItemDecoration(recyclerOH.context,
                                    (layoutManager as LinearLayoutManager).orientation
                            ))



                }




            } else {
                Toast.makeText(activity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }


        }, Response.ErrorListener {
            Toast.makeText(activity, "Some Error Occurred", Toast.LENGTH_SHORT).show()

        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content type"] = "application/json"
                headers["token"] = "c1635bc1e3000c"
                return headers

            }


        }
        queue.add(jsonObjectRequest)
        layoutManager= LinearLayoutManager(activity)

        return view
    }
}

