package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritesDAO {
    @Insert
    fun insertItem(itemEntity:FavoritesEntity)
    @Delete
    fun deleteItem(itemEntity: FavoritesEntity)
    @Query("SELECT * from Favorites")
    fun getAllItems():List<FavoritesEntity>
    @Query("SELECT * from Favorites where resId=:resID")
    fun getItemFromId(resID:String):FavoritesEntity
}