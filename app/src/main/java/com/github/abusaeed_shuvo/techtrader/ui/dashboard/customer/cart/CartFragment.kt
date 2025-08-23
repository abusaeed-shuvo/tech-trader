package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.cart

import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentCartBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.cart.componets.CartItemAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout

	private val viewModel: CartViewModel by viewModels()
	private lateinit var adapter: CartItemAdapter

	override fun setListener() {
		swipeRefreshLayout = binding.swipeToRefreshLayout

		viewModel.getAllCartItems()
		setupRecyclerView()

		swipeRefreshLayout.setOnRefreshListener {
			viewModel.getAllCartItems()

		}
		swipeRefreshLayout.setColorSchemeColors(
			Color.RED, Color.GREEN, Color.BLUE
		)
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.LTGRAY)
	}

	override fun setObserver() {
		viewModel.cartListResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error -> {
					swipeRefreshLayout.isRefreshing = false
					Snackbar.make(
						binding.root,
						"Error: ${dataState.message}",
						Snackbar.LENGTH_SHORT
					)
						.show()
				}

				is DataState.Loading -> {
					swipeRefreshLayout.isRefreshing = true
				}

				is DataState.Success -> {
					swipeRefreshLayout.isRefreshing = false
					dataState.data?.let { products ->
						adapter.submitList(products)
					}
				}

			}
		}
	}

	private fun setupRecyclerView() {
		adapter = CartItemAdapter(
			onPlusClicked = { item ->

			},
			onMinusClicked = { item ->

			},
			onRemoveClicked = { item ->

			}
		)
		binding.rcvCartItem.adapter = adapter
	}
}