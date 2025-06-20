package com.example.mydbapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mydbapp.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Инициализация базы данных начальными значениями
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getDatabase(context).productDao()
                                prePopulateDatabase(dao)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Функция для заполнения базы начальными данными
        private suspend fun prePopulateDatabase(productDao: ProductDao) {
            val products = listOf(
                Product(name = "Смартфон", description = "Современный смартфон с мощным процессором", price = 29999.99, imageUrl = "https://example.com/smartphone.jpg"),
                Product(name = "Ноутбук", description = "Легкий и производительный ноутбук", price = 59999.99, imageUrl = "https://example.com/laptop.jpg"),
                Product(name = "Наушники", description = "Беспроводные наушники с шумоподавлением", price = 9999.99, imageUrl = "https://example.com/headphones.jpg")
            )
            products.forEach { productDao.insert(it) }
        }
    }
}
