package com.example.applicationforrefactor

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.Fade
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.applicationforrefactor.databinding.ItemGalleryImageBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

fun ImageView.loadImage(context: Context, url: String?, placeholder: Int = 0) {
    val imageView = this
    Glide.with(context)
        .asBitmap()
        .centerCrop()
        .placeholder(placeholder)
        .load(url)
        .apply(
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).priority(Priority.HIGH)
        )
        .into(object : BitmapImageViewTarget(this) {
            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                val viewWidthToBitmapWidthRatio =
                    imageView.width.toDouble() / bitmap.width.toDouble()
                imageView.layoutParams.height =
                    (bitmap.height * viewWidthToBitmapWidthRatio).toInt()
                imageView.setImageBitmap(bitmap)
            }
        })

}

class GalleryImagesAdapter(
    var list: MutableList<GalleryImage>,
    var selectImages: ArrayList<String>,
    var listener: GalleryListener
) :
    RecyclerView.Adapter<GalleryImagesAdapter.GalleryImagesViewHolder>() {

    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImagesViewHolder {
        val binding: ItemGalleryImageBinding? = DataBindingUtil.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_image, parent, false)
        )

        context = parent.context

        return GalleryImagesViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GalleryImagesViewHolder, position: Int) {
        val image = list[position]
        holder.binding.image.loadImage(holder.itemView.context, image.imagePath)

        for (imagePath in selectImages) {
            if (imagePath == image.imagePath)
                image.isSelected = true
        }

        if (image.isSelected!!) {
            holder.binding.selectImage.visibility = View.VISIBLE
        } else {
            holder.binding.selectImage.visibility = View.GONE
        }

        holder.binding.itemView.setOnClickListener {
            val count = 10
            if (selectImages.size < count) {
                image.isSelected = !image.isSelected!!
                listener.onClickItem(image.isSelected!!, image)
                notifyItemChanged(position)
            }
        }

        holder.binding.selectImage.setOnClickListener {
            image.isSelected = !image.isSelected!!
            listener.onClickItem(image.isSelected!!, image)
            notifyItemChanged(position)
        }
    }

    class GalleryImagesViewHolder(
        var binding: ItemGalleryImageBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

    interface GalleryListener {
        fun onClickItem(isSelected: Boolean, galleryImage: GalleryImage)
    }
}