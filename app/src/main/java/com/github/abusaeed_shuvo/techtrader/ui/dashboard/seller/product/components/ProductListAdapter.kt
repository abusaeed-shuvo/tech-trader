package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.models.ProductEntity
import com.github.abusaeed_shuvo.techtrader.databinding.ItemProductBinding


class ProductListAdapter :
	ListAdapter<ProductEntity, ProductListAdapter.ProductVH>(ADiffCallBack()) {
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
		fun bind(productEntity: ProductEntity) = with(binding) {
			productName.text = productEntity.name
			productDesc.text = productEntity.description
			productPrice.text = "Price: ${productEntity.price}$"
			productQuantity.text = "Quantity: ${productEntity.quantity.toString()}"
			Glide.with(binding.root.context).load(productEntity.imageLink)
				.placeholder(R.drawable.ic_image_upload).error(R.drawable.ic_error)
				.into(binding.productImage)

		}
	}

	class ADiffCallBack : DiffUtil.ItemCallback<ProductEntity>() {
		override fun areItemsTheSame(
			oldItem: ProductEntity, newItem: ProductEntity
		): Boolean {
			return oldItem.productId == newItem.productId
		}

		override fun areContentsTheSame(
			oldItem: ProductEntity, newItem: ProductEntity
		): Boolean {
			return oldItem == newItem
		}

	}
}