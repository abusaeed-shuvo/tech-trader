package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.databinding.ItemCustomerProductBinding

class CustomerProductItemAdapter
	(
	val onClick: (Product) -> Unit
) :
	ListAdapter<
			Product, CustomerProductItemAdapter.CustomerProductVH>(ADiffCallBack()) {
	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): CustomerProductVH {
		return CustomerProductVH(
			ItemCustomerProductBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: CustomerProductVH,
		position: Int
	) {
		val product = getItem(position)
		holder.bind(product)
	}


	inner class CustomerProductVH(private val binding: ItemCustomerProductBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(product: Product) = with(binding) {
			productNameTv.text = product.name
			productPriceTv.text = "Price: ${product.price}$"
			productRatingsTv.text = "⭐${product.avgRating} (${product.ratingCount})"
			if (product.discount > 0 && product.discount <= 100) {
				productDiscountTv.text = "-${product.discount}"
			}

			Glide.with(binding.root.context).load(product.imageLink.toUri())
				.placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_image_upload).into(productImg)
			root.setOnClickListener {
				onClick(product)
			}
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