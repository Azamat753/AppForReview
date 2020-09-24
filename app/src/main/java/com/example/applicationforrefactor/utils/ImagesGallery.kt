package com.example.applicationforrefactor.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.applicationforrefactor.model.GalleryImage

object GetGalleryData {
    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.Q)
    fun listOfImages(context : Context) :ArrayList<GalleryImage> {
        val cursor : Cursor
        val column_index_data : Int
        val listOfAllImages : ArrayList<GalleryImage> = ArrayList()
        var absolutePathOfImage : String
        val uri : Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy : String = MediaStore.Images.Media.DATE_TAKEN
        cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC")!!
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursor.moveToNext()){
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(GalleryImage(absolutePathOfImage, false))
        }
        return listOfAllImages
    }
}