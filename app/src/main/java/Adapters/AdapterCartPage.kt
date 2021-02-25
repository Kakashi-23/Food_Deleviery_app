package Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.s.b.s.ideapad.food_delivery_app.R
import database.CartEntity

class AdapterCartPage(val context: Context,val menuitemlist:List<CartEntity>):RecyclerView.Adapter<AdapterCartPage.DashboardViewHolder>()
{


    class DashboardViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val itemName:TextView=view.findViewById(R.id.cartRowItemName)
        val itemPrice:TextView=view.findViewById(R.id.cartRowItemPrice)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.cart_page_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuitemlist.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val list=menuitemlist[position]
        holder.itemName.text=list.itemName
        holder.itemPrice.text=list.itemCost



    }


}