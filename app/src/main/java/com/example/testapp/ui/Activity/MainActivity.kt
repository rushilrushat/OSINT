package com.example.testapp.ui.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.R
import com.example.testapp.util.permissions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

	private val PermissionsRequestCode = 123
	private lateinit var managePermissions: permissions


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		// Initialize a list of required permissions to request runtime
		val list = listOf<String>(
				Manifest.permission.INTERNET,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE
		)

		// Initialize a new instance of ManagePermissions class
		managePermissions = permissions(this, list, PermissionsRequestCode)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			managePermissions.checkPermissions()

		ip_button.setOnClickListener {
			intent = Intent(this, com.example.testapp.ui.Activity.iplookup::class.java)
			startActivity(intent)
		}
		true_button.setOnClickListener {
			intent = Intent(this, com.example.testapp.ui.Activity.trucaller::class.java)
			startActivity(intent)
		}
		dns_button.setOnClickListener {
			intent = Intent(this, com.example.testapp.ui.Activity.iplookup::class.java)
			startActivity(intent)
		}
		tower_info.setOnClickListener {
			intent = Intent(this, Towerinfo::class.java)
			startActivity(intent)
		}

	}

	// Receive the permissions request result
	override fun onRequestPermissionsResult(
			requestCode: Int, permissions: Array<String>,
			grantResults: IntArray
	) {
		when (requestCode) {
			PermissionsRequestCode -> {
				val isPermissionsGranted = managePermissions
						.processPermissionsResult(requestCode, permissions, grantResults)

				if (isPermissionsGranted) {
					// Do the task now
					toast("Permissions granted.")
				} else {
					toast("Permissions denied.")
				}
				return
			}
		}
	}
}

fun Context.toast(message: String) {
	Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}