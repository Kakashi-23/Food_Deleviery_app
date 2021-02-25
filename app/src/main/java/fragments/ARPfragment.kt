package fragments

import Adapters.Adapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.s.b.s.ideapad.food_delivery_app.R
import model.Reslist
import org.json.JSONObject
import util.ConnectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class ARPfragment : Fragment() {
    lateinit var recyclerarp: RecyclerView
    lateinit var recyclerAdapter: Adapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    // rating comparator
    var ratingComparator= Comparator<Reslist>{ res1, res2->
        res1.resrating.compareTo(res2.resrating,true)
    }
    var reslistarray= arrayListOf<Reslist>()
    // price comparator
    var priceComparator= Comparator<Reslist>{ res1, res2->
        res1.resprice.compareTo(res2.resprice,true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val view= inflater.inflate(R.layout.fragment_a_r_pfragment, container, false)

        recyclerarp =view.findViewById(R.id.arpRecycler)

        if (ConnectionManager().checkconnectivity(activity as Context)) {
            setHasOptionsMenu(true)

            // volley request and data shown
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/ "
            val jsonparams = JSONObject()
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                var reslist = it.getJSONObject("data")
                var success = reslist.getBoolean("success")
                if (success) {
                    val data = reslist.getJSONArray("data")

                    for (i in 0 until data.length()) {
                        val reslistJsonObject = data.getJSONObject(i)
                        val reslistobject = Reslist(
                                reslistJsonObject.getString("id"),
                                reslistJsonObject.getString("name"),
                                reslistJsonObject.getString("rating"),
                                reslistJsonObject.getString("cost_for_one"),
                                reslistJsonObject.getString("image_url")
                        )


                        reslistarray.add(reslistobject)
                        recyclerAdapter = Adapter(activity as Context, reslistarray)
                        recyclerarp.adapter = recyclerAdapter
                        recyclerarp.layoutManager = layoutManager
                        recyclerarp.addItemDecoration(
                                androidx.recyclerview.widget.DividerItemDecoration(recyclerarp.context,
                                        (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).orientation
                                )
                        )

                    }


                } else {
                    Toast.makeText(activity as Context, "Some error occured", Toast.LENGTH_SHORT).show()
                }


            }, Response.ErrorListener {
                Toast.makeText(activity as Context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content type"] = "application/json"
                    headers["token"] = "c1635bc1e3000c"
                    return headers


                }
            }
            queue.add(jsonObjectRequest)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity as Context)
        }
        else
        {
            val dialog= androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Not connected")
            dialog.setPositiveButton("Open Settings"){
                text,listener->
                val settingsintent= Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(settingsintent)
                activity?.finish()
            }
            dialog.setNegativeButton("exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sorting,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuPriceHTL->{
                Collections.sort(reslistarray,priceComparator)
                reslistarray.reverse()}
            R.id.menuPriceLTH-> Collections.sort(reslistarray,priceComparator)
            R.id.menuRating->{
                Collections.sort(reslistarray,ratingComparator)
                reslistarray.reverse()}

        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onContextItemSelected(item)
    }

}
