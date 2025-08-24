package com.github.abusaeed_shuvo.techtrader.data.service

import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntry
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface CartService {
	fun addToCart(cartItem: CartEntry): Task<Void?>?
	fun removeFromCart(cartEntry: CartEntry): Task<Void?>?
	fun getAllCartItems(): Task<QuerySnapshot?>?
}