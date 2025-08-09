package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.abusaeed_shuvo.techtrader.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UploadProductFragment : Fragment() {

	companion object {
		fun newInstance() = UploadProductFragment()
	}

	private val viewModel: UploadProductViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// TODO: Use the ViewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return inflater.inflate(R.layout.fragment_upload_product, container, false)
	}
}