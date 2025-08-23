package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product_display.product_store

import android.graphics.Color
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentProductStoreBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product.ProductListViewModel
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product.components.CustomerProductItemAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductStoreFragment :
	BaseFragment<FragmentProductStoreBinding>(FragmentProductStoreBinding::inflate) {

	private lateinit var adapter: CustomerProductItemAdapter
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout
	private val args: ProductStoreFragmentArgs by navArgs()
	private lateinit var sId: String


	private val viewModel: ProductListViewModel by viewModels()

	override fun setListener() {
		swipeRefreshLayout = binding.swipeToRefreshLayout
		sId = args.storeId

		adapter = CustomerProductItemAdapter(onClick = { product ->
			val action =
				ProductStoreFragmentDirections.actionProductStoreFragmentToProductDisplayFragment(
					productId = product.productId
				)
			findNavController().navigate(action)
		})
		binding.rcvProducts.adapter = adapter


		viewModel.getAllStoreItemsByID(sId)


		swipeRefreshLayout.setOnRefreshListener {
			viewModel.getAllStoreItemsByID(sId)

		}
		swipeRefreshLayout.setColorSchemeColors(
			Color.RED, Color.GREEN, Color.BLUE
		)
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.LTGRAY)
		requireActivity().onBackPressedDispatcher.addCallback(
			viewLifecycleOwner,
			object : OnBackPressedCallback(true) {
				override fun handleOnBackPressed() {
					findNavController().popBackStack()
				}

			})
	}

	override fun setObserver() {
		viewModel.storeProductResponse.observe(viewLifecycleOwner) {
			when (it) {
				is DataState.Error -> {
					swipeRefreshLayout.isRefreshing = false
					Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_SHORT)
						.show()
				}

				is DataState.Loading -> {
					swipeRefreshLayout.isRefreshing = true
				}

				is DataState.Success -> {
					swipeRefreshLayout.isRefreshing = false
					it.data?.let { products ->
						adapter.submitList(products)
					}
				}
			}
		}
	}

}