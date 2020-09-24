package com.example.applicationforrefactor.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationforrefactor.R
import com.example.applicationforrefactor.databinding.ItemGalleryImageBinding
import com.example.applicationforrefactor.extension.loadImage
import com.example.applicationforrefactor.model.GalleryImage

class GalleryImagesAdapter(
    var list: MutableList<GalleryImage>,
    var selectImages: ArrayList<String>,
    var listener: GalleryListener
) : RecyclerView.Adapter<GalleryImagesAdapter.GalleryImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImagesViewHolder {
        val binding: ItemGalleryImageBinding? = DataBindingUtil.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_image, parent, false)
        )
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
    ) : RecyclerView.ViewHolder(binding.root)

    interface GalleryListener {
        fun onClickItem(isSelected: Boolean, galleryImage: GalleryImage)
    }
}