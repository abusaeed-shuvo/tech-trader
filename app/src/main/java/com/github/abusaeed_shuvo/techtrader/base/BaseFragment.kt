package com.github.abusaeed_shuvo.techtrader.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.abusaeed_shuvo.techtrader.libs.LoadingDialog

abstract class BaseFragment<VB : ViewBinding>(
	private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {
	private var _binding: VB? = null
	val binding get() = _binding!!
	lateinit var loading: LoadingDialog

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = bindingInflater.invoke(inflater)
		loading = LoadingDialog(requireContext())
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListener()
		setObserver()

	}

	abstract fun setListener()
	abstract fun setObserver()
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	fun requestPermissions(
		request: ActivityResultLauncher<Array<String>>,
		permissions: Array<String>
	) {
		request.launch(permissions)
	}

	fun areAllPermissionsGranted(permissions: Array<String>): Boolean {
		return permissions.all {
			ContextCompat.checkSelfPermission(
				requireContext(),
				it
			) == PackageManager.PERMISSION_GRANTED
		}
	}

}