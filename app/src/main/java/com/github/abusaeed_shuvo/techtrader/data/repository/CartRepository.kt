package com.github.abusaeed_shuvo.techtrader.data.repository

import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntry
import com.github.abusaeed_shuvo.techtrader.data.service.CartService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import javax.inject.Inject

class CartRepository @Inject constructor(
	private val db: FirebaseFirestore,
	private val auth: FirebaseAuth
) : CartService {

	val userId: String?
		get() = auth.currentUser?.uid

	private fun cartRef(): CollectionReference? {
		return userId?.let { uId ->
			db.collection(Nodes.USER)
				.document(uId)
				.collection(Nodes.CART)

		}

	}

	override fun addToCart(cartItem: CartEntry): Task<Void?>? {
		return cartRef()
			?.document(cartItem.productId)
			?.set(cartItem)
	}

	override fun removeFromCart(
		uId: String, cartEntry: CartEntry
	): Task<Void?>? {
		return cartRef()
			?.document(cartEntry.productId)
			?.delete()
	}

	override fun getAllCartItems(): Task<QuerySnapshot?>? {
		return cartRef()?.get()
	}

	fun getDBBatch(): WriteBatch {
		return db.batch()
	}

	fun updateCartItemQuantity(pId: String, newQuantity: Long): Task<Void?>? {
		return cartRef()?.document(pId)?.update("quantity", newQuantity)
	}

	fun getProductById(pId: String): Task<DocumentSnapshot?> {

		return db.collection(Nodes.PRODUCT).document(pId).get()

	}

}