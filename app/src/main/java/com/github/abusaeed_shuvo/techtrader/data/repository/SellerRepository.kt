package com.github.abusaeed_shuvo.techtrader.data.repository

import android.net.Uri
import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.service.SellerService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class SellerRepository @Inject constructor(
	private val db: FirebaseFirestore,
	private val storageRef: StorageReference
) : SellerService {
	override fun uploadProductImage(productImageUri: Uri): UploadTask {
		val storage: StorageReference =
			storageRef.child(Nodes.PRODUCT).child("PRODUCT_${System.currentTimeMillis()}")
		return storage.putFile(productImageUri)
	}

	override fun uploadProduct(product: Product): Task<Void> {
		return db.collection(Nodes.PRODUCT).document(product.productId).set(product)
	}

	override fun getAllProductByUserId(userId: String): Task<QuerySnapshot> {
		return db.collection(Nodes.PRODUCT).whereEqualTo(Nodes.SELLER_ID, userId).get()
	}
}