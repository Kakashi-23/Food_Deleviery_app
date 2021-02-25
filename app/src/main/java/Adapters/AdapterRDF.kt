package Adapters

import activity.CartPage
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.room.Room
import com.s.b.s.ideapad.food_delivery_app.R
import database.CartDatabase
import database.CartEntity
import model.ResMenu

class AdapterRDF (val context: Context, val resitemList:ArrayList<ResMenu>,val goToBtnView:Button,val resname:String): androidx.recyclerview.widget.RecyclerView.Adapter<AdapterRDF.DashboardViewHolder>() {


    class DashboardViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.rdfItemName)
        val ItemPrice: TextView = view.findViewById(R.id.rdfPrice)
        val srno: TextView = view.findViewById(R.id.rdfSr)
        val addBtn: Button = view.findViewById(R.id.rdfADD)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_res_details_fragment, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resitemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val itemlist = resitemList[position]

        holder.itemName.text = itemlist.itemName
        holder.ItemPrice.text = itemlist.itemPrice
        val value = position + 1
        holder.srno.text =value.toString()
        val cartentity = CartEntity(itemlist.itemId.toInt(), itemlist.itemName, itemlist.itemPrice,itemlist.resId)




            val checkfav= DBAsyncTask(context, cartentity, 3).execute()
            val isfav=checkfav.get()
            if (isfav){
                holder.addBtn.text="Remove"
                 }
            else{
              holder.addBtn.text="ADD"

            }
        holder.addBtn.setOnClickListener {
                if (!DBAsyncTask(context,
                                cartentity, 3)
                                .execute()
                                .get()){
                    val async= DBAsyncTask(context, cartentity, 1).execute()
                    val result=async.get()
                    if (result){
                        goToBtnView.visibility=View.VISIBLE
                        Toast.makeText(context,"added to cart", Toast.LENGTH_SHORT).show()

                        holder.addBtn.text="remove"
                        }
                    else{
                        Toast.makeText(context,"error occured", Toast.LENGTH_SHORT).show()
                    }


                }
                else{
                    val async= DBAsyncTask(context, cartentity, 2).execute()
                    val result=async.get()
                    if (result){
                        Toast.makeText(context,"removed from cart ", Toast.LENGTH_SHORT).show()

                        holder.addBtn.text="ADD"
                        }
                    else{
                        Toast.makeText(context,"error occured", Toast.LENGTH_SHORT).show()
                    }
                }
            goToBtnView.setOnClickListener{
                val intent=Intent(context,CartPage::class.java)
intent.putExtra("Resname",resname)
                context.startActivity(intent)


            }


            }
        }
    class DBAsyncTask(val context: Context, val cartEntity: CartEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, CartDatabase::class.java, "Cart-DB").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    db.CartDao().insertItem(cartEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.CartDao().deleteItem(cartEntity)
                    db.close()
                    return true

                }
                3->{ val CartItem:CartEntity?=db.CartDao().getItemFromId(cartEntity.itemId.toString())
                    db.close()
                    return CartItem!=null
                }



            }

            return false
        }



    }



}


