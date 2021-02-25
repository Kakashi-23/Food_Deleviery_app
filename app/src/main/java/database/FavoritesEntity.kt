package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorites")
class FavoritesEntity (
        @PrimaryKey val resId:String,
       @ColumnInfo(name ="Name") val resName:String,
        @ColumnInfo(name = "Image") val resImage:String,
        @ColumnInfo(name = "Rating") val rating:String,
        @ColumnInfo(name = "Cost") val cost:String
)