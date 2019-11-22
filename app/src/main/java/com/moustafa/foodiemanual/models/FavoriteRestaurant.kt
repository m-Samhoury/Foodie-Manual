package com.moustafa.foodiemanual.models

import androidx.room.*

/**
 * @author moustafasamhoury
 * created on Friday, 22 Nov, 2019
 */
@Entity
data class FavoriteRestaurant(
    @PrimaryKey @ColumnInfo(name = "restaurantName")
    val restaurantName: String
)

@Dao
interface FavoriteRestaurantDao {
    @Query("SELECT restaurantName FROM FavoriteRestaurant where restaurantName = :restaurantName")
    suspend fun get(restaurantName: String): FavoriteRestaurant?

    @Query("SELECT restaurantName FROM FavoriteRestaurant")
    suspend fun getAllFavoriteRestaurants(): List<FavoriteRestaurant>?

    @Insert
    suspend fun insertRestaurantToFavorites(restaurantToBeInserted: FavoriteRestaurant)

    @Delete
    suspend fun removeRestaurantFromFavorites(restaurantToBeRemoved: FavoriteRestaurant)

    @Query("DELETE FROM FavoriteRestaurant")
    suspend fun deleteAll()
}
