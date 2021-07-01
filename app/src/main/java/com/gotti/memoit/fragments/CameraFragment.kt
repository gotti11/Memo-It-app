package com.gotti.memoit.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gotti.memoit.R
import com.gotti.memoit.databinding.FragmentCameraBinding
import com.gotti.memoit.databinding.FragmentHomeBinding
import com.gotti.memoit.others.Communicator
import com.gotti.memoit.others.Constants
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {

    //for binding
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var safeContext: Context
    private lateinit var communicator: Communicator
    var isTorchEnabled = false

    //for camera
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        communicator = activity as Communicator

        val sharedPref = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isFirstTime", false)
        editor.commit()

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    Constants.REQUIRED_PERMISSIONS,
                    Constants.REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        binding.shotPhotoFAB.setOnClickListener {
            var pictureCaptured = binding.viewFinderCameraView.bitmap //taking the bitmap(capture) from camera view

            if (pictureCaptured != null){
                communicator.passDataCom(pictureCaptured)
            } else {
                Toast.makeText(safeContext, "error it is null", Toast.LENGTH_LONG).show()
            }

        }

        //set up listener for changing camera front/back
        binding.switchCameraFAB.setOnClickListener {

            lensFacing = if (CameraSelector.DEFAULT_FRONT_CAMERA == lensFacing) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            startCamera()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinderCameraView.surfaceProvider)
                    }

//            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                        this, lensFacing, preview)

                val cam = cameraProvider.bindToLifecycle(
                        this, lensFacing, preview)

                //enabling-disabling flash
                binding.flashCameraFAB.setOnClickListener {
                    if ( cam.cameraInfo.hasFlashUnit() ) {

                            if (!isTorchEnabled) {
                                cam.cameraControl.enableTorch(true)
                                isTorchEnabled = true
                            } else {
                                cam.cameraControl.enableTorch(false)
                                isTorchEnabled = false
                            }

                    } else {
                        Toast.makeText(safeContext, "no flash available", Toast.LENGTH_LONG).show()
                    }
                }

            } catch (exc: Exception){
                Log.e(Constants.TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun allPermissionsGranted() = Constants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}