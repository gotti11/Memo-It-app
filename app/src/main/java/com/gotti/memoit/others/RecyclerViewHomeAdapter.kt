package com.gotti.memoit.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gotti.memoit.R
import com.gotti.memoit.databinding.FragmentInfoBinding
import com.gotti.memoit.databinding.PhotoRecyclerviewItemBinding

class RecyclerViewHomeAdapter(private val photoTaken: ArrayList<PhotoTaken>): RecyclerView.Adapter<RecyclerViewHomeAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: PhotoRecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHomeAdapter.MyViewHolder {
        val row = PhotoRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: RecyclerViewHomeAdapter.MyViewHolder, position: Int) {
        val currentItem = photoTaken[position]

        holder.binding.takenPhotoImageView.setImageResource(currentItem.getPhoto)
        holder.binding.nameOfPhotoTextView.text = currentItem.getName
        holder.binding.DateOfPhotoTextview.text = currentItem.getDateTaken
    }

    override fun getItemCount() = photoTaken.size



}