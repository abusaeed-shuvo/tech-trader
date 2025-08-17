package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product

import android.graphics.Color
import android.view.View
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentMyProductsBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product.components.ProductListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyProductsFragment :
	BaseFragment<FragmentMyProductsBinding>(FragmentMyProductsBinding::inflate) {
	private lateinit var adapter: ProductListAdapter
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout

	@Inject
	lateinit var auth: FirebaseAuth

	private val viewModel: ProductsViewModel by viewModels()


	override fun setListener() {

		swipeRefreshLayout = binding.swipeToRefreshLayout
		adapter = ProductListAdapter()
		binding.rcvProducts.adapter = adapter


		auth.currentUser?.let { user ->
			viewModel.getProductById(user.uid)
		}

		swipeRefreshLayout.setOnRefreshListener {
			auth.currentUser?.let { user ->
				viewModel.getProductById(user.uid)
			}
		}
		swipeRefreshLayout.setColorSchemeColors(
			Color.RED, Color.GREEN, Color.BLUE
		)
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.LTGRAY)
	}

	override fun setObserver() {
		viewModel.productResponse.observe(viewLifecycleOwner) {
			when (it) {
				is DataState.Error   -> {
					dismissLoading()
					Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_SHORT)
						.show()
				}

				is DataState.Loading -> {
					showLoading()
				}

				is DataState.Success -> {
					it.data?.let { products ->
						adapter.submitList(products)
					}
					dismissLoading()
				}
			}
		}
	}

	private fun showLoading() {
		binding.progressIndicator.visibility = View.VISIBLE
		binding.progressIndicator.isIndeterminate = true
	}

	private fun dismissLoading() {
		binding.progressIndicator.visibility = View.GONE
	}
}