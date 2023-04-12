package com.example.uioproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotosAdapter(private val photosData: List<Bitmap>,val tags: Tags) :
    RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photo_image_view)
        val tagPeople : TextView = itemView.findViewById(R.id.tags_people)
        val tagDesc : TextView = itemView.findViewById(R.id.tags_des)
        val tagLoc : TextView = itemView.findViewById(R.id.tags_loc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUrl = photosData[position]

        // Load the image using Glide or any other image loading library
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.photoImageView)

        holder.tagPeople.text = tags.getTagPeopleName(position)
        holder.tagDesc.text = tags.getTagDes(position)
        holder.tagLoc.text = tags.getTagLocation(position)
        holder.tagLoc.setOnClickListener{
            val locationPickerIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${tags.getTagLocation(position)}"))
            startActivity(holder.tagLoc.context,locationPickerIntent,null)

        }


    }

    override fun getItemCount(): Int {
        return photosData.size
    }
}
