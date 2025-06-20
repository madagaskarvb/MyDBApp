package com.example.mydbapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydbapp.data.Product
import com.example.mydbapp.databinding.FragmentMainBinding
import com.example.mydbapp.viewmodel.ProductViewModel
import kotlin.random.Random
import androidx.navigation.fragment.findNavController
import com.example.mydbapp.R

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация адаптера
        adapter = ProductAdapter(
            onItemClick = { product ->
                // Переход к детальному фрагменту
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                }
                findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
            },
            onItemLongClick = { product ->
                // Показываем диалог подтверждения удаления
                showDeleteConfirmationDialog(product)
            }
        )

        // Настройка RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainFragment.adapter
        }

        // Наблюдение за изменениями данных
        productViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            adapter.setProducts(products)
            binding.emptyView.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        }

        // Кнопка обновления данных
        binding.refreshButton.setOnClickListener {
            // Здесь можно добавить логику обновления данных
            // Например, добавление нового продукта для демонстрации
            val newProduct = Product(
                name = "Новый продукт ${System.currentTimeMillis()}",
                description = "Описание нового продукта",
                price = Random.nextDouble(1000.0, 10000.0),
                imageUrl = "https://example.com/new_product.jpg"
            )
            productViewModel.insert(newProduct)
        }
    }

    private fun showDeleteConfirmationDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление продукта")
            .setMessage("Вы уверены, что хотите удалить ${product.name}?")
            .setPositiveButton("Да") { _, _ ->
                productViewModel.delete(product)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
