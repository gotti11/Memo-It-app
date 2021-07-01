package com.gotti.memoit.activities

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gotti.memoit.R
import com.gotti.memoit.databinding.ActivityMainBinding
import com.gotti.memoit.fragments.CameraFragment
import com.gotti.memoit.fragments.EditPictureFragment
import com.gotti.memoit.fragments.WelcomePage
import com.gotti.memoit.others.Communicator
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), Communicator {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        checkIfFirstTime()
        setContentView(view)
        binding.myBNavView.menu.getItem(1).isEnabled = false

        val bottomNavView = binding.myBNavView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_navHostFragment) as NavHostFragment
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.infoFragment))

        val navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)

        binding.addPhotoFab.setOnClickListener {

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.cameraFrag_FrameLayout, CameraFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun passDataCom(imageCaptured: Bitmap?) {
        val bundle = Bundle()
        val b: ByteArrayOutputStream = ByteArrayOutputStream()

        if (imageCaptured != null) {
            imageCaptured.compress(Bitmap.CompressFormat.PNG, 100, b)
        }
        val byteArray = b.toByteArray()
        bundle.putByteArray("image", byteArray)

        val transaction = this.supportFragmentManager.beginTransaction()
        val editPictureFragment = EditPictureFragment()
        editPictureFragment.arguments = bundle

        transaction.replace(R.id.cameraFrag_FrameLayout, editPictureFragment)
        transaction.commit()
    }

    fun checkIfFirstTime(){

        val sharedPref: SharedPreferences = getSharedPreferences("isFirstTime", 0)
        var isFirstTime = sharedPref.getBoolean("isFirstTime", true)

        if (isFirstTime){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.cameraFrag_FrameLayout, WelcomePage())
                commit()
            }
        } else {

        }
    }

}