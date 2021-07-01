package com.gotti.memoit.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gotti.memoit.R
import com.gotti.memoit.databinding.FragmentInfoBinding
import com.gotti.memoit.databinding.PhotoRecyclerviewItemBinding
import com.gotti.memoit.databinding.StickerPictureRecyclerviewItemBinding

class StickerPictureRecyclerViewAdapter(private val sticker: ArrayList<Int>, private val listener: onItemClickListener): RecyclerView.Adapter<StickerPictureRecyclerViewAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: StickerPictureRecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition

            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerPictureRecyclerViewAdapter.MyViewHolder {
        val row = StickerPictureRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: StickerPictureRecyclerViewAdapter.MyViewHolder, position: Int) {
        val currentItem = sticker[position]

        holder.binding.stickerImageView.setImageResource(currentItem)
    }

    override fun getItemCount() = sticker.size

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

}