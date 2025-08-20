package com.github.abusaeed_shuvo.techtrader.data.repository

import android.net.Uri
import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.service.CustomerService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class CustomerRepository @Inject constructor(
	private val db: FirebaseFirestore,
	private val storageRef: StorageReference
) : CustomerService {


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

	override fun getAllProducts(): Task<QuerySnapshot> {
		return db.collection(Nodes.PRODUCT).get()
	}
}