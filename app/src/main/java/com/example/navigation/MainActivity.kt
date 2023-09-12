package com.example.navigation

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.os.Bundle
import com.example.navigation.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionDeniedResponse
import android.widget.Toast
import com.karumi.dexter.PermissionToken
import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraAccessException
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.listener.PermissionRequest

class MainActivity : AppCompatActivity() {

    var imageButton: ImageView? = null
    var state = false
    lateinit var nav_view : NavigationView
    lateinit var drawerLayout : DrawerLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawerLayout)

        nav_view.setNavigationItemSelectedListener{ menuItem ->
            when (menuItem.itemId) {
                R.id.nav_share -> {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "This App Share With Friends")
                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                    true
                }
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }

        }

        imageButton = findViewById(R.id.torchbtn)
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    runFlashlight()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@MainActivity,
                        "Camera Permission required",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                }
            }).check()
    }

    @SuppressLint("NewApi")
    private fun runFlashlight() {
        imageButton!!.setOnClickListener {
            if (!state) {
                val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                try {
                    val CameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(CameraId, true)
                    state = true
                    imageButton!!.setImageResource(R.drawable.on)
                } catch (e: CameraAccessException) {
                }
            } else {
                val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                try {
                    val CameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(CameraId, false)
                    state = false
                    imageButton!!.setImageResource(R.drawable.off)
                } catch (e: CameraAccessException) {
                }
            }
        }
    }
}


