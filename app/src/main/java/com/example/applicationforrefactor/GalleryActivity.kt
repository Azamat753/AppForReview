package com.example.applicationforrefactor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.applicationforrefactor.GetGalleryData.listOfImages
import com.example.applicationforrefactor.databinding.ActivityGalleryBinding
import com.google.gson.Gson

data class GalleryImage(
    var imagePath : String? = null,
    var isSelected : Boolean? = null
)


class GalleryActivity : AppCompatActivity(), GalleryImagesAdapter.GalleryListener {

    private val PERMISSION_REQUEST_CODE = 100
    lateinit var binding: ActivityGalleryBinding
    private var images: ArrayList<GalleryImage> = ArrayList()
    var selectImages = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadImages()

        }

        binding.closeButton.setOnClickListener { onBackPressed()

        }

        binding.doneButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("images", Gson().toJson(selectImages))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun loadImages() {
        binding.galleryRecycler.setHasFixedSize(true)
        binding.galleryRecycler.layoutManager = GridLayoutManager(this, 4)
        images = listOfImages(this)
        binding.galleryRecycler.adapter = GalleryImagesAdapter(images, selectImages, this)
        setImageState()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages()
            } else {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onClickItem(isSelected: Boolean, galleryImage: GalleryImage) {
        if (isSelected) {
            selectImages.add(galleryImage.imagePath!!)
        } else {
            selectImages.remove(galleryImage.imagePath)
        }
        setImageState()
    }


    private fun setImageState() {
        var state = "${selectImages.size} фото"


        if (!selectImages.isNullOrEmpty())  {
            state = "Вы выбрали фотографию."
        }

        binding.imageSelectCount.text = state
    }

}