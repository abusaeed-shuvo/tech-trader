package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.github.abusaeed_shuvo.techtrader.databinding.ActivitySellerDashboardBinding
import com.github.abusaeed_shuvo.techtrader.ui.notifications.NotificationActivity
import com.github.abusaeed_shuvo.techtrader.ui.register.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SellerDashboard : AppCompatActivity() {
	private lateinit var binding: ActivitySellerDashboardBinding
	private lateinit var navController: NavController
	private lateinit var appBarConfiguration: AppBarConfiguration

	@Inject
	lateinit var auth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivitySellerDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
			val systemBars =
				insets.getInsets(
					WindowInsetsCompat.Type.systemBars()
							or WindowInsetsCompat.Type.ime()
				)
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		navController = findNavController(R.id.fragmentContainerView2)

		appBarConfiguration = AppBarConfiguration(
			navController.graph
		)

		binding.bottomNav.setupWithNavController(navController)
		setupActionBarWithNavController(navController, appBarConfiguration)

		navController.addOnDestinationChangedListener { controller, destination, arguments ->
			when (destination.id) {
				R.id.myProductsFragment, R.id.uploadProductFragment, R.id.chatListFragment,
				R.id.sellerProfileFragment -> {
					binding.bottomNav.visibility = View.VISIBLE
				}

				else                       -> {
					binding.bottomNav.visibility = View.GONE
				}
			}
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}


	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.seller_top_menu, menu)

		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		when (item.itemId) {
			R.id.menu_seller_settings      -> {
				Snackbar.make(this, binding.main, "Clicked settings", Snackbar.LENGTH_SHORT).show()
			}

			R.id.menu_seller_report        -> {
				Snackbar.make(this, binding.main, "Clicked report", Snackbar.LENGTH_SHORT).show()

			}

			R.id.menu_seller_notifications -> {
				val route = Intent(this, NotificationActivity::class.java)
				startActivity(route)
			}

			R.id.menu_seller_logout        -> {
				MaterialAlertDialogBuilder(this)
					.setTitle("Do you want to logout?")
					.setMessage("${auth.currentUser?.email ?: "Current user"} will be logged out!")
					.setPositiveButton("Logout") { _, _ ->
						auth.signOut()
						startActivity(Intent(this@SellerDashboard, MainActivity::class.java))
						finish()
					}
					.setNegativeButton("Dismiss", null)
					.show()
			}

		}

		return super.onOptionsItemSelected(item)
	}
}