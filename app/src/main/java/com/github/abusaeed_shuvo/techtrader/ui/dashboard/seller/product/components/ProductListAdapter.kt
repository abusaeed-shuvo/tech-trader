package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.databinding.ItemProductBinding


class ProductListAdapter :
	ListAdapter<
			Product, ProductListAdapter.ProductVH>(ADiffCallBack()) {
	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ProductVH {
		return ProductVH(
			ItemProductBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: ProductVH, position: Int
	) {
		val notification = getItem(position)
		holder.bind(notification)
	}

	inner class ProductVH(private val binding: ItemProductBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(product: Product) = with(binding) {
			productName.text = product.name
			productDesc.text = product.description
			productPrice.text = "Price: ${product.price}$"
			productQuantity.text = "Quantity: ${product.quantity}"
			Glide.with(binding.root.context).load(product.imageLink.toUri())
				.placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_image_upload).into(productImage)

		}
	}

	class ADiffCallBack : DiffUtil.ItemCallback<Product>() {
		override fun areItemsTheSame(
			oldItem: Product,
			newItem: Product
		): Boolean = oldItem.productId == newItem.productId

		override fun areContentsTheSame(
			oldItem: Product,
			newItem: Product
		): Boolean = oldItem == newItem


	}
}