package com.example.mydbapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydbapp.data.Product
import com.example.mydbapp.databinding.ItemProductBinding


class ProductAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onItemLongClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products = emptyList<Product>()

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = "₽${product.price}"
                // Здесь можно добавить загрузку изображения с помощью Glide или Picasso
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = products[position]
        holder.bind(currentProduct)

        // Обработка клика по элементу
        holder.itemView.setOnClickListener {
            onItemClick(currentProduct)
        }

        // Обработка долгого нажатия
        holder.itemView.setOnLongClickListener {
            onItemLongClick(currentProduct)
            true
        }
    }

    override fun getItemCount() = products.size

    fun setProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }
}
