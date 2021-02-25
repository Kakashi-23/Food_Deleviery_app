package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun insertItem(itemEntity: CartEntity)
    @Delete
    fun deleteItem(itemEntity: CartEntity)
    @Query("SELECT * from cart")
    fun getAllItems():List<CartEntity>
    @Query("SELECT * from cart where itemId =:item_id")
    fun getItemFromId(item_id:String):CartEntity


}