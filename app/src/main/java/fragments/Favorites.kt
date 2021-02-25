package fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import Adapters.AdapterFavorites

import com.s.b.s.ideapad.food_delivery_app.R
import database.FavoritesDatabase
import database.FavoritesEntity


class Favorites : Fragment() {
lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: AdapterFavorites


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView=view.findViewById(R.id.favRecycler)
        layoutManager=LinearLayoutManager(activity as Context)

        val favobject=DBAsyncTaskFavorites(activity as Context).execute()
        val list=favobject.get()
        if (list.isEmpty()){
            Toast.makeText(activity as Context,"Nothing added to Favorites",Toast.LENGTH_LONG).show()

        }
        else {
            recyclerAdapter = AdapterFavorites(activity as Context, list)
            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(
                    androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context,
                            (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).orientation)
            )

        }

        return view
    }
    class DBAsyncTaskFavorites(val context: Context) : AsyncTask<Void, Void, List<FavoritesEntity>>() {

        val db = Room.databaseBuilder(context, FavoritesDatabase::class.java, "Favorites-DB").build()
        override fun doInBackground(vararg params: Void?): List<FavoritesEntity>? {
            val Item=db.FavoritesDAO().getAllItems()
            db.close()
            return Item
        }


    }

}

