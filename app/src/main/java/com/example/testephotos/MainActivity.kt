package com.example.testephotos

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.testephotos.databinding.ActivityMainBinding
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        action()

    }

    /**
     * Activating the camera
     */
    private fun action() {


            binding.button.setOnClickListener {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                photoFile = getPhotoFile(FILE_NAME)

                val fileProvider = FileProvider.getUriForFile(
                    this,
                    "com.example.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
                }

            }


    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }



    /**
     * Show image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            takenImage?.apply {
                binding.imageView.setImageBitmap(takenImage)
                binding.imageView.setImageBitmap(rotate(-90F))
            }


        } else {

            super.onActivityResult(requestCode, resultCode, data)

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