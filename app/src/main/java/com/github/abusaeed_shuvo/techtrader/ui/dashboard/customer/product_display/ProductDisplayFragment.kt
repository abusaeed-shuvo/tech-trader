package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product_display

import android.graphics.Color
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.models.Rating
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntry
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentProductDisplayBinding
import com.github.abusaeed_shuvo.techtrader.libs.getConversationId
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product_display.components.ReviewItemAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDisplayFragment :
	BaseFragment<FragmentProductDisplayBinding>(FragmentProductDisplayBinding::inflate) {

	private val viewModel: ProductDisplayViewModel by viewModels()
	private val args: ProductDisplayFragmentArgs by navArgs()
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout
	private lateinit var pid: String
	private lateinit var uid: String
	private var userName: String = ""
	private var userImg: String = ""
	private lateinit var adapter: ReviewItemAdapter

	@Inject
	lateinit var auth: FirebaseAuth

	override fun setListener() {
		adapter = ReviewItemAdapter()
		binding.rcvRating.adapter = adapter
		uid = auth.currentUser?.uid ?: ""
		viewModel.getUserById(uid)
		pid = args.productId
		viewModel.getProductByPId(pid)
		viewModel.loadRatings(pid)
		swipeRefreshLayout = binding.swipeToRefresh
		swipeRefreshLayout.setOnRefreshListener {
			viewModel.getProductByPId(pid)

		}
		swipeRefreshLayout.setColorSchemeColors(
			Color.RED, Color.GREEN, Color.BLUE
		)
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.LTGRAY)
		viewModel.checkItemStateInCart(pid, uid)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
			OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				findNavController().popBackStack()
			}

		})
	}


	override fun setObserver() {
		viewModel.productQueryResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
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
					dataState.data?.let { product ->
						setUi(product)
						viewModel.getStoreByPId(product.sellerId)
					}
				}
			}

		}
		viewModel.storeQueryResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					Log.e("SHOPID", "setObserver: ${dataState.message}")
				}

				is DataState.Loading -> {
					Log.d("SHOPID", "setObserver: loading")

				}

				is DataState.Success -> {
					dataState.data?.let {
						Log.d("SHOPID", "setObserver: $it")
						setBottomAppBar(it)
					}

				}
			}
		}

		viewModel.userDetailsState.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					Log.e("SHOPID", "setObserver: ${dataState.message}")
				}

				is DataState.Loading -> {
					Log.d("SHOPID", "setObserver: loading")

				}

				is DataState.Success -> {
					dataState.data?.let {
						userName = it.name
						userImg = it.profileImageLink
					}
				}
			}

		}
		viewModel.ratingsLiveData.observe(viewLifecycleOwner) {
			adapter.submitList(it)
		}
		viewModel.itemStateInCart.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					Snackbar.make(
						binding.root,
						"Failed to load cart state: ${dataState.message}",
						Snackbar.LENGTH_SHORT
					)
						.show()
				}

				is DataState.Loading -> {
					binding.btnAddToCart.apply {
						text = "Loading..."
						isEnabled = false
					}
				}

				is DataState.Success -> {
					binding.btnAddToCart.apply {

						text = if (dataState.data == true) {
							isEnabled = false
							"Already in cart"
						} else {
							isEnabled = true
							"Add to cart"
						}
					}
				}
			}

		}
	}


	private fun setUi(product: Product) = with(binding) {
		product.apply {
			(activity as? AppCompatActivity)?.supportActionBar?.title = name
			productNameView.text = name
			productPriceTv.text = "$$price"
			productDetails.text = description
			productRatingsTv.text = "⭐${avgRating} ($ratingCount)"
			Glide.with(requireContext()).load(imageLink.toUri())
				.placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_image_upload).into(productImage)

			submitRatingAndReview.setOnClickListener {
				submitRatingAndReview()
			}

		}

	}

	private fun setBottomAppBar(shop: UserEntity) = with(binding) {
		shop.apply {
			Glide.with(requireContext()).load(profileImageLink.toUri())
				.placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_image_upload).into(actionOpenShop)

			btnAddToCart.setOnClickListener {
				addToCart()
			}
			actionOpenShop.setOnClickListener {
				val action =
					ProductDisplayFragmentDirections.actionProductDisplayFragmentToProductStoreFragment(
						storeId = shop.id
					)

				findNavController().navigate(action)
			}
			actionOpenChat.setOnClickListener {

				viewModel.createChatRoom(
					uId = uid,
					pId = shop.id,
					uNm = userName,
					sNm = shop.name,
					uImg = userImg,
					sImg = shop.profileImageLink
				).addOnSuccessListener {
					val cId = getConversationId(uid, shop.id)
					val action =
						ProductDisplayFragmentDirections.actionProductDisplayFragmentToMessageListFragment(
							cId
						)
					findNavController().navigate(action)

				}
			}
		}
	}


	private fun submitRatingAndReview() {
		val stars = binding.ratingBar.rating.toInt()
		val reviewText = binding.productReviewEt.editText?.text.toString()


		val rating = Rating(
			userId = uid,
			name = userName,
			rating = stars,
			review = reviewText
		)
		viewModel.saveUserRating(pid, rating)
	}

	private fun addToCart() {
		val item = CartEntry(
			pid,
			1
		)
		viewModel.addItemToCart(uid, item).addOnSuccessListener {
			Snackbar.make(binding.root, "Item added to cart successfully!", Snackbar.LENGTH_SHORT)
				.show()
		}.addOnFailureListener {
			Snackbar.make(
				binding.root,
				"Failed to add to cart: ${it.message}",
				Snackbar.LENGTH_SHORT
			)
				.show()
		}
		viewModel.checkItemStateInCart(pid, uid)

	}

}