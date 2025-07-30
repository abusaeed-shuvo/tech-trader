package com.github.abusaeed_shuvo.techtrader.data.repository

import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.models.UserLogin
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.github.abusaeed_shuvo.techtrader.data.service.AuthService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class AuthRepository @Inject constructor(
	private val auth: FirebaseAuth,
	private val db: FirebaseFirestore
) : AuthService {
	override fun userRegistration(userSignup: UserSignup): Task<AuthResult> {
		return auth
			.createUserWithEmailAndPassword(userSignup.email, userSignup.password)
	}

	override fun userLogin(userLogin: UserLogin): Task<AuthResult> =
		auth.signInWithEmailAndPassword(userLogin.email, userLogin.password)

	override fun createUser(userSignup: UserSignup): Task<Void> {
		return db.collection(Nodes.USER).document(userSignup.id).set(userSignup)
	}


}