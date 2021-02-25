package Adapters


import activity.ResMenuRdf
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.s.b.s.ideapad.food_delivery_app.R
import com.squareup.picasso.Picasso
import database.FavoritesDatabase
import database.FavoritesEntity
import model.Reslist

class Adapter (val context: Context, val resList:ArrayList<Reslist>): androidx.recyclerview.widget.RecyclerView.Adapter<Adapter.DashboardViewHolder>()  {

    class DashboardViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val resName: TextView =view.findViewById(R.id.resname)
        val resratings: TextView =view.findViewById(R.id.resrating)
        val resprice: TextView =view.findViewById(R.id.price)
        val resimage: ImageView =view.findViewById(R.id.reslogo)
        val rlcontent: RelativeLayout =view.findViewById(R.id.rlcontent)
        val ratingLogo:ImageView=view.findViewById(R.id.ratinglogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val  view= LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val reslist=resList[position]
        holder.resName.text= reslist.resname
        holder.resprice.text=reslist.resprice
        holder.resratings.text= reslist.resrating
        Picasso.get().load(reslist.resimage).error(R.drawable.logo).into(holder.resimage)
        holder.rlcontent.setOnClickListener {
           val intent=Intent(context,ResMenuRdf::class.java)
            intent.putExtra("id",reslist.resid)
            intent.putExtra("resname",reslist.resname)
            context.startActivity(intent)
        }
        val defaultColor=ContextCompat.getColor(context, R.color.White)
        holder.ratingLogo.setColorFilter(defaultColor)
        val faventity=FavoritesEntity(reslist.resid,reslist.resname,reslist.resimage,reslist.resrating,reslist.resprice)
            val isfav= DBAsyncTaskAdapter(context, faventity, 3).execute().get()
        val favcolor= ContextCompat.getColor(context, R.color.colorRed)
            if (isfav)
            {

                holder.ratingLogo.setColorFilter(favcolor)
            }
        holder.ratingLogo.setOnClickListener {
            if (!DBAsyncTaskAdapter(context,
                            faventity,
                            3)
                            .execute()
                            .get()){
                val async= DBAsyncTaskAdapter(context, faventity, 1).execute()
                val result=async.get()
                if (result){
                    Toast.makeText(context,"added to favorites", Toast.LENGTH_SHORT).show()

                    holder.ratingLogo.setColorFilter(favcolor)
                   }
                else{
                    Toast.makeText(context,"error occured", Toast.LENGTH_SHORT).show()
                }


            }
            else{
                val async= DBAsyncTaskAdapter(context, faventity, 2).execute()
                val result=async.get()
                if (result){
                    Toast.makeText(context,"removed from favorites ", Toast.LENGTH_SHORT).show()
                    holder.ratingLogo.setColorFilter(defaultColor)

                   }
                else{
                    Toast.makeText(context,"error occured", Toast.LENGTH_SHORT).show()
                }
            }
        }


        }





    class DBAsyncTaskAdapter(val context: Context, val favEntity: FavoritesEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, FavoritesDatabase::class.java, "Favorites-DB").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {
                    db.FavoritesDAO().insertItem(favEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.FavoritesDAO().deleteItem(favEntity)
                    db.close()
                    return true

                }
                3->{ val Item=db.FavoritesDAO().getItemFromId(favEntity.resId)
                    db.close()
                    return Item!=null
                }



            }

            return false
        }


    }





    }


