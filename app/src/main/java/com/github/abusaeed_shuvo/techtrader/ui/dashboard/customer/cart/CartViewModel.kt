package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntry
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntryLocal
import com.github.abusaeed_shuvo.techtrader.data.repository.CartRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
	private val repository: CartRepository
) : ViewModel() {
	private val _cartListResponse = MutableLiveData<DataState<List<CartEntryLocal>>>()
	val cartListResponse = _cartListResponse


	fun getAllCartItems() {
		_cartListResponse.postValue(DataState.Loading())

		repository.getAllCartItems()
			?.addOnSuccessListener { documentSnapshots ->
				val cartList = mutableListOf<CartEntryLocal>()
				val tasks = mutableListOf<Task<DocumentSnapshot?>>()

				documentSnapshots?.documents?.forEach { documentSnapshot ->
					documentSnapshot.toObject(CartEntry::class.java)?.let { cartEntry ->
						val task = repository.getProductById(cartEntry.productId)
						tasks.add(task)

						task.addOnSuccessListener { doc ->
							val product = doc?.toObject(Product::class.java)
							val cartEntryLocal = if (product != null) {
								CartEntryLocal(
									productId = cartEntry.productId,
									productName = product.name,
									productImage = product.imageLink,
									price = product.price,
									quantity = cartEntry.quantity,
									totalPrice = product.price * cartEntry.quantity
								)
							} else {
								CartEntryLocal(
									productId = cartEntry.productId,
									quantity = 0,
									productName = "The product has been removed from store",
									productImage = "",
									price = 0.0,
									totalPrice = 0.0
								)
							}
							cartList.add(cartEntryLocal)
						}.addOnFailureListener { exception ->
							_cartListResponse.postValue(DataState.Error(exception.message))
						}
					}
				}

				// ✅ Wait until *all* product fetches complete
				Tasks.whenAllComplete(tasks).addOnSuccessListener {
					_cartListResponse.postValue(DataState.Success(cartList))
				}.addOnFailureListener { exception ->
					_cartListResponse.postValue(DataState.Error(exception.message))
				}
			}
			?.addOnFailureListener { exception ->
				_cartListResponse.postValue(DataState.Error(exception.message))
			}
	}


}

