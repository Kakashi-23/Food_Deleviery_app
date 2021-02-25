package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity (
        @PrimaryKey       val itemId:Int,
        @ColumnInfo(name = "Item Name")  val itemName:String,
       @ColumnInfo(name = "Cost") val itemCost:String,
        @ColumnInfo(name="Restaurant_id") val resId:String
)
