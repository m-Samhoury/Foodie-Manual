package com.moustafa.foodiemanual.repository.databasesource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moustafa.foodiemanual.models.FavoriteRestaurant
import com.moustafa.foodiemanual.models.FavoriteRestaurantDao

/**
 * @author moustafasamhoury
 * created on Friday, 22 Nov, 2019
 */

@Database(entities = [FavoriteRestaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteRestaurantDao(): FavoriteRestaurantDao
}
