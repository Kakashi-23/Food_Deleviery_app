package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.s.b.s.ideapad.food_delivery_app.R
import model.OHItemName


class AdapterOH(val context: Context, val itemList: ArrayList<OHItemName>): androidx.recyclerview.widget.RecyclerView.Adapter<AdapterOH.DashboardViewHolder>() {

    lateinit var recyclerAdapter: AdapterOfRowOH
    lateinit var layoutManager: RecyclerView.LayoutManager

    class DashboardViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val recycler:RecyclerView=view.findViewById(R.id.ohRecycler)
        val date: TextView = view.findViewById(R.id.ohDate)
        val resName: TextView = view.findViewById(R.id.ohResName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.row_oh, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
      return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val list=itemList[position]
        layoutManager= LinearLayoutManager(context)
        holder.resName.text=list.resname
        holder.date.text=list.date
        val getObject=list.foodItem
       recyclerAdapter= AdapterOfRowOH(context, getObject)
        holder.recycler.adapter=recyclerAdapter
        holder.recycler.layoutManager=layoutManager



    }
}