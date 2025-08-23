package com.github.abusaeed_shuvo.techtrader.data.repository

import android.net.Uri
import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.models.Rating
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.models.cart.CartEntry
import com.github.abusaeed_shuvo.techtrader.data.service.CustomerService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class CustomerRepository @Inject constructor(
	private val db: FirebaseFirestore,
	private val storageRef: StorageReference
) : CustomerService {


	fun addToCart(uId: String, cartItem: CartEntry): Task<Void?> {
		return db.collection(Nodes.USER)
			.document(uId)
			.collection(Nodes.CART)
			.document(cartItem.productId)
			.set(cartItem)
	}

	fun checkItemStateInCart(uId: String, pId: String): Task<DocumentSnapshot?> {
		return db.collection(Nodes.USER)
			.document(uId)
			.collection(Nodes.CART)
			.document(pId)
			.get()

	}

	fun uploadUserImage(productImageUri: Uri, userId: String): UploadTask {
		val storage: StorageReference =
			storageRef.child(Nodes.USER).child(userId)
		return storage.putFile(productImageUri)
	}

	fun getUserDataById(userId: String): Task<DocumentSnapshot?> {
		return db.collection(Nodes.USER).document(userId).get()
	}

	fun updateUserProfile(userEntity: UserEntity): Task<Void> {
		return db.collection(Nodes.USER).document(userEntity.id).update(
			mapOf(
				"name" to userEntity.name,
				"profileImageLink" to userEntity.profileImageLink
			)
		)
	}

	fun getRatings(pId: String): Task<QuerySnapshot> {
		return db.collection(Nodes.PRODUCT)
			.document(pId)
			.collection(Nodes.RATINGS)
			.orderBy(Nodes.TIME_STAMP, Query.Direction.DESCENDING)
			.get()
	}

	fun addOrUpdateRating(pid: String, rating: Rating): Task<Transaction?> {
		val productRef = db.collection(Nodes.PRODUCT).document(pid)
		val ratingRef = productRef.collection(Nodes.RATINGS).document(rating.userId)

		return db.runTransaction { transaction ->
			val productSnap = transaction.get(productRef)
			val product = productSnap.toObject(Product::class.java)
			              ?: throw FirebaseFirestoreException(
				              "Product not found",
				              FirebaseFirestoreException.Code.ABORTED
			              )
			val oldRatingSnap = transaction.get(ratingRef)
			val oldRating = oldRatingSnap.toObject(Rating::class.java)

			transaction.set(ratingRef, rating)

			val oldAvg = product.avgRating
			val oldCount = product.ratingCount

			val newCount = if (oldRating == null) oldCount + 1 else oldCount
			val newAvg = if (oldRating == null) {
				(oldAvg * oldCount + rating.rating) / newCount
			} else {
				(oldAvg * oldCount - oldRating.rating + rating.rating) / newCount
			}

			transaction.update(
				productRef, mapOf(
					"avgRating" to newAvg,
					"ratingCount" to newCount
				)
			)

		}
	}

	override fun getAllProducts(): Task<QuerySnapshot> {
		return db.collection(Nodes.PRODUCT).get()
	}

	override fun getProductById(pId: String): Task<DocumentSnapshot?> {

		return db.collection(Nodes.PRODUCT).document(pId).get()

	}

	override fun getStoreDataById(sId: String): Task<DocumentSnapshot?> {
		return db.collection(Nodes.USER).document(sId).get()
	}

	fun getAllProductByStoreId(sId: String): Task<QuerySnapshot> {
		return db.collection(Nodes.PRODUCT).whereEqualTo(Nodes.SELLER_ID, sId).get()
	}
}