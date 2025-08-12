package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentMyProductsBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product.components.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyProductsFragment :
	BaseFragment<FragmentMyProductsBinding>(FragmentMyProductsBinding::inflate) {
	private lateinit var adapter: ProductListAdapter

	private val viewModel: ProductsViewModel by viewModels()


	override fun setListener() {
		adapter = ProductListAdapter()
		binding.rcvProducts.adapter = adapter
		binding.rcvProducts.layoutManager = LinearLayoutManager(requireContext())

	}

	override fun setObserver() {
		viewModel.products.observe(viewLifecycleOwner) { products ->
			adapter.submitList(products)

		}
	}
}