package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product_display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.models.Rating
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntry
import com.github.abusaeed_shuvo.techtrader.data.repository.CustomerRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDisplayViewModel @Inject constructor(
	private val repository: CustomerRepository
) : ViewModel() {
	private val _productQueryResponse = MutableLiveData<DataState<Product>>()
	val productQueryResponse = _productQueryResponse

	private val _storeQueryResponse = MutableLiveData<DataState<UserEntity>>()
	val storeQueryResponse = _storeQueryResponse

	private val _ratingUploadResponse = MutableLiveData<DataState<Rating>>()
	val ratingUploadResponse = _ratingUploadResponse

	private val _userDetailsState = MutableLiveData<DataState<UserEntity>>()
	val userDetailsState = _userDetailsState

	private val _ratingsLiveData = MutableLiveData<List<Rating>>()
	val ratingsLiveData: LiveData<List<Rating>> = _ratingsLiveData

	private val _itemStateInCart = MutableLiveData<DataState<Boolean>>(DataState.Success(false))
	val itemStateInCart = _itemStateInCart

	fun getProductByPId(pid: String) {
		_productQueryResponse.postValue(DataState.Loading())
		repository.getProductById(pid).addOnSuccessListener { queryDocumentSnapshot ->
			val product = queryDocumentSnapshot?.toObject(Product::class.java)
			if (product != null) {
				_productQueryResponse.postValue(DataState.Success(product))
			} else {
				_productQueryResponse.postValue(DataState.Error("Product not found"))
			}

		}.addOnFailureListener { exception ->
			_productQueryResponse.postValue(DataState.Error(exception.message))

		}
	}

	fun getStoreByPId(pid: String) {
		_storeQueryResponse.postValue(DataState.Loading())

		repository.getStoreDataById(pid).addOnSuccessListener { queryDocumentSnapshot ->
			queryDocumentSnapshot?.toObject(UserEntity::class.java).let { store ->
				_storeQueryResponse.postValue(DataState.Success(store))
			}
		}.addOnFailureListener { exception ->
			_storeQueryResponse.postValue(DataState.Error(exception.message))

		}
	}

	fun getUserById(userId: String) {
		_userDetailsState.postValue(DataState.Loading())

		repository.getUserDataById(userId).addOnSuccessListener { documentSnapshot ->
			val user = documentSnapshot?.toObject(UserEntity::class.java)

			if (user != null) {
				_userDetailsState.postValue(DataState.Success(user))
			} else {
				_userDetailsState.postValue(DataState.Error("User not found"))
			}
		}.addOnFailureListener { exception ->
			_userDetailsState.postValue(DataState.Error("${exception.message}"))
		}
	}


	fun saveUserRating(pid: String, rating: Rating) {
		_ratingUploadResponse.postValue(DataState.Loading())
		repository.addOrUpdateRating(pid, rating)
			.addOnSuccessListener {
				_ratingUploadResponse.postValue(DataState.Success(rating))
			}.addOnFailureListener {
				_ratingUploadResponse.postValue(DataState.Error(it.message))
			}
	}

	fun loadRatings(productId: String) {
		repository.getRatings(productId)
			.addOnSuccessListener { snapshot ->
				val ratings = snapshot.toObjects(Rating::class.java)
				_ratingsLiveData.postValue(ratings)
			}
			.addOnFailureListener {
				_ratingsLiveData.postValue(emptyList()) // or handle error
			}
	}

	fun addItemToCart(uId: String, item: CartEntry): Task<Void?> {
		return repository.addToCart(uId, item)
	}

	fun checkItemStateInCart(pId: String, uId: String) {
		_itemStateInCart.postValue(DataState.Loading())

		repository.checkItemStateInCart(uId, pId)
			.addOnSuccessListener {
				_itemStateInCart.postValue(DataState.Success(it?.exists()))
			}.addOnFailureListener {
				_itemStateInCart.postValue(DataState.Success(false))
			}
	}

}