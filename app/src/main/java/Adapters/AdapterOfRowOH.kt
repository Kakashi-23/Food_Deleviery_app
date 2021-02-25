package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.s.b.s.ideapad.food_delivery_app.R
import org.json.JSONArray

class AdapterOfRowOH(val context:Context,val jsonArray:JSONArray): RecyclerView.Adapter<AdapterOfRowOH.DashboardViewHolder>() {



    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName:TextView=view.findViewById(R.id.txtOHItemName)
        val itemPrice:TextView=view.findViewById(R.id.txtOHPrice)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.row_of_row_oh, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jsonArray.length()
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

        holder.itemName.text=jsonArray.getJSONObject(position).getString("name")
        holder.itemPrice.text=jsonArray.getJSONObject(position).getString("cost")

    }
}