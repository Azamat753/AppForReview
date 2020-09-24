package com.example.applicationforrefactor.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.applicationforrefactor.R
import com.example.applicationforrefactor.databinding.ActivityGalleryBinding
import com.example.applicationforrefactor.extension.showToast
import com.example.applicationforrefactor.model.GalleryImage
import com.example.applicationforrefactor.recycler.GalleryImagesAdapter
import com.example.applicationforrefactor.utils.GetGalleryData.listOfImages
import com.google.gson.Gson

class GalleryActivity : AppCompatActivity(), GalleryImagesAdapter.GalleryListener {

    private val PERMISSION_REQUEST_CODE = 100
    lateinit var binding: ActivityGalleryBinding
    private var images: ArrayList<GalleryImage> = ArrayList()
    var selectImages = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.Q)
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
            intent.putExtra(getString(R.string.images), Gson().toJson(selectImages))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadImages() {
        binding.galleryRecycler.setHasFixedSize(true)
        binding.galleryRecycler.layoutManager = GridLayoutManager(this, 4)
        images = listOfImages(this)
        binding.galleryRecycler.adapter = GalleryImagesAdapter(images, selectImages, this)
        setImageState()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
                this.showToast("")
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
        var state = selectImages.size.toString() + getString(R.string.photo)

        if (!selectImages.isNullOrEmpty())  {
            state = getString(R.string.you_choose_image)
        }
        binding.imageSelectCount.text = state
    }

}