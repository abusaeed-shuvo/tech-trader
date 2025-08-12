package com.github.abusaeed_shuvo.techtrader.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.abusaeed_shuvo.techtrader.data.models.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
	@Query("SELECT * FROM products")
	fun getAllProducts(): Flow<List<ProductEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(productEntity: ProductEntity)

	@Delete
	suspend fun delete(productEntity: ProductEntity)
}