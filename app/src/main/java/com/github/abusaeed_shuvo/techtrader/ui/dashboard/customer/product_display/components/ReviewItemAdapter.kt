package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product_display.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.abusaeed_shuvo.techtrader.data.models.Rating
import com.github.abusaeed_shuvo.techtrader.databinding.ItemRatingReviewBinding
import com.github.abusaeed_shuvo.techtrader.libs.getFormattedTime

class ReviewItemAdapter : ListAdapter<Rating, ReviewItemAdapter.ReviewItemVH>(ADiffCallBack()) {
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ReviewItemVH {
		return ReviewItemVH(
			ItemRatingReviewBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun onBindViewHolder(
		holder: ReviewItemVH,
		position: Int
	) {
		val rating = getItem(position)
		holder.bind(rating)
	}


	inner class ReviewItemVH(private val binding: ItemRatingReviewBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(rating: Rating) = with(binding) {
			rating.apply {
				reviewText.text = review
				ratingBar.rating = rating.rating.toFloat()
				ratingName.text = name
				reviewTimeStamp.text = getFormattedTime(timeStamp)
			}
		}
	}

	class ADiffCallBack : DiffUtil.ItemCallback<Rating>() {
		override fun areItemsTheSame(
			oldItem: Rating,
			newItem: Rating
		): Boolean = oldItem.userId == newItem.userId

		override fun areContentsTheSame(
			oldItem: Rating,
			newItem: Rating
		): Boolean = oldItem == newItem
	}
}