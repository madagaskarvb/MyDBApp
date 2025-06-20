package com.example.mydbapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mydbapp.databinding.FragmentDetailBinding
import com.example.mydbapp.viewmodel.ProductViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt("productId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Наблюдение за данными продукта
        productViewModel.getProductById(productId).observe(viewLifecycleOwner) { product ->
            binding.apply {
                productName.text = product.name
                productDescription.text = product.description
                productPrice.text = "₽${product.price}"
                // Здесь можно добавить загрузку изображения
            }
        }

        // Кнопка возврата
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Кнопка удаления
        binding.deleteButton.setOnClickListener {
            productViewModel.getProductById(productId).value?.let { product ->
                productViewModel.delete(product)
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
