package com.example.summary9.screen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.summary9.databinding.FragmentMainBinding
import com.example.summary9.extensino.showSnackbar
import com.example.summary9.helper.Listener
import com.example.summary9.screen.base.BaseFragment
import java.io.ByteArrayOutputStream

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate), Listener {

    private lateinit var galleryResultLauncher: ActivityResultLauncher<String>
    private lateinit var startCameraLauncher: ActivityResultLauncher<Intent>

    override fun init() {
        listeners()

        galleryResultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    val imageView: ImageView = binding.ivImage
                    imageView.setImageURI(uri)
                    binding.progressBar.visibility = View.GONE
                }
            }

        startCameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let {
                        val compressedBitmap = compressBitmap(it)
                        binding.ivImage.setImageBitmap(compressedBitmap)
                        binding.progressBar.visibility = View.GONE
                    }
                } else {
                    binding.root.showSnackbar("Failed to Capture image")
                }
            }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = BottomSheetDialog().apply {
            setListener(object : BottomSheetDialog.OptionSelected {
                override fun onOptionSelected(option: String) {
                    when (option) {
                        "TAKE_PICTURE" -> openCamera()
                        "CHOOSE_GALLERY" -> chooseFromGallery()
                    }
                }
            })
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }


    override fun listeners() {
        binding.buttonAddPhoto.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startCameraLauncher.launch(cameraIntent)
        } catch (e: ActivityNotFoundException) {
            binding.root.showSnackbar(e.message!!)
        }
    }

    private fun chooseFromGallery() {
        galleryResultLauncher.launch("image/*")
    }
}