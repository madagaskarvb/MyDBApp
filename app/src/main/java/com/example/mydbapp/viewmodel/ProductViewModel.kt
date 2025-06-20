package com.example.mydbapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mydbapp.data.AppDatabase
import com.example.mydbapp.data.Product
import com.example.mydbapp.data.ProductRepository
import kotlinx.coroutines.launch


class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val allProducts: LiveData<List<Product>>

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        allProducts = repository.allProducts.asLiveData()
    }

    fun getProductById(id: Int): LiveData<Product> {
        return repository.getProductById(id).asLiveData()
    }

    fun insert(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }

    fun delete(product: Product) = viewModelScope.launch {
        repository.delete(product)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
