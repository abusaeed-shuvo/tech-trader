package com.github.abusaeed_shuvo.techtrader.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface CustomerService {
	fun getAllProducts(): Task<QuerySnapshot>
}