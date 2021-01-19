package com.example.testephotos

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.testephotos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.isEnabled = false

        action()

    }

    /**
     * Activating the camera
     */
    private fun action() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 111)
        } else {
            binding.button.isEnabled = true
            binding.button.setOnClickListener {
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 101)
            }
        }

    }

    /**
     * Request permission
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.button.isEnabled = true
        }

    }


    /**
     * Show image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            var pic = data?.getParcelableExtra<Bitmap>("data")
            pic?.apply {
                binding.imageView.setImageBitmap(pic)
                binding.imageView.setImageBitmap(rotate(-90F))

            }
        }

    }

    /**
     * Rotate Image
     */

    fun Bitmap.rotate(degrees: Float = 180F): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees)

        return Bitmap.createBitmap(
                this, // source bitmap
                0, // x coordinate of the first pixel in source
                0, // y coordinate of the first pixel in source
                width, // The number of pixels in each row
                height, // The number of rows
                matrix, // Optional matrix to be applied to the pixels
                false // true if the source should be filtered
        )
    }


}