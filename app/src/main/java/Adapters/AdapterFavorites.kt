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
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.s.b.s.ideapad.food_delivery_app.R
import com.squareup.picasso.Picasso
import database.FavoritesDatabase
import database.FavoritesEntity

class AdapterFavorites(val context: Context,val favList:List<FavoritesEntity>): androidx.recyclerview.widget.RecyclerView.Adapter<AdapterFavorites.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val resName: TextView = view.findViewById(R.id.resname)
        val resratings: TextView = view.findViewById(R.id.resrating)
        val resprice: TextView = view.findViewById(R.id.price)
        val resimage: ImageView = view.findViewById(R.id.reslogo)
        val rlcontent: RelativeLayout = view.findViewById(R.id.rlcontent)
        val ratingLogo: ImageView = view.findViewById(R.id.ratinglogo)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val list = favList[position]
        holder.resName.text = list.resName
        holder.resratings.text = list.rating
        holder.resprice.text = list.cost
        Picasso.get().load(list.resImage).error(R.drawable.logo).into(holder.resimage)
        holder.rlcontent.setOnClickListener {
            val intent = Intent(context, ResMenuRdf::class.java)
            intent.putExtra("id", list.resId)
            intent.putExtra("resname", list.resName)
            context.startActivity(intent)
        }
        val favcolor = ContextCompat.getColor(context, R.color.colorRed)

        holder.ratingLogo.setColorFilter(favcolor)
        holder.ratingLogo.setOnClickListener {
            val dbObject= DBAsyncTaskFavAdapter(context, list).execute()
            holder.ratingLogo.setColorFilter(ContextCompat.getColor(context, R.color.White))


        }


    }

    class DBAsyncTaskFavAdapter(val context: Context, val favEntity: FavoritesEntity) : AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, FavoritesDatabase::class.java, "Favorites-DB").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.FavoritesDAO().deleteItem(favEntity)
            db.close()
            return false


        }



    }
}