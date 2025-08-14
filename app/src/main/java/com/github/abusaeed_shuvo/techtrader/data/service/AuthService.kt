package com.github.abusaeed_shuvo.techtrader.data.service

import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.models.UserLogin
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthService {
	fun userRegistration(userSignup: UserSignup): Task<AuthResult>
	fun userLogin(userLogin: UserLogin): Task<AuthResult>
	fun createUser(userEntity: UserEntity): Task<Void>
}