package com.github.abusaeed_shuvo.techtrader.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface CustomerService {
	fun getAllProducts(): Task<QuerySnapshot>
	fun getProductById(pId: String): Task<DocumentSnapshot?>
	fun getStoreDataById(sId: String): Task<DocumentSnapshot?>
}