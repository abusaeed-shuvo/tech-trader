package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.cart.componets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntryLocal
import com.github.abusaeed_shuvo.techtrader.databinding.ItemCartProductBinding

class CartItemAdapter(
	val onPlusClicked: (item: CartEntryLocal) -> Unit,
	val onMinusClicked: (item: CartEntryLocal) -> Unit,
	val onRemoveClicked: (item: CartEntryLocal) -> Unit
) : ListAdapter<CartEntryLocal, CartItemAdapter.CartItemVH>(CartDiffUtil()) {
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): CartItemVH {
		return CartItemVH(
			ItemCartProductBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false,

				)
		)
	}

	override fun onBindViewHolder(
		holder: CartItemVH,
		position: Int
	) {
		val item = getItem(position)
		holder.bind(item)
	}


	inner class CartItemVH(private val binding: ItemCartProductBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: CartEntryLocal) = with(binding) {
			productName.text = item.productName
			productPrice.text = "$${item.price}"
			productQuantity.text = item.quantity.toString()
			productQuantity2.text = item.quantity.toString()
			productPriceTotal.text = "$${item.totalPrice}"
			Glide.with(binding.root.context).load(item.productImage.toUri())
				.placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_image_upload).into(productImage)

			btnRemoveItemFromCart.setOnClickListener {
				onRemoveClicked(item)
			}
			btnRemoveProductItem.setOnClickListener {
				onMinusClicked(item)
			}
			btnAddProductItem.setOnClickListener {
				onPlusClicked(item)
			}
		}
	}

	class CartDiffUtil() : DiffUtil.ItemCallback<CartEntryLocal>() {
		override fun areItemsTheSame(
			oldItem: CartEntryLocal,
			newItem: CartEntryLocal
		): Boolean = oldItem.productId == newItem.productId

		override fun areContentsTheSame(
			oldItem: CartEntryLocal,
			newItem: CartEntryLocal
		): Boolean = oldItem == newItem

	}
}