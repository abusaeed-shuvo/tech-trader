package com.github.abusaeed_shuvo.techtrader.data.service

import android.net.Uri
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask

interface SellerService {
	fun uploadProductImage(productImageUri: Uri): UploadTask

	fun uploadProduct(product: Product): Task<Void>

	fun getAllProductByUserId(userId: String): Task<QuerySnapshot>
}