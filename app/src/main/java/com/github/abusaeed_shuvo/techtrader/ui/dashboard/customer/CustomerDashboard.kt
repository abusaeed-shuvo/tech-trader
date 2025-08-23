package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.databinding.ActivityCustomerDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CustomerDashboard : AppCompatActivity() {
	private lateinit var binding: ActivityCustomerDashboardBinding
	private lateinit var navController: NavController
	private lateinit var appBarConfiguration: AppBarConfiguration

	@Inject
	lateinit var auth: FirebaseAuth


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivityCustomerDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
			val systemBars = insets.getInsets(
				WindowInsetsCompat.Type.systemBars()
						or WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.displayCutout()
			)
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		navController = findNavController(R.id.fragmentContainerView3)

		appBarConfiguration = AppBarConfiguration(
			navController.graph
		)

		binding.customerBtmNav.setupWithNavController(navController)
		setupActionBarWithNavController(navController, appBarConfiguration)

		navController.addOnDestinationChangedListener { controller, destination, arguments ->
			when (destination.id) {
				R.id.cartFragment, R.id.productListFragment, R.id.customerMessageFragment,
				R.id.customerProfileFragment -> {
					binding.customerBtmNav.visibility = View.VISIBLE
				}

				else                         -> {
					binding.customerBtmNav.visibility = View.GONE
				}
			}
		}

	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}


}